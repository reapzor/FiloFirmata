package com.bortbort.arduino.FiloFirmata.Listeners;

import com.bortbort.arduino.FiloFirmata.Messages.SysexStringDataMessage;

/**
 * Sysex String Data Message Listener.
 * Abstract class definition for a listener that can be registered to fire when a SysexStringDataMessage is
 * received over the SerialPort.
 */
public abstract class SysexStringDataListener extends MessageListener<SysexStringDataMessage> {

    public SysexStringDataListener() {
        super(SysexStringDataMessage.class);
    }

}
