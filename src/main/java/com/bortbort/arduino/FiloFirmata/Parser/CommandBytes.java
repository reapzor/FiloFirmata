package com.bortbort.arduino.FiloFirmata.Parser;

/**
 * Firmata Protocol Command Bytes.
 * List of all bytes used to identify specific messages sent from the Firmata device over the SerialPort.
 * https://github.com/firmata/protocol/blob/master/protocol.md
 */
public enum CommandBytes {
    DIGITAL_MESSAGE         (0x90),
    REPORT_ANALOG_PIN       (0xC0),
    REPORT_DIGITAL_PIN      (0xD0),
    ANALOG_MESSAGE          (0xE0),
    START_SYSEX             (0xF0), // Start of SYSEX packet. Used by SysexCommandParser.
    SET_PIN_MODE            (0xF4),
    SET_DIGITAL_PIN_VALUE   (0xF5),
    END_SYSEX               (0xF7), // End of SYSEX packet. Used by SysexCommandParser.
    PROTOCOL_VERSION        (0xF9),
    SYSTEM_RESET            (0xFF);

    /**
     * Command Byte representing the CommandBytes enum value.
     */
    byte commandByte;

    /**
     * Construct enum with the provided commandByte.
     *
     * @param commandByte int representing the Firmata protocol command byte.
     */
    CommandBytes(int commandByte) {
        this.commandByte = (byte) commandByte;
    }

    /**
     * Command Byte representing the CommandBytes enum value.
     *
     * @return Byte representing the CommandBytes enum value.
     */
    public byte getCommandByte() {
        return commandByte;
    }
}
