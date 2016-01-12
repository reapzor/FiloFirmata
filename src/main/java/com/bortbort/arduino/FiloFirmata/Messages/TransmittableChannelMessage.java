package com.bortbort.arduino.FiloFirmata.Messages;

import com.bortbort.arduino.FiloFirmata.Parser.CommandBytes;

/**
 * Created by chuck on 1/11/2016.
 */
public abstract class TransmittableChannelMessage extends TransmittableMessage {
    private byte channelByte;

    public TransmittableChannelMessage(CommandBytes commandByte, byte channelByte) {
        super(commandByte);
        this.channelByte = channelByte;
    }

    public byte getChannelByte() {
        return channelByte;
    }

    /**
     * Combine the CommandByte and the serialized message packet together to form a Firmata supported
     * byte packet to be sent over the SerialPort. To form the CommandByte for a ChannelMessage, we need to
     * pad the command with the channel we want to handle the command (or in our case, the pin/port)
     *
     * @return byte[] array representing the full Firmata command packet to be sent to the Firmata device.
     */
    @Override
    public byte[] toByteArray() {
        byte[] messageBytes = serialize();

        byte portPaddedCommandByte = (byte) (commandByte + (channelByte & 0x0F));
        if (messageBytes == null) {
            return new byte[] { portPaddedCommandByte };
        }

        byte[] outputBytes = new byte[messageBytes.length + 1];
        outputBytes[0] = commandByte;
        System.arraycopy(messageBytes, 0, outputBytes, 1, messageBytes.length);
        return outputBytes;
    }

}
