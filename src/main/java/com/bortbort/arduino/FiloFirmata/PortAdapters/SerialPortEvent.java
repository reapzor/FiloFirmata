package com.bortbort.arduino.FiloFirmata.PortAdapters;

import java.io.InputStream;

/**
 * Created by chuck on 1/3/2016.
 */
public class SerialPortEvent {
    private SerialPortEventTypes eventType;
    private InputStream inputStream;

    public SerialPortEvent(SerialPortEventTypes eventType, InputStream inputStream) {
        this.eventType = eventType;
        this.inputStream = inputStream;
    }

    public SerialPortEventTypes getEventType() {
        return eventType;
    }

    public InputStream getInputStream() {
        return inputStream;
    }
}
