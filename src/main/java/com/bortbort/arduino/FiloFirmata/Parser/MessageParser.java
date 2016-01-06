package com.bortbort.arduino.FiloFirmata.Parser;

import com.bortbort.arduino.FiloFirmata.Firmata;
import com.bortbort.arduino.FiloFirmata.PortAdapters.SerialPortEvent;
import com.bortbort.arduino.FiloFirmata.PortAdapters.SerialPortEventListener;
import com.bortbort.arduino.FiloFirmata.PortAdapters.SerialPortEventTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by chuck on 1/5/2016.
 */
public class MessageParser extends SerialPortEventListener {
    private ExecutorService executor;
    private static final Logger log = LoggerFactory.getLogger(MessageParser.class);
    private Firmata firmata;
    private MessageParserRunnable messageParserRunnable = new MessageParserRunnable();

    private class MessageParserRunnable implements Runnable {
        @Override
        public void run() {
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

    public MessageParser(Firmata firmata) {
        this.firmata = firmata;
    }

    public void start() {
        executor = Executors.newSingleThreadExecutor();
    }

    public void stop() {
        executor.shutdown();
        executor = null;
    }

    @Override
    public void serialEvent(SerialPortEvent event) {
        if (event.getEventType() == SerialPortEventTypes.DATA_AVAILABLE) {
            if (executor != null) {
                executor.submit(messageParserRunnable);
            }
        }
    }
}
