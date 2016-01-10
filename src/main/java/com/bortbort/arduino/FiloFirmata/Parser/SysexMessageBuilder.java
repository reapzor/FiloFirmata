package com.bortbort.arduino.FiloFirmata.Parser;

import com.bortbort.arduino.FiloFirmata.Messages.Message;

/**
 * SysexMessageBuilder definition for building Firmata Sysex Message objects from a determined byte array.
 */
public abstract class SysexMessageBuilder extends MessageBuilderBase {

    /**
     * Construct a SysexMessageBuilder using the given SysexCommandByte.
     *
     * @param sysexCommandByte SysexCommandBytes enum value representing a Firmata sysex command byte.
     */
    public SysexMessageBuilder(SysexCommandBytes sysexCommandByte) {
        super(sysexCommandByte.getSysexCommandByte());
    }

    /**
     * Construct a SysexMessageBuilder using the given sysexCommandByte.
     *
     * @param sysexCommandByte Byte value representing a Firmata sysex command byte.
     */
    public SysexMessageBuilder(byte sysexCommandByte) {
        super(sysexCommandByte);
    }

    /**
     * Build a Sysex Message from the given byte array.
     *
     * @param messageBody Byte array containing the serialized byte data representing the Sysex Message.
     * @return Firmata Sysex Message object representing the data obtained from the byte array.
     */
    public abstract Message buildMessage(byte[] messageBody);
}
