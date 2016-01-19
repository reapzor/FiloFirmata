package com.bortbort.arduino.FiloFirmata.Messages;

/**
 * Digital Pin Values
 * Simple ENUM defining the logic behind a digital communications rail.
 */
public enum DigitalPinValues {
    LOW    (0x00), // Digital Low
    HIGH   (0x01); // Digital High

    private byte byteValue;

    DigitalPinValues(int byteValue) {
        this.byteValue = (byte) byteValue;
    }

    /**
     * Get Byte Value
     * @return Byte representing the digital logic pin value.
     */
    public byte getByteValue() {
        return byteValue;
    }
}
