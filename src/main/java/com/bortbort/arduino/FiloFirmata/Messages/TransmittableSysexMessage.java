package com.bortbort.arduino.FiloFirmata.Messages;

import com.bortbort.arduino.FiloFirmata.Parser.CommandBytes;
import com.bortbort.arduino.FiloFirmata.Parser.SysexCommandBytes;

/**
 * Transmittable SysexMessage used to represent a set of Java data that can be serialized into a Firmata data packet.
 */
public abstract class TransmittableSysexMessage extends TransmittableMessage {
    /**
     * SysexCommandByte used to identify this sysex message over the SerialPort communications line.
     */
    Byte sysexCommandByte;

    /**
     * Construct a new TransmittableSysexMessage using a pre-defined Firmata SysexCommandByte.
     *
     * @param sysexCommandByte SysexCommandBytes sysexCommandByte identifying the sysex command
     *                         that this sysex message is for.
     */
    public TransmittableSysexMessage(SysexCommandBytes sysexCommandByte) {
        this(sysexCommandByte.getSysexCommandByte());
    }

    /**
     * Construct a new TransmittableSysexMessage using a SysexCommandByte.
     *
     * @param sysexCommandByte SysexCommandByte identifying the sysex command that this sysex message is for.
     */
    public TransmittableSysexMessage(Byte sysexCommandByte) {
        super(CommandBytes.START_SYSEX);
        this.sysexCommandByte = sysexCommandByte;
    }

    /**
     * Serialize a SysexMessage object into an array of bytes to be sent over the SerialPort to the Firmata supported
     * communications device.
     *
     * @return byte[] array representing the full TransmittableSysexMessage data packet.
     */
    protected abstract byte[] serialize();

    /**
     * Combine the SysexCommandByte and the serialized sysex message packet together to form a Firmata supported
     * sysex byte packet to be sent over the SerialPort.
     *
     * @return byte[] array representing the full Firmata sysex command packet to be sent to the Firmata device.
     */
    @Override
    public byte[] toByteArray() {
        byte[] messageBytes = serialize();

        if (messageBytes == null) {
            return new byte[] { commandByte, sysexCommandByte, CommandBytes.END_SYSEX.getCommandByte() };
        }

        byte[] outputBytes = new byte[messageBytes.length + 3];
        outputBytes[0] = commandByte;
        outputBytes[1] = sysexCommandByte;
        System.arraycopy(messageBytes, 0, outputBytes, 1, messageBytes.length);
        outputBytes[outputBytes.length-1] = CommandBytes.END_SYSEX.getCommandByte();
        return outputBytes;
    }

    /**
     * SysexCommandByte used to identify this sysex message over the SerialPort communications line.
     *
     * @return Byte representing the Firmata Sysex Command for this message
     */
    public Byte getSysexCommandByte() {
        return sysexCommandByte;
    }
}
