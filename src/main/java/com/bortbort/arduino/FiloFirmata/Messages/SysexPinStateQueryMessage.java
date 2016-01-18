package com.bortbort.arduino.FiloFirmata.Messages;

import com.bortbort.arduino.FiloFirmata.Parser.SysexCommandBytes;

import java.io.ByteArrayOutputStream;

/**
 * Created by chuck on 1/14/2016.
 */
public class SysexPinStateQueryMessage extends TransmittableSysexMessage {
    int pinIdentifier;

    public SysexPinStateQueryMessage(Integer pinIdentifier) {
        super(SysexCommandBytes.PIN_STATE_QUERY);
        this.pinIdentifier = pinIdentifier;
    }

    public Integer getPinIdentifier() {
        return pinIdentifier;
    }

    @Override
    protected Boolean serialize(ByteArrayOutputStream outputStream) {
        outputStream.write(pinIdentifier);
        return true;
    }

}