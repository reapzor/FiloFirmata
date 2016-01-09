package com.bortbort.arduino.FiloFirmata.Parser;

import com.bortbort.arduino.FiloFirmata.Messages.Message;
import java.io.InputStream;

/**
 * Created by chuck on 1/5/2016.
 */
public abstract class MessageBuilder extends MessageBuilderBase {

    public MessageBuilder(byte commandByte) {
        super(commandByte);
    }

    public abstract Message buildMessage(InputStream inputStream);

}
