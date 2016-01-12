package com.bortbort.arduino.FiloFirmata.Listeners;

import com.bortbort.arduino.FiloFirmata.Messages.AnalogMessage;
import com.bortbort.arduino.FiloFirmata.Messages.ProtocolVersionMessage;

/**
 * Created by chuck on 1/11/2016.
 */
public abstract class AnalogMessageListener extends MessageListener<AnalogMessage> {

    public AnalogMessageListener() {
        super(AnalogMessage.class);
    }

}
