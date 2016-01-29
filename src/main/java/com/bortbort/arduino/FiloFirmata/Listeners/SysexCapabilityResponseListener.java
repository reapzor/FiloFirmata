package com.bortbort.arduino.FiloFirmata.Listeners;

import com.bortbort.arduino.FiloFirmata.Messages.SysexCapabilityMessage;

/**
 * Sysex Capability Response Message Listener.
 * Abstract class definition for a listener that can be registered to fire when a SysexCapabilityMessage is
 * received over the SerialPort.
 */
public abstract class SysexCapabilityResponseListener extends MessageListener<SysexCapabilityMessage> {

    public SysexCapabilityResponseListener() {
        super(SysexCapabilityMessage.class);
    }

}

