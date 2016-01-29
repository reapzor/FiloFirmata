package com.bortbort.arduino.FiloFirmata.Messages;

/**
 * Abstract Channel Message
 * Defines a base Message that supports "Midi Channels" or in our case, "Firmata Pins".
 * Handles identification and access of the Channel Byte which was masked with the Command Byte.
 */
public abstract class ChannelMessage implements Message {
    private byte channelByte;

    public ChannelMessage(byte channelByte) {
        this.channelByte = channelByte;
    }

    /**
     * Get Channel Byte
     * @return Byte representing the Channel or Firmata Pin that the Command Byte applies to.
     */
    public byte getChannelByte() {
        return channelByte;
    }

    /**
     * Get Channel Int
     * @return Int representing the Channel or Firmata Pin that the Command byte applies to.
     */
    public int getChannelInt() { return (int) channelByte; }
}
