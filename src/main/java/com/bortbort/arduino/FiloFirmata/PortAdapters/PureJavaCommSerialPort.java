package com.bortbort.arduino.FiloFirmata.PortAdapters;

import purejavacomm.*;
import purejavacomm.SerialPortEvent;
import purejavacomm.SerialPortEventListener;
import java.io.IOException;
import java.util.TooManyListenersException;

/**
 * Created by chuck on 1/3/2016.
 */
public class PureJavaCommSerialPort extends SerialPort implements SerialPortEventListener {
    private PureJavaSerialPort serialPort = null;
    private CommPortIdentifier commPortIdentifier = null;

    // Test constructor
    public PureJavaCommSerialPort(String portID, Integer baudRate) {
        this(portID, baudRate, SerialPortDataBits.DATABITS_8, SerialPortStopBits.STOPBITS_1,
                SerialPortParity.PARITY_NONE);
    }

    public PureJavaCommSerialPort(String portID, Integer baudRate, SerialPortDataBits dataBits,
                                  SerialPortStopBits stopBits, SerialPortParity parity) {
        // Do not use piped streams since this library uses input and output streams already
        // The port mappings in the adapter directly match the PureJava implementation. No need to translate.
        super(portID, baudRate, dataBits, stopBits, parity, false, false);
    }

    @Override
    protected Boolean openPort() {
        if (commPortIdentifier == null) {
            try {
                commPortIdentifier = CommPortIdentifier.getPortIdentifier(getPortID());
            } catch (NoSuchPortException e) {
                log.error("Communications port {} not found or connected. {}", getPortID(), e.getMessage());
                return false;
            }
        }

        if (commPortIdentifier.isCurrentlyOwned()) {
            log.error("Communications port {} is currently in use by {}. Trying to open anyway.",
                    getPortID(), commPortIdentifier.getCurrentOwner());
        }

        log.info("Connecting to port {} at baudrate {} with databits setting {}, stopbits setting {}, and parity " +
        "setting {}. Timeout: 2s", getPortID(), getBaudRate(), getDataBits(), getStopBits(), getParity());

        try {
            serialPort = (PureJavaSerialPort) commPortIdentifier.open(
                    PureJavaCommSerialPort.class.getName(),
                    2000);
        } catch (PortInUseException e) {
            log.error("Cannot open communications port {}. Cannot obtain ownership!", getPortID());
            return false;
        }

        try {
            serialPort.setSerialPortParams(
                    getBaudRate(),
                    getDataBits().getDataBitsInt(),
                    getStopBits().getStopBitsInt(),
                    getParity().getParityInt());
        } catch (UnsupportedCommOperationException e) {
            log.error("Unable to configure communications port {} to baud rate {}.", getPortID(), getBaudRate());
            return false;
        }

        serialPort.notifyOnDataAvailable(true);
        serialPort.notifyOnPortClosed(true);
        serialPort.notifyOnOutputEmpty(true);

        // Adding the event listener is where this serial port really starts churning, so make sure it is the last
        //   thing we do, ensuring all configuration is done beforehand.
        try {
            serialPort.addEventListener(this);
        } catch (TooManyListenersException e) {
            log.warn("Already listening to serial port! {}", e.getMessage());
        }

        try {
            setInputStream(serialPort.getInputStream());
        } catch (IOException e) {
            log.error("Unable to get input stream from port {}. {}", getPortID(), e.getMessage());
            return false;
        }

        try {
            setOutputStream(serialPort.getOutputStream());
        } catch (IOException e) {
            log.error("Unable to get output stream to port {}. {}", getPortID(), e.getMessage());
            return false;
        }

        return true;
    }

    @Override
    protected Boolean closePort() {
        if (serialPort != null) {
            log.info("Disconnecting from port {}.", getPortID());
            serialPort.close();
            serialPort.removeEventListener();
            serialPort = null;
        }

        return true;
    }

    @Override
    public void serialEvent(SerialPortEvent event) {
        switch (event.getEventType()) {
            case SerialPortEvent.DATA_AVAILABLE:
                fireEvent(SerialPortEventTypes.DATA_AVAILABLE);
                break;
            case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
                fireEvent(SerialPortEventTypes.OUTPUT_BUFFER_EMPTY);
                break;
            case SerialPortEvent.PORT_CLOSED:
                fireEvent(SerialPortEventTypes.PORT_CLOSED);
                break;
            default:
                log.error("Unrecognized event {}! {} {}",
                        event.getEventType(), event.getOldValue(), event.getNewValue());
                break;
        }
    }


}
