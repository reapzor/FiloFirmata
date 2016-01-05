package com.bortbort.arduino.FiloFirmata.PortAdapters;


/**
 * Created by chuck on 1/3/2016.
 */
public class SerialPortEvent {
    private SerialPortEventTypes eventType;

    public SerialPortEvent(SerialPortEventTypes eventType) {
        this.eventType = eventType;
    }

    public SerialPortEventTypes getEventType() {
        return eventType;
    }
}
