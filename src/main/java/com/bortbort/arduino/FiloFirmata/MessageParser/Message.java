package com.bortbort.arduino.FiloFirmata.MessageParser;

import java.io.InputStream;

/**
 * Created by chuck on 1/5/2016.
 */
public abstract class Message {
    private byte identifierByte;

    public Message(byte identifierByte) {
        this.identifierByte = identifierByte;
    }

    public byte getIdentifierByte() {
        return identifierByte;
    }

    public abstract Boolean parseMessage(InputStream inputStream);

}
