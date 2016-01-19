package com.bortbort.arduino.FiloFirmata.Listeners;

import com.bortbort.arduino.FiloFirmata.Messages.ProtocolVersionMessage;

/**
 * Protocol Version Message Listener.
 * Abstract class definition for a listener that can be registered to fire when a ProtocolVersionMessage is
 * received over the SerialPort.
 */
public abstract class ProtocolVersionListener extends MessageListener<ProtocolVersionMessage> {

    public ProtocolVersionListener() {
        super(ProtocolVersionMessage.class);
    }

}
