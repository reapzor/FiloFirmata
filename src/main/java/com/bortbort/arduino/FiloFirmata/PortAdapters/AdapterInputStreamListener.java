package com.bortbort.arduino.FiloFirmata.PortAdapters;

import java.util.EventListener;

/**
 * Created by chuck on 1/4/2016.
 */
public abstract class AdapterInputStreamListener implements EventListener {
    public abstract void inputStreamAvailable(AdapterInputStreamEvent event);
}
