package com.bortbort.arduino.FiloFirmata.Listeners;

import com.bortbort.arduino.FiloFirmata.Messages.ProtocolVersionMessage;

/**
 * Created by chuck on 1/9/2016.
 */
public abstract class ProtocolVersionListener extends MessageListener<ProtocolVersionMessage> {

    public ProtocolVersionListener() {
        super(ProtocolVersionMessage.class);
    }

    @Override
    public abstract void messageReceived(ProtocolVersionMessage message);
}
