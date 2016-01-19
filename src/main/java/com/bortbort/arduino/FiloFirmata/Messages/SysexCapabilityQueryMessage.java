package com.bortbort.arduino.FiloFirmata.Messages;

import com.bortbort.arduino.FiloFirmata.Parser.SysexCommandBytes;

import java.io.ByteArrayOutputStream;

/**
 * Sysex Capability Query Message
 * Asks the Firmata device to respond with a list of capable modes that a pin can support.
 */
public class SysexCapabilityQueryMessage extends TransmittableSysexMessage {

    public SysexCapabilityQueryMessage() {
        super(SysexCommandBytes.CAPABILITY_QUERY);
    }

    @Override
    protected Boolean serialize(ByteArrayOutputStream outputStream) {
        return true;
    }

}
