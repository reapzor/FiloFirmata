package com.bortbort.arduino.FiloFirmata.Parser;

/**
 * Created by chuck on 1/6/2016.
 */
public abstract class SysexMessageParser {
    private byte commandByte;

    public SysexMessageParser(byte commandByte) {
        this.commandByte = commandByte;
    }

    public byte getCommandByte() {
        return commandByte;
    }

    public abstract Boolean buildMessage(byte[] messageBody);
}
