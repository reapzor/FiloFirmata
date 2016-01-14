package com.bortbort.arduino.FiloFirmata.Messages;

import com.bortbort.arduino.FiloFirmata.Parser.SysexCommandBytes;

/**
 * Created by chuck on 1/13/2016.
 */
public class SysexCapabilityQueryMessage extends TransmittableSysexMessage {

    public SysexCapabilityQueryMessage() {
        super(SysexCommandBytes.CAPABILITY_QUERY);
    }

    @Override
    protected byte[] serialize() {
        return null;
    }
}
