package com.bortbort.arduino.FiloFirmata.Listeners;

import com.bortbort.arduino.FiloFirmata.Messages.AnalogMessage;
import com.bortbort.arduino.FiloFirmata.Messages.ProtocolVersionMessage;

/**
 * Analog Message Listener.
 * Abstract class definition for a listener that can be registered to fire when an AnalogMessage is
 * received over the SerialPort.
 */
public abstract class AnalogMessageListener extends MessageListener<AnalogMessage> {

    public AnalogMessageListener() {
        super(AnalogMessage.class);
    }

}
