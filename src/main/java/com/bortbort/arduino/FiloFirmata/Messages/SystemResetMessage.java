package com.bortbort.arduino.FiloFirmata.Messages;

import com.bortbort.arduino.FiloFirmata.Parser.CommandBytes;

import java.io.ByteArrayOutputStream;

/**
 * System Reset Message
 * Asks the Firmata device to reboot/reset
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
