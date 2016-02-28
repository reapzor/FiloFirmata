package com.bortbort.arduino.FiloFirmata.Messages;

import com.bortbort.arduino.FiloFirmata.Parser.CommandBytes;
import com.bortbort.arduino.FiloFirmata.PinCapability;

import java.io.ByteArrayOutputStream;

/**
 * Set Pin Mode Message
 * Asks the Firmata device to set a PinCapability mode onto a given pin. This
 * tells the pin what data it should be handling, if its input or output, should enable the
 * internal pullup line, etc.
 */
public class SetPinModeMessage extends TransmittableMessage {
    int pinIdentifier;
    PinCapability pinMode;

    /**
     * Set Pin Mode Message
     * @param pinIdentifier int Index representing the pin to perform this request on.
     * @param pinMode PinCapability mode to set the Firmata pin to (Input, Output, Analog, etc)
     */
    public SetPinModeMessage(int pinIdentifier, PinCapability pinMode) {
        super(CommandBytes.SET_PIN_MODE);
        this.pinIdentifier = pinIdentifier;
        this.pinMode = pinMode;
    }

    @Override
    protected Boolean serialize(ByteArrayOutputStream outputStream) {
        // For this message, the channel/pin is its own byte?
        outputStream.write(pinIdentifier);
        // Output the PinCapability mode byte value.
        outputStream.write(pinMode.getIdentifierByte());
        return true;
    }

}
