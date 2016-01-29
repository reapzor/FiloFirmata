package com.bortbort.arduino.FiloFirmata.Messages;

import com.bortbort.arduino.FiloFirmata.Parser.CommandBytes;

import java.io.ByteArrayOutputStream;

/**
 * Request Protocol Version Message
 * Asks the Firmata device to respond with its Firmware protocol version
 */
public class ProtocolVersionQueryMessage extends TransmittableMessage {

    public ProtocolVersionQueryMessage() {
        super(CommandBytes.PROTOCOL_VERSION);
    }

    @Override
    protected Boolean serialize(ByteArrayOutputStream outputStream) {
        return true;
    }
}
