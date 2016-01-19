package com.bortbort.arduino.FiloFirmata.Messages;

import com.bortbort.arduino.FiloFirmata.Parser.CommandBytes;
import com.bortbort.arduino.FiloFirmata.Parser.SysexCommandBytes;

import java.io.ByteArrayOutputStream;

/**
 * Transmittable Sysex Message
 * Enables transmitting of a Firmata Sysex command. Handles serialization of the root command byte for Sysex,
 * and the Sysex command byte that the message is for.
 * Supplies a system for serializing the Sysex message into a byte array to be sent over the wire.
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
     * Combine the SysexCommandByte and the serialized sysex message packet together to form a Firmata supported
     * sysex byte packet to be sent over the SerialPort.
     *
     * @return byte[] array representing the full Firmata sysex command packet to be sent to the Firmata device.
     */
    @Override
    public byte[] toByteArray() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(16);

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
