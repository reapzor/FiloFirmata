package com.bortbort.arduino.FiloFirmata.Listeners;

import com.bortbort.arduino.FiloFirmata.Messages.SysexPinStateResponseMessage;

/**
 * Created by chuck on 1/14/2016.
 */
public abstract class SysexPinStateResponseListener extends MessageListener<SysexPinStateResponseMessage> {

    public SysexPinStateResponseListener() {
        super(SysexPinStateResponseMessage.class);
    }

}
