package com.bortbort.arduino.FiloFirmata.Parser;

import com.bortbort.arduino.FiloFirmata.Messages.Message;

/**
 * Created by chuck on 1/6/2016.
 */
public abstract class SysexMessageBuilder extends MessageBuilderBase {

    public SysexMessageBuilder(SysexCommandBytes commandByte) {
        super(commandByte.getCommandByte());
    }

    public SysexMessageBuilder(byte commandByte) {
        super(commandByte);
    }

    public abstract Message buildMessage(byte[] messageBody);
}
