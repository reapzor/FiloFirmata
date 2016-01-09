package com.bortbort.arduino.FiloFirmata.Parser;

/**
 * Created by chuck on 1/8/2016.
 */
public class MessageBuilderBase {
    private byte commandByte;

    public MessageBuilderBase(byte commandByte) {
        this.commandByte = commandByte;
    }

    public byte getCommandByte() {
        return commandByte;
    }
}
