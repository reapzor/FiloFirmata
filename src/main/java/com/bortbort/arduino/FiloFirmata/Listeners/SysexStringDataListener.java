package com.bortbort.arduino.FiloFirmata.Listeners;

import com.bortbort.arduino.FiloFirmata.Messages.SysexStringDataMessage;

/**
 * Created by chuck on 1/9/2016.
 */
public abstract class SysexStringDataListener extends MessageListener<SysexStringDataMessage> {

    public SysexStringDataListener() {
        super(SysexStringDataMessage.class);
    }

}
