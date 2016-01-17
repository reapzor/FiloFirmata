package com.bortbort.arduino.FiloFirmata.Messages;

import com.bortbort.arduino.FiloFirmata.Parser.CommandBytes;

import java.io.ByteArrayOutputStream;

/**
 * Created by chuck on 1/9/2016.
 */
public class ProtocolVersionMessage extends TransmittableMessage {
    Integer majorVersion;
    Integer minorVersion;

    public ProtocolVersionMessage(Integer majorVersion, Integer minorVersion) {
        super(CommandBytes.PROTOCOL_VERSION);
        this.majorVersion = majorVersion;
        this.minorVersion = minorVersion;
    }

    public Integer getMajorVersion() {
        return majorVersion;
    }

    public Integer getMinorVersion() {
        return minorVersion;
    }

    @Override
    protected Boolean serialize(ByteArrayOutputStream outputStream) {
        return true;
    }

}
