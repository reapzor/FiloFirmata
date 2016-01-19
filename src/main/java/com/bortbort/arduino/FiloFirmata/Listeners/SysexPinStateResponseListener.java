package com.bortbort.arduino.FiloFirmata.Listeners;

import com.bortbort.arduino.FiloFirmata.Messages.SysexPinStateResponseMessage;

/**
 * Sysex Pin State Response Message Listener.
 * Abstract class definition for a listener that can be registered to fire when a SysexPinStateResponseMessage is
 * received over the SerialPort.
 */
public abstract class SysexPinStateResponseListener extends MessageListener<SysexPinStateResponseMessage> {

    public SysexPinStateResponseListener() {
        super(SysexPinStateResponseMessage.class);
    }

}
