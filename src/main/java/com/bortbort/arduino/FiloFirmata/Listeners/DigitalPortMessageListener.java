package com.bortbort.arduino.FiloFirmata.Listeners;

import com.bortbort.arduino.FiloFirmata.Messages.DigitalPortMessage;

/**
 * Created by chuck on 1/12/2016.
 */
public abstract class DigitalPortMessageListener extends MessageListener<DigitalPortMessage> {

    public DigitalPortMessageListener() {
        super(DigitalPortMessage.class);
    }


}
