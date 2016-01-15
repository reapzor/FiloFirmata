package com.bortbort.arduino.FiloFirmata.Listeners;

import com.bortbort.arduino.FiloFirmata.Messages.SysexCapabilityResponseMessage;

/**
 * Created by chuck on 1/14/2016.
 */
public abstract class SysexCapabilityResponseListener extends MessageListener<SysexCapabilityResponseMessage> {

    public SysexCapabilityResponseListener() {
        super(SysexCapabilityResponseMessage.class);
    }

}

