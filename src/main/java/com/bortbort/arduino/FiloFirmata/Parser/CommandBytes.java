package com.bortbort.arduino.FiloFirmata.Parser;

/**
 * Created by chuck on 1/6/2016.
 */
public enum CommandBytes {
    ANALOG_MESSAGE      (0xE0),
    DIGITAL_MESSAGE     (0x90),
    EVENT_ANALOG_PIN    (0xC0),
    EVENT_DIGITAL_PIN   (0xD0),
    SET_PIN_MODE        (0xF4),
    SET_PIN_VALUE       (0xF5),
    START_SYSEX         (0xF0),
    END_SYSEX           (0xF7);


    byte commandByte;
    CommandBytes(int commandByte) {
        this.commandByte = (byte) commandByte;
    }

    public byte getCommandByte() {
        return commandByte;
    }
}
