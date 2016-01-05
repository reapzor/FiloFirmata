package com.bortbort.arduino.FiloFirmata;

import com.bortbort.arduino.FiloFirmata.PortAdapters.SerialPort;
import com.bortbort.arduino.FiloFirmata.PortAdapters.SerialPortEvent;
import com.bortbort.arduino.FiloFirmata.PortAdapters.SerialPortEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by chuck on 1/4/2016.
 */
public class FirmataSerialPortListener extends SerialPortEventListener {
    private static final Logger log = LoggerFactory.getLogger(FirmataSerialPortListener.class);
    private SerialPort serialPort;

    public FirmataSerialPortListener(SerialPort serialPort) {
        this.serialPort = serialPort;
    }

    @Override
    public void serialEvent(SerialPortEvent event) {
        try {
            log.info("Event: {} {}", event.getEventType(), serialPort.getInputStream().available());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
