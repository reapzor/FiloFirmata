package com.bortbort.arduino.FiloFirmata.Messages;

/**
 * Created by chuck on 1/11/2016.
 */
public abstract class ChannelMessage implements Message {
    private byte channelByte;

    public ChannelMessage(byte channelByte) {
        this.channelByte = channelByte;
    }

    public byte getChannelByte() {
        return channelByte;
    }
}
