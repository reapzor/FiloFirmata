package com.bortbort.arduino.FiloFirmata.PortAdapters;

import purejavacomm.*;
import purejavacomm.SerialPortEvent;
import purejavacomm.SerialPortEventListener;

import java.util.TooManyListenersException;

/**
 * Created by chuck on 1/3/2016.
 */
public class PureJavaCommSerialPort extends SerialPort implements SerialPortEventListener {
    private PureJavaSerialPort serialPort = null;
    private CommPortIdentifier commPortIdentifier = null;


    public PureJavaCommSerialPort(String portID, Integer baudRate) {
        super(portID, baudRate);
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
            log.warn("Communications port {} is currently in use by {}. Trying to open anyway.",
                    getPortID(), commPortIdentifier.getCurrentOwner());
        }

        try {
            serialPort = (PureJavaSerialPort) commPortIdentifier.open(
                    PureJavaCommSerialPort.class.getName(),
                    2000);
        } catch (PortInUseException e) {
            log.error("Communications port {} is in use. Cannot obtain ownership.", getPortID());
            return false;
        }

        try {
            serialPort.setSerialPortParams(
                    getBaudRate(),
                    purejavacomm.SerialPort.DATABITS_8,
                    purejavacomm.SerialPort.STOPBITS_1,
                    purejavacomm.SerialPort.PARITY_NONE);
        } catch (UnsupportedCommOperationException e) {
            log.error("Unable to configure communications port {} to baud rate {}", getPortID(), getBaudRate());
            return false;
        }

        serialPort.notifyOnDataAvailable(true);
        serialPort.notifyOnPortClosed(true);
        serialPort.notifyOnOutputEmpty(true);

        // Adding the event listener is where this serial port really opens up, so make sure it is the last
        //   thing we do, ensuring all configuration is done beforehand.
        try {
            serialPort.addEventListener(this);
        } catch (TooManyListenersException e) {
            log.warn("Already listening to serial port! {}", e.getMessage());
        }

        return true;
    }

    @Override
    protected Boolean closePort() {
        Boolean ret = true;

        if (serialPort != null) {
            serialPort.removeEventListener();
            serialPort.close();
        }

        return ret;
    }

    @Override
    public void serialEvent(SerialPortEvent event) {

    }
}
