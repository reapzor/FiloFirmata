package com.bortbort.arduino.FiloFirmata.Messages;

import com.bortbort.arduino.FiloFirmata.Parser.CommandBytes;

/**
 * Created by chuck on 1/9/2016.
 */
public class SystemResetMessage extends TransmittableMessage {

    public SystemResetMessage() {
        super(CommandBytes.SYSTEM_RESET);
    }

    @Override
    protected byte[] serialize() {
        return null;
    }
}
