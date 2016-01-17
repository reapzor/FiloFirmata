package com.bortbort.arduino.FiloFirmata.Messages;

import com.bortbort.arduino.FiloFirmata.Parser.CommandBytes;

import java.io.ByteArrayOutputStream;

/**
 * Created by chuck on 1/16/2016.
 */
public class ReportAnalogPinMessage extends TransmittableChannelMessage {
    Boolean enableReporting;

    public ReportAnalogPinMessage(int pinIdentifier, Boolean enableReporting) {
        super(CommandBytes.REPORT_ANALOG_PIN, pinIdentifier);
        this.enableReporting = enableReporting;
    }

    @Override
    protected Boolean serialize(ByteArrayOutputStream outputStream) {
        outputStream.write((byte) (enableReporting ? 0x01 : 0x00));
        return true;
    }

}
