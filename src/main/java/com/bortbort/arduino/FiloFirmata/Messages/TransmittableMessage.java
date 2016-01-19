package com.bortbort.arduino.FiloFirmata.Messages;

import com.bortbort.arduino.FiloFirmata.Parser.CommandBytes;

import java.io.ByteArrayOutputStream;

/**
 * Transmittable Message
 * Enables transmitting of a Firmata command.
 * Supplies a system for serializing the message into a byte array to be sent over the wire.
 */
public abstract class TransmittableMessage implements Message {
    /**
     * CommandByte used to identify this message over the SerialPort communications line.
     */
    byte commandByte;

    /**
     * Construct a new TransmittableMessage using a pre-defined Firmata CommandByte.
     *
     * @param commandByte CommandBytes commandByte identifying the command that this message is for.
     */
    public TransmittableMessage(CommandBytes commandByte) {
        this.commandByte = commandByte.getCommandByte();
    }

    /**
     * Construct a new TransmittableMessage using a CommandByte.
     *
     * @param commandByte CommandByte identifying the command that this message is for.
     */
    public TransmittableMessage(Byte commandByte) {
        this.commandByte = commandByte;
    }

    /**
     * Serialize a Message object into an array of bytes to be sent over the SerialPort to the Firmata supported
     * communications device.
     *
     * @param outputStream ByteArrayOutputStream to build message in.
     * @return byte[] array representing the data in the TransmittableMessage implementation.
     */
    protected abstract Boolean serialize(ByteArrayOutputStream outputStream);

    /**
     * Combine the CommandByte and the serialized message packet together to form a Firmata supported
     * byte packet to be sent over the SerialPort.
     *
     * @return true if no errors building message
     */
    public byte[] toByteArray() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(32);

        outputStream.write(commandByte);

        if (serialize(outputStream)) {
            return outputStream.toByteArray();
        }

        return null;
    }

    /**
     * CommandByte used to identify this message over the SerialPort communications line.
     *
     * @return Byte representing the Firmata Command for this message.
     */
    public Byte getCommandByte() {
        return commandByte;
    }
}
