package com.bortbort.arduino.FiloFirmata.PortAdapters;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by chuck on 1/3/2016.
 */
public abstract class SerialPort {
    private ExecutorService executor;
    protected static final Logger log = LoggerFactory.getLogger(SerialPort.class);
    private String portID;
    private Integer baudRate;
    private ArrayList<SerialPortEventListener> eventListeners = new ArrayList<>();
    private InputStream inputStream;
    private OutputStream outputStream;
    private Boolean connected = false;
    private SerialPortDataBits dataBits;
    private SerialPortStopBits stopBits;
    private SerialPortParity parity;
    // Used by adapter implementations that do not support streams;
    //private PipedOutputStream adapterOutputStream;
    //private PipedInputStream adapterInputStream;
    //private AdapterInputStreamListener adapterInputStreamListener;
    private Boolean useAdapterOutputStream;
    private Boolean useAdapterInputStream;


    public SerialPort(String portID, Integer baudRate, SerialPortDataBits dataBits,
                      SerialPortStopBits stopBits, SerialPortParity parity) {
        this(portID, baudRate, dataBits, stopBits, parity, false, false);
    }

    public SerialPort(String portID, Integer baudRate, SerialPortDataBits dataBits, SerialPortStopBits stopBits,
                      SerialPortParity parity, Boolean useAdapterOutputStream, Boolean useAdapterInputStream) {
        this.portID = portID;
        this.baudRate = baudRate;
        this.dataBits = dataBits;
        this.stopBits = stopBits;
        this.parity = parity;
        this.useAdapterOutputStream = useAdapterOutputStream;
        this.useAdapterInputStream = useAdapterInputStream;
    }


    // Set the input stream the client will read from
    protected void setInputStream(InputStream inputStream) {
        if (useAdapterOutputStream) {
            throw new RuntimeException("Cannot set input stream while useAdapterOutputStream is enabled." +
            " Use the other constructor to turn off piped streams if you are supplying your own.");
        }
        this.inputStream = inputStream;
    }

    // Set the output stream the client will write to
    protected void setOutputStream(OutputStream outputStream) {
        if (useAdapterInputStream) {
            throw new RuntimeException("Cannot set input stream while useAdapterInputStream is enabled." +
                    " Use the other constructor to turn off piped streams if you are supplying your own.");
        }
        this.outputStream = outputStream;
    }

    // Tell the client if there is data available, etc.
    protected void fireEvent(SerialPortEventTypes eventType) {
        executor.submit(new Runnable() {
            @Override
            public void run() {
                for (SerialPortEventListener listener : eventListeners) {
                    listener.serialEvent(new SerialPortEvent(eventType));
                }

            }
        });
    }

    // Will only be called once if a connection is established.
    protected abstract Boolean openPort();

    // Could be called multiple times, even if not connected.
    protected abstract Boolean closePort();

    /* <Not yet used>
    // If using a serial library that does not use streams, write the bytes to here, which will pipe to a stream.
    protected PipedOutputStream getAdapterOutputStream() {
        return adapterOutputStream;
    }

    // If using a serial library that does not use streams, read the bytes written from the client here.
    protected PipedInputStream getAdapterInputStream() { return adapterInputStream; }

    // Register an input stream listener to get notified when the user has written bytes to the adapter
    //   Use when adapting a SerialPort library that does not support streams.
    protected void addAdapterInputStreamListener(AdapterInputStreamListener adapterInputStreamListener) {
        if (!useAdapterInputStream) {
            throw new RuntimeException("Do not add an input stream listener if not using adapterInputStream");
        }
        if (outputStream != null) {
            ((EventedAdapterPipedOutputStream) outputStream).addInputStreamListener(adapterInputStreamListener);
        }
        this.adapterInputStreamListener = adapterInputStreamListener;
    }

    // Remove the listener if needed by the adapting library
    protected void removeAdapterInputStreamListener() {
        if (!useAdapterInputStream) {
            throw new RuntimeException("Do not remove an input stream listener if not using adapterInputStream");
        }
        if (outputStream != null && adapterInputStreamListener != null) {
            ((EventedAdapterPipedOutputStream) outputStream).removeInputStreamListener(adapterInputStreamListener);
        }
        this.adapterInputStreamListener = null;
    }
    </Not yet used> */


    /* Client API */

    public Boolean connect(SerialPortEventListener eventListener) {
        addEventListener(eventListener);
        return connect();
    }

    public Boolean disconnect(SerialPortEventListener eventListener) {
        removeEventListener(eventListener);
        return disconnect();
    }

    public synchronized Boolean connect() {
        if (connected) {
            return true;
        }

        executor = Executors.newSingleThreadExecutor();

        /* Uncomment and fix bugs if/when used.
        if (useAdapterOutputStream) {
            inputStream = new PipedInputStream();
            try {
                adapterOutputStream = new PipedOutputStream((PipedInputStream) inputStream);
            } catch (IOException e) {
                log.error("Unable to generate input stream for SerialPort adapter!");
                e.printStackTrace();
                disconnect();
                return false;
            }
        }

        if (useAdapterInputStream) {
            EventedAdapterPipedOutputStream os = new EventedAdapterPipedOutputStream();
            if (adapterInputStreamListener != null) {
                os.addInputStreamListener(adapterInputStreamListener);
            }
            outputStream = os;
            try {
                adapterInputStream = new PipedInputStream((PipedOutputStream) outputStream);
            } catch (IOException e) {
                log.error("Unable to generate output stream for SerialPort adapter!");
                e.printStackTrace();
                disconnect();
                return false;
            }
        }
        */

        if (!openPort()) {
            disconnect();
            return false;
        }

        connected = true;
        return true;
    }

    public Boolean disconnect() {
        Boolean ret = closePort();

        if (executor != null) {
            executor.shutdown();
            executor = null;
        }

        /* Uncomment and fix bugs if/when used.
        if (useAdapterOutputStream) {
            try {
                if (adapterOutputStream != null) {
                    adapterOutputStream.close();
                }
            } catch (IOException e) {
                log.error("Unable to close adapter output stream!");
                e.printStackTrace();
                ret = false;
            }
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                log.error("Unable to close input stream!");
                e.printStackTrace();
                ret = false;
            }
        }
        else {
            inputStream = null;
        }

        if (useAdapterInputStream) {
            try {
                if (adapterInputStream != null) {
                    adapterInputStream.close();
                }
            } catch (IOException e) {
                log.error("Unable to close adapter input stream!");
                e.printStackTrace();
                ret = false;
            }
            try {
                if (outputStream != null) {
                    outputStream.close();
                    if (adapterInputStreamListener != null) {
                        ((EventedAdapterPipedOutputStream) outputStream).removeInputStreamListener(
                                adapterInputStreamListener);
                    }
                }
            } catch (IOException e) {
                log.error("Unable to close output stream!");
                e.printStackTrace();
                ret = false;
            }
        }
        else {
            outputStream = null;
        }
        */

        inputStream = null;
        outputStream = null;

        connected = !ret;
        return ret;
    }

    public void addEventListener(SerialPortEventListener eventListener) {
        if (!eventListeners.contains(eventListener)) {
            eventListeners.add(eventListener);
        }
    }

    public void removeEventListener(SerialPortEventListener eventListener) {
        eventListeners.remove(eventListener);
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public OutputStream getOutputStream() {
        return outputStream;
    }

    public String getPortID() {
        return portID;
    }

    public Integer getBaudRate() {
        return baudRate;
    }

    public Boolean getConnected() {
        return connected;
    }

    public SerialPortDataBits getDataBits() {
        return dataBits;
    }

    public SerialPortStopBits getStopBits() {
        return stopBits;
    }

    public SerialPortParity getParity() {
        return parity;
    }
}
