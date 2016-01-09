package com.bortbort.arduino.FiloFirmata;

import com.bortbort.arduino.FiloFirmata.Messages.Message;
import com.bortbort.arduino.FiloFirmata.Parser.CommandParser;
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
class SerialDataHandler extends SerialPortEventListener {
    private static final Logger log = LoggerFactory.getLogger(SerialDataHandler.class);
    private Firmata firmata;


    public SerialDataHandler(Firmata firmata) {
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
                Message message = CommandParser.handleByte(inputByte, inputStream);
                if (message != null) {
                    log.info("Dispatching message {}", message.getClass().getName());
                    firmata.getMessageDispatcher().dispatchMessage(message);
                }
            }
        } catch (IOException e) {
            log.error("IO Error reading from serial port. Closing connection.");
            e.printStackTrace();
            firmata.stop();
        }
    }
}
