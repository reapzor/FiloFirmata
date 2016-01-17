package com.bortbort.arduino.FiloFirmata.Messages;

import com.bortbort.arduino.FiloFirmata.Parser.CommandBytes;

import java.io.ByteArrayOutputStream;

/**
 * Created by chuck on 1/16/2016.
 */
public class SetDigitalPinValueMessage extends TransmittableMessage {
    private int pinIdentifier;
    private DigitalPinValues pinValue;

    public SetDigitalPinValueMessage(int pinIdentifier, DigitalPinValues pinValue) {
        super(CommandBytes.SET_DIGITAL_PIN_VALUE);
        this.pinIdentifier = pinIdentifier;
        this.pinValue = pinValue;
    }

    @Override
    protected Boolean serialize(ByteArrayOutputStream outputStream) {
        outputStream.write(pinIdentifier);
        outputStream.write(pinValue.getByteValue());
        return true;
    }
}
