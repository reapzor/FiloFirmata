package com.bortbort.arduino.FiloFirmata.Parser;

import com.bortbort.arduino.FiloFirmata.Messages.Message;

/**
 * Created by chuck on 1/6/2016.
 */
public abstract class SysexMessageBuilder {
    private byte commandByte;

    public SysexMessageBuilder(byte commandByte) {
        this.commandByte = commandByte;
    }

    public byte getCommandByte() {
        return commandByte;
    }

    public abstract Message buildMessage(byte[] messageBody);
}
