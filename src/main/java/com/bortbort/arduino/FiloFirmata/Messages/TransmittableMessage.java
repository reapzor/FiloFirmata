package com.bortbort.arduino.FiloFirmata.Messages;

import com.bortbort.arduino.FiloFirmata.Parser.CommandBytes;

/**
 * Transmittable Message used to represent a set of Java data that can be serialized into a Firmata data packet
 */
public abstract class TransmittableMessage extends Message {
    Byte commandByte;

    public TransmittableMessage(CommandBytes commandByte) {
        this.commandByte = commandByte.getCommandByte();
    }

    public TransmittableMessage(Byte commandByte) {
        this.commandByte = commandByte;
    }

    /**
     * Serialize a Message object into an array of bytes to be sent over the SerialPort to the Firmata supported
     * communications device.
     *
     * @return byte[] array representing the data in the TransmittableMessage implementation.
     */
    public abstract byte[] serialize();

    /**
     * Combine the CommandByte and the serialized message packet together to form a Firmata supported
     * command packet to be sent over the SerialPort.
     *
     * @return byte[] array representing the full Firmata command packet to be sent to the Firmata device.
     */
    public byte[] toByteArray() {
        byte[] messageBytes = serialize();

        if (messageBytes == null) {
            return new byte[] {commandByte};
        }

        byte[] outputBytes = new byte[messageBytes.length + 1];
        outputBytes[0] = commandByte;
        System.arraycopy(messageBytes, 0, outputBytes, 1, messageBytes.length);
        return outputBytes;
    }
}
