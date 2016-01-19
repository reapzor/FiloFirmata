package com.bortbort.arduino.FiloFirmata.Messages;

import com.bortbort.arduino.FiloFirmata.Parser.SysexCommandBytes;

import java.io.ByteArrayOutputStream;

/**
 * Sysex Pin State Query Message
 * Asks the Firmata device to respond with the current state/mode/value of a given pin index.
 */
public class SysexPinStateQueryMessage extends TransmittableSysexMessage {
    int pinIdentifier;

    /**
     * Sysex Pin State Query Messae
     * @param pinIdentifier Integer index of pin to get state for.
     */
    public SysexPinStateQueryMessage(Integer pinIdentifier) {
        super(SysexCommandBytes.PIN_STATE_QUERY);
        this.pinIdentifier = pinIdentifier;
    }

    public Integer getPinIdentifier() {
        return pinIdentifier;
    }

    @Override
    protected Boolean serialize(ByteArrayOutputStream outputStream) {
        // Output the pin index as its own byte while building the message.
        outputStream.write(pinIdentifier);
        return true;
    }

}
