package com.bortbort.arduino.FiloFirmata.Parser;

/**
 * Base Message Builder Class.
 * Handles the ownership of the Firmata Command Byte needed to identify the message type this builder is for.
 */
public abstract class MessageBuilderBase {
    /**
     * CommandByte used to identify the Firmata message this builder is implemented for.
     */
    private byte commandByte;

    /**
     * Construct a new MessageBuilderBase using a CommandByte.
     *
     * @param commandByte CommandByte identifying the command that this builder is implemented for.
     */
    public MessageBuilderBase(byte commandByte) {
        this.commandByte = commandByte;
    }

    /**
     * CommandByte used to identify the Firmata message this builder is implemented for.
     */
    public byte getCommandByte() {
        return commandByte;
    }
}
