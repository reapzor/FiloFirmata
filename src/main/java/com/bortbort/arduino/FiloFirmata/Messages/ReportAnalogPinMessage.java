package com.bortbort.arduino.FiloFirmata.Messages;

import com.bortbort.arduino.FiloFirmata.Parser.CommandBytes;

import java.io.ByteArrayOutputStream;

/**
 * Report Analog Channel Message
 * Asks the Firmata Device to start reporting Analog values for the given channel (pin)
 */
public class ReportAnalogPinMessage extends TransmittableChannelMessage {
    Boolean enableReporting;

    /**
     * Report Analog Channel Message
     * @param pinIdentifier int Index value for Firmata pin.
     * @param enableReporting Boolean value to enable or disable reporting. (True = enable)
     */
    public ReportAnalogPinMessage(int pinIdentifier, Boolean enableReporting) {
        super(CommandBytes.REPORT_ANALOG_PIN, pinIdentifier);
        this.enableReporting = enableReporting;
    }


    @Override
    protected Boolean serialize(ByteArrayOutputStream outputStream) {
        // Output logic 1 or 0 depending on Boolean evaluation.
        outputStream.write((byte) (enableReporting ? 0x01 : 0x00));
        return true;
    }

}
