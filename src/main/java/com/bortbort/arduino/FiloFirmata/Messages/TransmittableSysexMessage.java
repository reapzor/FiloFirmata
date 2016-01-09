package com.bortbort.arduino.FiloFirmata.Messages;

import com.bortbort.arduino.FiloFirmata.Parser.CommandBytes;
import com.bortbort.arduino.FiloFirmata.Parser.SysexCommandBytes;

/**
 * Transmittable SysexMessage used to represent a set of Java data that can be serialized into a Firmata data packet
 */
public abstract class TransmittableSysexMessage extends TransmittableMessage {
    Byte sysexCommandByte;

    public TransmittableSysexMessage(SysexCommandBytes sysexCommandByte) {
        this(sysexCommandByte.getCommandByte());
    }

    public TransmittableSysexMessage(Byte sysexCommandByte) {
        super(CommandBytes.START_SYSEX);
        this.sysexCommandByte = sysexCommandByte;
    }

    /**
     * Serialize a Message object into an array of bytes to be sent over the SerialPort to the Firmata supported
     * communications device.
     *
     * @return byte[] array representing the Sysex message data in the TransmittableMessage implementation.
     */
    public abstract byte[] serializeSysex();

    /**
     * Serialize a SysexMessage object into an array of bytes to be sent over the SerialPort to the Firmata supported
     * communications device.
     *
     * @return byte[] array representing the full TransmittableSysexMessage data packet
     */
    @Override
    public byte[] serialize() {
        byte[] messageBytes = serializeSysex();

        if (messageBytes == null) {
            return new byte[] {sysexCommandByte};
        }

        byte[] outputBytes = new byte[messageBytes.length + 2];
        outputBytes[0] = sysexCommandByte;
        System.arraycopy(messageBytes, 0, outputBytes, 1, messageBytes.length);
        outputBytes[outputBytes.length-1] = CommandBytes.END_SYSEX.getCommandByte();
        return outputBytes;
    }
}
