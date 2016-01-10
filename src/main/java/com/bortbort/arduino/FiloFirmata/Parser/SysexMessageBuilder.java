package com.bortbort.arduino.FiloFirmata.Parser;

import com.bortbort.arduino.FiloFirmata.Messages.Message;

/**
 * SysexMessageBuilder definition for building Firmata Sysex Message objects from a determined byte array
 */
public abstract class SysexMessageBuilder extends MessageBuilderBase {

    /**
     * Construct a SysexMessageBuilder using the given SysexCommandByte.
     */
    public SysexMessageBuilder(SysexCommandBytes commandByte) {
        super(commandByte.getSysexCommandByte());
    }

    /**
     * Construct a SysexMessageBuilder using the given sysexCommandByte.
     */
    public SysexMessageBuilder(byte commandByte) {
        super(commandByte);
    }

    /**
     * Build a Sysex Message from the given byte array.
     *
     * @param messageBody Byte array containing the serialized byte data representing the Sysex Message.
     * @return Firmata Sysex Message object representing the data obtained from the byte array.
     */
    public abstract Message buildMessage(byte[] messageBody);
}
