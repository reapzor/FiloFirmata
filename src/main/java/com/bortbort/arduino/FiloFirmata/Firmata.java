package com.bortbort.arduino.FiloFirmata;

import com.bortbort.arduino.FiloFirmata.Messages.*;
import com.bortbort.arduino.FiloFirmata.Parser.CommandParser;
import com.bortbort.arduino.FiloFirmata.Parser.MessageBuilder;
import com.bortbort.arduino.FiloFirmata.Parser.SysexCommandParser;
import com.bortbort.arduino.FiloFirmata.Parser.SysexMessageBuilder;
import com.bortbort.arduino.FiloFirmata.PortAdapters.*;
import com.bortbort.helpers.DataTypeHelpers;
import net.jodah.typetools.TypeResolver;
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
     * Add a messageListener to the Firmta object which will fire whenever a matching message is received
     * over the SerialPort that corresponds to the given channel. If the channel is null, the listener
     * will fire regardless of which channel (pin) the message is coming from.
     *
     * @param channel Integer indicating the specific channel or pin to listen on, or null to listen to
     *                all messages regardless of channel/pin.
     * @param messageClass Class indicating the message type to listen to.
     * @param messageListener (Generic)MessageListener object to handle a received Message event over the SerialPort.
     */
    public void addMessageListener(Integer channel, Class<? extends Message> messageClass,
                                   MessageListener messageListener) {
        // Build up the empty class listener map if not already there
        if (!messageListenerMap.containsKey(messageClass)) {
            messageListenerMap.put(messageClass, new HashMap<>());
        }

        // Get the message listener map for the given class
        HashMap<Integer, ArrayList<MessageListener>> listenerMap = messageListenerMap.get(messageClass);

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
     * @param messageClass Class indicating the message type to listen to.
     * @param messageListener (Generic)MessageListener to be removed.
     */
    public void removeMessageListener(Integer channel, Class<? extends Message> messageClass,
                                      MessageListener messageListener) {
        if (messageListenerMap.containsKey(messageClass)) {
            HashMap<Integer, ArrayList<MessageListener>> listenerMap = messageListenerMap.get(messageClass);
            if (listenerMap.containsKey(channel)) {
                ArrayList<MessageListener> messageListeners = listenerMap.get(channel);
                messageListeners.remove(messageListener);
            }
        }
    }

    /**
     * Add a generic message listener to listen for a specific type of message. Useful if you want to combine
     * several or more message handlers into one bucket.
     * @param messageClass Message class to listen to from the project board.
     * @param messageListener Listener that will fire whenever the message type is received.
     */
    public void addMessageListener(Class<? extends Message> messageClass, MessageListener<Message> messageListener) {
        addMessageListener(messageListener.getChannelIdentifier(), messageClass, messageListener);
    }

    /**
     * Remove a generic message listener from listening to a specific type of message.
     * @param messageClass Message class to stop listening to.
     * @param messageListener Listener to remove from the listener list for the given messageClass.
     */
    public void removeMessageListener(Class<? extends Message> messageClass,
                                      MessageListener<Message> messageListener) {
        removeMessageListener(messageListener.getChannelIdentifier(), messageClass, messageListener);
    }

    /**
     * Add a messageListener to the Firmta object which will fire whenever a matching message is received
     * over the SerialPort that corresponds to the given channel.
     *
     * @param channel Integer indicating the specific channel or pin to listen on
     * @param messageListener MessageListener object to handle a received Message event over the SerialPort.
     */
    public void addMessageListener(Integer channel, MessageListener<? extends Message> messageListener) {
        addMessageListener(channel, messageListener.getMessageType(), messageListener);
    }

    /**
     * Remove a messageListener from the Firmata object which will stop the listener from responding to message
     * received events over the SerialPort.
     *
     * @param channel Integer channel to remove the listener from.
     * @param messageListener MessageListener to be removed.
     */
    public void removeMessageListener(Integer channel, MessageListener<? extends Message> messageListener) {
        removeMessageListener(channel, messageListener.getMessageType(), messageListener);
    }

    /**
     * Add a messageListener to the Firmata object which will fire whenever a matching message is received
     * over the SerialPort that corresponds to the given DigitalChannel.
     *
     * @param channel DigitalChannel to listen on
     * @param messageListener MessageListener object to handle a received Message event over the SerialPort.
     */
    public void addMessageListener(DigitalChannel channel, MessageListener<? extends Message> messageListener) {
        addMessageListener(channel.getIdentifier(), messageListener.getMessageType(), messageListener);
    }

    /**
     * Remove a messageListener from the Firmata object which will stop the listener from responding to message
     * received events over the SerialPort.
     *
     * @param channel DigitalChannel to remove the listener from.
     * @param messageListener MessageListener to be removed.
     */
    public void removeMessageListener(DigitalChannel channel, MessageListener<? extends Message> messageListener) {
        removeMessageListener(channel.getIdentifier(), messageListener.getMessageType(), messageListener);
    }

    /**
     * Add a messageListener to the Firmta object which will fire whenever a matching message is received
     * over the SerialPort. If the listener is for a ChannelMessage, the listener will fire regardless of which
     * channel (pin) the message is coming from.
     *
     * @param messageListener MessageListener object to handle a received Message event over the SerialPort.
     */
    public void addMessageListener(MessageListener<? extends Message> messageListener) {
        addMessageListener(messageListener.getChannelIdentifier(),
                messageListener.getMessageType(), messageListener);
    }

    /**
     * Remove a messageListener from the Firmata object which will stop the listener from responding to message
     * received events over the SerialPort.
     *
     * @param messageListener MessageListener object to remove.
     */
    public void removeMessageListener(MessageListener<? extends Message> messageListener) {
        removeMessageListener(messageListener.getChannelIdentifier(),
                messageListener.getMessageType(), messageListener);
    }



    /**
     * Send a Message over the serial port to a Firmata supported device.
     *
     * @param message TransmittableMessage object used to translate a series of bytes to the SerialPort.
     * @return True if the Message sent. False if the Message was not sent.
     */
    public synchronized Boolean sendMessage(TransmittableMessage message) {
        if (!start()) {
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
     * Send a Message over the serial port and block/wait for the message response.
     * In cases where you do not need ASync behavior, or just want to send a single message and
     * handle a single response, where the async design may be a bit too verbose, use this instead.
     * By sending a message and dictacting the type of response, you will get the response message as the return
     * value, or null if the message was not received within the timeout blocking period.
     * @param message TransmittableMessage to be sent over the serial port
     * @param <T> Message type that you are expecting as a reply to the message being transmitted.
     * @return T message object if the project board sends a reply. null if no reply was sent in time.
     */
    public <T extends Message> T sendMessageSynchronous(Class<T> responseType, TransmittableMessage message) {
        T responseMessage = null;
        SynchronousMessageListener messageListener = new SynchronousMessageListener(responseType);

        addMessageListener(messageListener);

        if (sendMessage(message)) {
            if (messageListener.waitForResponse()) {
                responseMessage = responseType.cast(messageListener.getResponseMessage());
            }
        }

        removeMessageListener(messageListener);

        return responseMessage;
    }

    /**
     * Send a series of raw bytes over the serial port to a Firmata supported device.
     *
     * @param rawBytes byte array to be sent over the SerialPort OutputStream.
     * @return True if the bytes were sent. False if the bytes were not sent.
     */
    public synchronized Boolean sendRaw(byte... rawBytes) {
        if (!start()) {
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
    private Boolean testProtocolCommunication() {
        SynchronousMessageListener versionListener = new SynchronousMessageListener(ProtocolVersionMessage.class);

        addMessageListener(versionListener);

        try {
            serialPort.getOutputStream().write(new ProtocolVersionQueryMessage().toByteArray());
        } catch (IOException e) {
            log.error("Unable to test protocol communications. Serial port error.");
            removeMessageListener(versionListener);
            return false;
        }

        ProtocolVersionMessage message = null;
        if (versionListener.waitForResponse()) {
            message = ProtocolVersionMessage.class.cast(versionListener.getResponseMessage());
        }

        removeMessageListener(versionListener);

        if (message != null) {
            log.info("Detected Firmata device protocol version: {}.{}",
                    message.getMajorVersion(), message.getMinorVersion());
            if (message.getMajorVersion() < MIN_SUPPORTED_VERSION_MAJOR ||
                    (message.getMinorVersion() < MIN_SUPPORTED_VERSION_MINOR &&
                            message.getMajorVersion() >= MIN_SUPPORTED_VERSION_MAJOR)) {
                log.warn("Firmata device protocol version is too old for this library." +
                                " Some functionality may not work. Library min supported version: {}.{}",
                        MIN_SUPPORTED_VERSION_MAJOR, MIN_SUPPORTED_VERSION_MINOR);
            }
            return true;
        }

        return false;
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
                    log.debug("Routing message {}", message.getClass().getSimpleName());
                    routeMessage(message);
                }
            }
        } catch (IOException e) {
            log.error("IO Error reading from serial port. Closing connection.");
            e.printStackTrace();
            stop();
        }
    }

    /**
     * Route a Message object built from data over the SerialPort communication line to a corresponding
     * MessageListener array designed to handle and interpret the object for processing in the client code.
     *
     * @param message Firmata Message to be routed to the registered listeners.
     */
    private void routeMessage(Message message) {
        Class messageClass = message.getClass();

        // Dispatch message to all specific listeners for the message class type
        if (messageListenerMap.containsKey(messageClass)) {
            dispatchMessage(messageListenerMap.get(messageClass), message);
        }

        // Dispatch message to all generic listeners
        if (messageListenerMap.containsKey(Message.class)) {
            dispatchMessage(messageListenerMap.get(Message.class), message);
        }
    }

    /**
     * Dispatch a message to the corresponding listener arrays in the listener map
     * @param listenerMap HashMap of listener arrays to dispatch the message to
     * @param message Message to be dispatched to the listeners
     */
    @SuppressWarnings("unchecked")
    private void dispatchMessage(HashMap<Integer, ArrayList<MessageListener>> listenerMap, Message message) {
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
        //   or to listeners for messages that do not support channels.
        messageListeners = listenerMap.get(null);
        if (messageListeners != null) {
            for (MessageListener listener : messageListeners) {
                listener.messageReceived(message);
            }
        }
    }

    /**
     * Inform if the firmata serial connection is established and running
     * @return True if connected, false if not.
     */
    public Boolean getStarted() {
        return started;
    }
}
