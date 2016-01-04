package com.bortbort.arduino.FiloFirmata.PortAdapters;


import java.util.EventListener;

/**
 * Created by chuck on 1/3/2016.
 */
public abstract class SerialPortEventListener implements EventListener {
    public abstract void serialEvent(SerialPortEvent event);
}
