package com.bortbort.arduino.FiloFirmata.Messages;

/**
 * Created by chuck on 1/11/2016.
 */
public class AnalogMessage extends ChannelMessage {
    private byte analogValue;

    public AnalogMessage(byte channelByte, byte analogValue) {
        super(channelByte);
        this.analogValue = analogValue;
    }

    public byte getAnalogValue() {
        return analogValue;
    }
}
