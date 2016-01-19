package com.bortbort.arduino.FiloFirmata.Messages;

import com.bortbort.arduino.FiloFirmata.Parser.CommandBytes;

import java.io.ByteArrayOutputStream;

/**
 * Report Digital Channel Message
 * Asks the Firmata Device to start reporting Digital values for the given channel (pin)
 */
public class ReportDigitalPortMessage extends TransmittableChannelMessage {
    Boolean enableReporting;

    /**
     * Report Digital Channel Message
     * @param pinIdentifier int Index value for Firmata pin.
     * @param enableReporting Boolean value to enable or disable reporting. (True = enable)
     */
    public ReportDigitalPortMessage(int pinIdentifier, Boolean enableReporting) {
        super(CommandBytes.REPORT_DIGITAL_PIN, pinIdentifier);
        this.enableReporting = enableReporting;
    }

    @Override
    protected Boolean serialize(ByteArrayOutputStream outputStream) {
        // Output logic 1 or 0 depending on Boolean evaluation.
        outputStream.write((byte) (enableReporting ? 0x01 : 0x00));
        return true;
    }

}