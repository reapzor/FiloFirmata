package com.bortbort.arduino.FiloFirmata;

import com.bortbort.arduino.FiloFirmata.Listeners.GlobalMessageListener;
import com.bortbort.arduino.FiloFirmata.Listeners.MessageListener;
import com.bortbort.arduino.FiloFirmata.Listeners.ProtocolVersionListener;
import com.bortbort.arduino.FiloFirmata.Messages.*;
import com.bortbort.arduino.FiloFirmata.Parser.CommandParser;
import com.bortbort.arduino.FiloFirmata.Parser.MessageBuilder;
import com.bortbort.arduino.FiloFirmata.Parser.SysexCommandParser;
import com.bortbort.arduino.FiloFirmata.Parser.SysexMessageBuilder;
import com.bortbort.arduino.FiloFirmata.PortAdapters.*;
import com.bortbort.helpers.DataTypeHelpers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Implements the Firmata protocol with a Firmata supported device, using any number of custom commands. This library
 * supports adapting any serial port library with it. Within the configuration you can dictate a custom SerialPort
 * implementation just as JSSC, RXTX, etc (provided you write them). The default implementation uses PureJavaSerialComm.
 */
public class Firmata extends SerialPortEventListener {
    private static final Logger log = LoggerFactory.getLogger(Firmata.class);
    private static final Integer MIN_SUPPORTED_VERSION_MAJOR = 2;
    private static final Integer MIN_SUPPORTED_VERSION_MINOR = 5;

    /**
     * Map of listener objects registered to respond to specific message events.
     */
    private final HashMap<Class, HashMap<Integer, ArrayList<MessageListener>>> messageListenerMap = new HashMap<>();

    /**
     * Arraylist of global listener objects registered to respond to any message event.
     */
    private final ArrayList<GlobalMessageListener> globalListenerArray = new ArrayList<>();

    /**
     * Serial port adapter reference.
     */
    private SerialPort serialPort;

    /**
     * Firmata configuration reference.
     */
    private FirmataConfiguration configuration;

    /**
     * Flag identifying if the Firmata library (and serial port) is started.
     */
    private Boolean started = false;

    /**
     * Protocol Version lock object. used to synchronously wait for a response when testing communications.
     */
    private final Object protocolVersionLock = new Object();

    /**
     * Protocol Version Listener used to verify we can communicate with the Firmata device.
     * Warn the user if the library if too old for this library, but do not treat it like a fatal event, in the
     * hopes that it may work.
     */
    private final ProtocolVersionListener versionListener = new ProtocolVersionListener() {
        @Override
        public void messageReceived(ProtocolVersionMessage message) {
            log.info("Detected Firmata device protocol version: {}.{}",
                    message.getMajorVersion(), message.getMinorVersion());
            if (message.getMajorVersion() < MIN_SUPPORTED_VERSION_MAJOR ||
                    (message.getMinorVersion() < MIN_SUPPORTED_VERSION_MINOR &&
                            message.getMajorVersion() >= MIN_SUPPORTED_VERSION_MAJOR)) {
                log.warn("Firmata device protocol version is too old for this library." +
                                " Some functionality may not work. Library min supported version: {}.{}",
                        MIN_SUPPORTED_VERSION_MAJOR, MIN_SUPPORTED_VERSION_MINOR);
            }
            removeMessageListener(versionListener);
            synchronized (protocolVersionLock) {
                protocolVersionLock.notify();
            }
        }
    };


    /**
     * Add a custom parser to the Firmata library. When the command byte for the parser is received, the parser
     * will be responsible for turning the the data that follows into a Firmata message.
     *
     * @param messageBuilder Builder class that translates the byte message into a message object.
     */
    public static void addCustomCommandParser(MessageBuilder messageBuilder) {
        CommandParser.addParser(messageBuilder);
    }

    /**
     * Add a custom sysex parser to the Firmata library. When the command byte for the parser is received, the parser
     * will be responsible for turning the the data that follows into a Firmata message.
     *
     * @param messageBuilder SysexMessageBuilder object describing how to build a message.
     */
    public static void addCustomSysexParser(SysexMessageBuilder messageBuilder) {
        SysexCommandParser.addParser(messageBuilder);
    }


    /**
     * Implement the Firmata library using the default FirmataConfiguration().
     */
    public Firmata() {
        this.configuration = new FirmataConfiguration();
    }

    /**
     * Implement the Firmata library using a configured serial port identifier
     * @param comPort the communications port path for the serial port
     */
    public Firmata(String comPort) {
        this.configuration = new FirmataConfiguration(comPort);
    }

    /**
     * Implement the Firmata library using a configured serial port identifier
     * @param comPort the communications port path for the serial port
     * @param baudRate the communications speed the port is running at
     */
    public Firmata(String comPort, Integer baudRate) {
        this.configuration = new FirmataConfiguration(comPort, baudRate);
    }

    /**
     * Implement the Firmata library using a custom FirmataConfiguration().
     *
     * @param configuration FirmataConfiguration custom object to match your port/api needs.
     */
    public Firmata(FirmataConfiguration configuration) {
        this.configuration = new FirmataConfiguration(configuration);
    }


    /**
     * Add a GlobalMessageListener to the Firmta object which will fire whenever any message is received
     * over the SerialPort.
     *
     * @param messageListener GlobalMessageListener object to handle a received Message event over the SerialPort.
     */
    public void addGlobalMessageListener(GlobalMessageListener messageListener) {
        if (!globalListenerArray.contains(messageListener)) {
            globalListenerArray.add(messageListener);
        }
    }

    /**
     * Remove a GlobalMessageListener from the Firmata object which will stop the listener from responding to message
     * received events over the SerialPort.
     *
     * @param messageListener GlobalMessageListener object to remove.
     */
    public void removeGlobalMessageListener(GlobalMessageListener messageListener) {
        globalListenerArray.remove(messageListener);
    }

    /**
     * Add a messageListener to the Firmta object which will fire whenever a matching message is received
     * over the SerialPort that corresponds to the given channel. If the channel is null, the listener
     * will fire regardless of which channel (pin) the message is coming from.
     *
     * @param channel Integer indicating the specific channel or pin to listen on, or null to listen to
     *                all messages regardless of channel/pin.
     * @param messageListener MessageListener object to handle a received Message event over the SerialPort.
     */
    public void addMessageListener(Integer channel, MessageListener messageListener) {
        Class messageListenerClass = messageListener.getMessageType();

        // Build up the empty class listener map if not already there
        if (!messageListenerMap.containsKey(messageListenerClass)) {
            messageListenerMap.put(messageListenerClass, new HashMap<>());
        }

        // Get the message listener map for the given class
        HashMap<Integer, ArrayList<MessageListener>> listenerMap = messageListenerMap.get(messageListenerClass);

        // Builds up the empty channel listener array if not already there
        if (!listenerMap.containsKey(channel)) {
            listenerMap.put(channel, new ArrayList<>());
        }

        // Get the message listener array for the given channel
        ArrayList<MessageListener> messageListeners = listenerMap.get(channel);

        if (!messageListeners.contains(messageListener)) {
            messageListeners.add(messageListener);
        }
    }

    /**
     * Remove a messageListener from the Firmata object which will stop the listener from responding to message
     * received events over the SerialPort.
     *
     * @param channel Integer channel to remove the listener from.
     * @param messageListener MessageListener to be removed.
     */
    public void removeMessageListener(Integer channel, MessageListener messageListener) {
        Class messageListenerClass = messageListener.getMessageType();
        if (messageListenerMap.containsKey(messageListenerClass)) {
            HashMap<Integer, ArrayList<MessageListener>> listenerMap = messageListenerMap.get(messageListenerClass);
            if (listenerMap.containsKey(channel)) {
                ArrayList<MessageListener> messageListeners = listenerMap.get(channel);
                messageListeners.remove(messageListener);
            }
        }
    }

    /**
     * Add a messageListener to the Firmta object which will fire whenever a matching message is received
     * over the SerialPort. If the listener is for a ChannelMessage, the listener will fire regardless of which
     * channel (pin) the message is coming from.
     *
     * @param messageListener MessageListener object to handle a received Message event over the SerialPort.
     */
    public void addMessageListener(MessageListener messageListener) {
       addMessageListener(null, messageListener);
    }

    /**
     * Remove a messageListener from the Firmata object which will stop the listener from responding to message
     * received events over the SerialPort.
     *
     * @param messageListener MessageListener object to remove.
     */
    public void removeMessageListener(MessageListener messageListener) {
        removeMessageListener(null, messageListener);
    }


    /**
     * Send a Message over the serial port to a Firmata supported device.
     *
     * @param message TransmittableMessage object used to translate a series of bytes to the SerialPort.
     * @return True if the Message sent. False if the Message was not sent.
     */
    public Boolean sendMessage(TransmittableMessage message) {
        if (!started) {
            log.error("Firmata library is not connected / started! Cannot send message {}",
                    message.getClass().getSimpleName());
            return false;
        }

        try {
            log.debug("Transmitting message {}. Bytes: {}", message.getClass().getSimpleName(),
                    DataTypeHelpers.bytesToHexString(message.toByteArray()));
            serialPort.getOutputStream().write(message.toByteArray());
            return true;
        } catch (IOException e) {
            log.error("Unable to transmit message {} through serial port", message.getClass().getName());
            stop();
        }

        return false;
    }

    /**
     * Send a series of raw bytes over the serial port to a Firmata supported device.
     *
     * @param rawBytes byte array to be sent over the SerialPort OutputStream.
     * @return True if the bytes were sent. False if the bytes were not sent.
     */
    public Boolean sendRaw(byte... rawBytes) {
        if (!started) {
            log.error("Firmata library is not connected / started! Cannot send bytes {}",
                    DataTypeHelpers.bytesToHexString(rawBytes));
            return false;
        }

        try {
            serialPort.getOutputStream().write(rawBytes);
            return true;
        } catch (IOException e) {
            log.error("Unable to transmit raw bytes through serial port. Bytes: {}",
                    DataTypeHelpers.bytesToHexString(rawBytes));
            stop();
        }

        return false;
    }

    /**
     * Starts the firmata library. If the library is already started this will just return true.
     * Starts the SerialPort and handlers, and ensures the attached device is able to communicate with our
     * library. This will return an UnsupportedDevice exception if the attached device does not meet the library
     * requirements.
     *
     * @return True if the library started. False if the library failed to start.
     */
    public synchronized Boolean start() {
        if (started) {
            return true;
        }

        createSerialPort();

        serialPort.addEventListener(this);

        if (!serialPort.connect()) {
            log.error("Failed to start Firmata Library. Cannot connect to Serial Port.");
            log.error("Configuration is {}", configuration);
            stop();
            return false;
        }

        if (configuration.getTestProtocolCommunication()) {
            if (!testProtocolCommunication()) {
                stop();
                return false;
            }
        }

        started = true;
        return true;
    }

    /**
     * Stops the Firmata library. If the library is already stopped, this will still attempt to stop anything
     * that may still be lingering. Take note that many calls could eventually result in noticeable cpu usage.
     *
     * @return True if the library has stopped. False if there was an error stopping the library.
     */
    public synchronized Boolean stop() {
        if (serialPort != null) {
            serialPort.removeEventListener(this);
        }

        if (!removeSerialPort()) {
            log.error("Failed to stop Firmata Library. Cannot close Serial Port.");
            log.error("Configuration is {}", configuration);
            return false;
        }

        started = false;
        return true;
    }

    /**
     * Remove all registered listeners from the Firmata library regardless of class and channel.
     */
    public void removeAllListeners() {
        globalListenerArray.clear();
        for (HashMap<Integer, ArrayList<MessageListener>> channelMap : messageListenerMap.values()) {
            // Java 8 fun
            //channelMap.values().stream().filter(listenerArray -> listenerArray != null).forEach(ArrayList::clear);
            for (ArrayList<MessageListener> listenerArray : channelMap.values()) {
                if (listenerArray != null) {
                    listenerArray.clear();
                }
            }
        }
    }


    /**
     * Generate the SerialPort object using the SerialPort adapter class provided in the FirmataConfiguration
     * If there is any issue constructing this object, toss a RuntimeException, as this is a developer error in
     * implementing the SerialPort adapter for this library.
     */
    private void createSerialPort() {
        Constructor<? extends SerialPort> constructor;

        try {
            constructor = configuration.getSerialPortAdapterClass().getDeclaredConstructor(
                    String.class, Integer.class,
                    SerialPortDataBits.class, SerialPortStopBits.class, SerialPortParity.class);
        } catch (NoSuchMethodException e) {
            log.error("Unable to construct SerialPort object. Programming error. Your class adapter must support " +
                    "a constructor with input args of" +
                    "YourSerialPort(String.class, Integer.class, SerialPortDataBits.class, " +
                    "SerialPortStopBits.class, SerialPortParity.class);");
            e.printStackTrace();
            throw new RuntimeException("Cannot construct SerialPort adapter!");
        }

        try {
            serialPort = constructor.newInstance(
                    configuration.getSerialPortID(),
                    configuration.getSerialPortBaudRate(),
                    configuration.getSerialPortDataBits(),
                    configuration.getSerialPortStopBits(),
                    configuration.getSerialPortParity());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            log.error("Unable to construct SerialPort object. Programming error. Instantiation error. {}",
                    e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Cannot construct SerialPort adapter!");
        }
    }

    /**
     * Disconnect from the SerialPort object that we are communicating with over the Firmata protocol.
     *
     * @return True if the SerialPort was closed. False if the port failed to close.
     */
    private Boolean removeSerialPort() {
        Boolean ret = true;

        if (serialPort != null) {
            ret = serialPort.disconnect();
            serialPort = null;
        }

        return ret;
    }

    /**
     * Test if Firmata Protocol communication is possible over the serial port. Done by sending a ProtocolVersion
     * request to the device. If the device supports Firmata, it should reply with a ProtocolVersionMessage.
     * Listen for this message while sending the request, and if it is not received within 5 seconds, consider
     * the device unsupported.
     *
     * @return true if communications were successful. False if there was a timeout or the data could not be
     * interpreted within 5 seconds.
     */
    synchronized private Boolean testProtocolCommunication() {
        Boolean protocolTestPassed = true;
        addMessageListener(versionListener);
        try {
            serialPort.getOutputStream().write(new ProtocolVersionQueryMessage().toByteArray());
        } catch (IOException e) {
            log.error("Unable to test protocol communications. Serial port error.");
            return false;
        }
        synchronized (protocolVersionLock) {
            try {
                protocolVersionLock.wait(5000);
            } catch (InterruptedException e) {
                log.error("Timed out waiting for Firmata protocol response. Device may not be supported " +
                "or may not be communicating over the Firmata protocol.");
                protocolTestPassed = false;
            }
        }
        removeMessageListener(versionListener);
        return protocolTestPassed;
    }

    /**
     * Handle events from the SerialPort object. When DATA_AVAILABLE is sent, handleDataAvailable() and build a message
     * object that can be passed up to the client for interpretation and handling.
     * Note: Currently only handling DATA_AVAILABLE.
     *
     * @param event SerialPortEvent indicating the type of event that was raised from the SerialPort object.
     */
    @Override
    public void serialEvent(SerialPortEvent event) {
        if (event.getEventType() == SerialPortEventTypes.DATA_AVAILABLE) {
            handleDataAvailable();
        }
    }

    /**
     * Handles the SerialPort input stream and builds a message object if a detected CommandByte is discovered
     * over the communications stream.
     */
    private void handleDataAvailable() {
        InputStream inputStream = serialPort.getInputStream();
        try {
            while (inputStream.available() > 0) {
                byte inputByte = (byte) inputStream.read();

                if (inputByte == -1) {
                    log.error("Reached end of stream trying to read serial port.");
                    stop();
                    return;
                }

                Message message = CommandParser.handleByte(inputByte, inputStream);

                if (message != null) {
                    log.debug("Dispatching message {}", message.getClass().getSimpleName());
                    // Primary listeners first
                    dispatchMessageToPrimaryListeners(message);
                    // Global listeners last
                    dispatchMessageToGlobalListeners(message);
                }
            }
        } catch (IOException e) {
            log.error("IO Error reading from serial port. Closing connection.");
            e.printStackTrace();
            stop();
        }
    }

    /**
     * Dispatch a Message object built from data over the SerialPort communication line to a corresponding
     * MessageListener class designed to handle and interpret the object for processing in the client code.
     * It seems the unchecked warning suppression is necessary, since this is the only part of the
     * design where we need to translate from generics to implementations, and the only objects
     * that know what Message implementation this is are the MessageListener implementations, which
     * are also generic here.
     *
     * @param message Firmata Message to be dispatched to the registered listeners.
     */
    @SuppressWarnings("unchecked")
    private void dispatchMessageToPrimaryListeners(Message message) {
        Class messageClass = message.getClass();
        if (messageListenerMap.containsKey(messageClass)) {
            HashMap<Integer, ArrayList<MessageListener>> listenerMap = messageListenerMap.get(messageClass);
            ArrayList<MessageListener> messageListeners;

            // If the message is a Channel Message, hit the channel based listeners first.
            if (message instanceof ChannelMessage) {
                messageListeners = listenerMap.get(((ChannelMessage) message).getChannelInt());
                if (messageListeners != null) {
                    for (MessageListener listener : messageListeners) {
                        listener.messageReceived(message);
                    }
                }
            }

            // Then send the message to any listeners registered to all channels
            //   Or to listeners for messages that do not support channels.
            messageListeners = listenerMap.get(null);
            if (messageListeners != null) {
                for (MessageListener listener : messageListeners) {
                    listener.messageReceived(message);
                }
            }
        }
    }

    /**
     * Dispatch a Message object to the list of global message listeners.
     * @param message Firmata Message to be dispatched to the registered listeners.
     */
    private void dispatchMessageToGlobalListeners(Message message) {
        for (GlobalMessageListener listener : globalListenerArray) {
            listener.messageReceived(message);
        }
    }
}
