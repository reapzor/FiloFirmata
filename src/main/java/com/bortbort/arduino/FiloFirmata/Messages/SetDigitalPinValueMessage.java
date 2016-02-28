package com.bortbort.arduino.FiloFirmata.Messages;

import com.bortbort.arduino.FiloFirmata.DigitalPinValue;
import com.bortbort.arduino.FiloFirmata.Parser.CommandBytes;

import java.io.ByteArrayOutputStream;

/**
 * Set Digital Pin Value Message
 * Asks the Firmata device to set a digital logic level (high/low) on a given pin.
 * Note this does not use the 'Channel/Command byte' design like reporting does. No idea why.
 */
public class SetDigitalPinValueMessage extends TransmittableMessage {
    private int pinIdentifier;
    private DigitalPinValue pinValue;

    /**
     * Set Digital Pin Value Message
     * @param pinIdentifier int Index representing the pin to set the value on.
     * @param pinValue DigitalPinValue logic level high or low to set on the pin.
     */
    public SetDigitalPinValueMessage(int pinIdentifier, DigitalPinValue pinValue) {
        super(CommandBytes.SET_DIGITAL_PIN_VALUE);
        this.pinIdentifier = pinIdentifier;
        this.pinValue = pinValue;
    }

    @Override
    protected Boolean serialize(ByteArrayOutputStream outputStream) {
        // For this message, the channel/pin is its own byte?
        outputStream.write(pinIdentifier);
        // Output logic 1 or 0 depending on high or low.
        outputStream.write(pinValue.getByteValue());
        return true;
    }
}
