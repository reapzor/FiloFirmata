package com.bortbort.arduino.FiloFirmata.Messages;

import com.bortbort.arduino.FiloFirmata.Parser.CommandBytes;

import java.io.ByteArrayOutputStream;

/**
 * Created by chuck on 1/16/2016.
 */
public class SetPinModeMessage extends TransmittableMessage {
    int pinIdentifier;
    PinCapabilities pinMode;


    public SetPinModeMessage(int pinIdentifier, PinCapabilities pinMode) {
        super(CommandBytes.SET_PIN_MODE);
        this.pinIdentifier = pinIdentifier;
        this.pinMode = pinMode;
    }

    @Override
    protected Boolean serialize(ByteArrayOutputStream outputStream) {
        outputStream.write(pinIdentifier);
        outputStream.write(pinMode.getIdentifierByte());
        return true;
    }

}
