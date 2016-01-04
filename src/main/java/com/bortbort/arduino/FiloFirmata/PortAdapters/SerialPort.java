package com.bortbort.arduino.FiloFirmata.PortAdapters;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by chuck on 1/3/2016.
 */
public abstract class SerialPort {
    protected static final Logger log = LoggerFactory.getLogger(SerialPort.class);
    private String portID;
    private Integer baudRate;
    private ArrayList<SerialPortEventListener> eventListeners = new ArrayList<>();
    private PipedOutputStream outputStream;
    private InputStream inputStream;
    private Boolean connected = false;


    public SerialPort(String portID, Integer baudRate) {
        this.portID = portID;
        this.baudRate = baudRate;
    }

    public Boolean connect() {
        if (connected) {
            return true;
        }

        inputStream = new PipedInputStream();
        try {
            outputStream = new PipedOutputStream((PipedInputStream) inputStream);
        } catch (IOException e) {
            log.error("Unable to generate output stream for SerialPort adapter!");
            e.printStackTrace();
            disconnect();
            return false;
        }

        if (!openPort()) {
            disconnect();
            return false;
        }

        connected = true;
        return true;
    }

    public Boolean disconnect() {
        if (!connected) {
            return true;
        }

        Boolean ret = closePort();

        try {
            outputStream.close();
        } catch (IOException e) {
            log.error("Unable to close outputstream!");
            e.printStackTrace();
            ret = false;
        }
        try {
            inputStream.close();
        } catch (IOException e) {
            log.error("Unable to close inputstream!");
            e.printStackTrace();
            ret = false;
        }

        connected = ret;
        return ret;
    }

    protected abstract Boolean openPort();
    protected abstract Boolean closePort();

    public void addEventListener(SerialPortEventListener eventListener) {
        if (!eventListeners.contains(eventListener)) {
            eventListeners.add(eventListener);
        }
    }

    public void removeEventListener(SerialPortEventListener eventListener) {
        eventListeners.remove(eventListener);
    }

    public void fireEvent(SerialPortEventTypes eventType) {
        for (SerialPortEventListener listener : eventListeners) {
            listener.serialEvent(new SerialPortEvent(eventType, inputStream));
        }
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    protected PipedOutputStream getOutputStream() {
        return  outputStream;
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
}
