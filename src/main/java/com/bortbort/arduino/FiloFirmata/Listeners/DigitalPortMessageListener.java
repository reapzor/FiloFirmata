package com.bortbort.arduino.FiloFirmata.Listeners;

import com.bortbort.arduino.FiloFirmata.Messages.DigitalPortMessage;

/**
 * Digital Port Message Listener.
 * Abstract class definition for a listener that can be registered to fire when a DigitalPortMessage is
 * received over the SerialPort.
 */
public abstract class DigitalPortMessageListener extends MessageListener<DigitalPortMessage> {

    public DigitalPortMessageListener() {
        super(DigitalPortMessage.class);
    }


}
