package com.bortbort.arduino.FiloFirmata.Parser;

import com.bortbort.arduino.FiloFirmata.Firmata;
import com.bortbort.arduino.FiloFirmata.PortAdapters.SerialPortEvent;
import com.bortbort.arduino.FiloFirmata.PortAdapters.SerialPortEventListener;
import com.bortbort.arduino.FiloFirmata.PortAdapters.SerialPortEventTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by chuck on 1/5/2016.
 */
public class FirmataDataHandler extends SerialPortEventListener {
    private static final Logger log = LoggerFactory.getLogger(FirmataDataHandler.class);
    private Firmata firmata;


    public FirmataDataHandler(Firmata firmata) {
        this.firmata = firmata;
    }


    @Override
    public void serialEvent(SerialPortEvent event) {
        if (event.getEventType() == SerialPortEventTypes.DATA_AVAILABLE) {
            handleDataAvailable();
        }
    }

    private void handleDataAvailable() {
        InputStream inputStream = firmata.getSerialPort().getInputStream();
        try {
            while (inputStream.available() > 0) {
                byte inputByte = (byte) inputStream.read();
                MessageRouter.handleByte(inputByte, inputStream);
            }
        } catch (IOException e) {
            log.error("IO Error reading from serial port. Closing connection.");
            e.printStackTrace();
            firmata.stop();
        }
    }
}
