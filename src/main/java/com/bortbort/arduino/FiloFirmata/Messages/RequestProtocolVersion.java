package com.bortbort.arduino.FiloFirmata.Messages;

import com.bortbort.arduino.FiloFirmata.Parser.CommandBytes;

import java.io.ByteArrayOutputStream;

/**
 * Created by chuck on 1/17/2016.
 */
public class RequestProtocolVersion extends TransmittableMessage {

    public RequestProtocolVersion() {
        super(CommandBytes.PROTOCOL_VERSION);
    }

    @Override
    protected Boolean serialize(ByteArrayOutputStream outputStream) {
        return true;
    }
}
