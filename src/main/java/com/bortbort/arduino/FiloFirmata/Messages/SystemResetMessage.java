package com.bortbort.arduino.FiloFirmata.Messages;

import com.bortbort.arduino.FiloFirmata.Parser.CommandBytes;

import java.io.ByteArrayOutputStream;

/**
 * Created by chuck on 1/9/2016.
 */
public class SystemResetMessage extends TransmittableMessage {

    public SystemResetMessage() {
        super(CommandBytes.SYSTEM_RESET);
    }

    @Override
    protected Boolean serialize(ByteArrayOutputStream outputStream) {
        return true;
    }
}
