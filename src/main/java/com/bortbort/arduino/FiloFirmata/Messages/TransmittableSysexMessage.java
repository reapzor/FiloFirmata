package com.bortbort.arduino.FiloFirmata.Messages;

import com.bortbort.arduino.FiloFirmata.Parser.CommandBytes;
import com.bortbort.arduino.FiloFirmata.Parser.SysexCommandBytes;

import java.io.ByteArrayOutputStream;

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
     * @param outputStream ByteArrayOutputStream to build message in.
     * @return true if no errors building message
     */
    protected abstract Boolean serialize(ByteArrayOutputStream outputStream);

    /**
     * Combine the SysexCommandByte and the serialized sysex message packet together to form a Firmata supported
     * sysex byte packet to be sent over the SerialPort.
     *
     * @return byte[] array representing the full Firmata sysex command packet to be sent to the Firmata device.
     */
    @Override
    public byte[] toByteArray() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(32);

        outputStream.write(commandByte);
        outputStream.write(sysexCommandByte);

        if (serialize(outputStream)) {
            outputStream.write(CommandBytes.END_SYSEX.getCommandByte());
            return outputStream.toByteArray();
        }

        return null;
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
