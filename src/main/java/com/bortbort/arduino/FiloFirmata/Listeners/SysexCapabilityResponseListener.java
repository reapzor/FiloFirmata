package com.bortbort.arduino.FiloFirmata.Listeners;

import com.bortbort.arduino.FiloFirmata.Messages.SysexCapabilityResponseMessage;

/**
 * Sysex Capability Response Message Listener.
 * Abstract class definition for a listener that can be registered to fire when a SysexCapabilityResponseMessage is
 * received over the SerialPort.
 */
public abstract class SysexCapabilityResponseListener extends MessageListener<SysexCapabilityResponseMessage> {

    public SysexCapabilityResponseListener() {
        super(SysexCapabilityResponseMessage.class);
    }

}

