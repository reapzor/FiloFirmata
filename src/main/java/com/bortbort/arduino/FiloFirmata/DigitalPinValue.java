package com.bortbort.arduino.FiloFirmata;

/**
 * Digital Pin Values
 * Simple ENUM defining the logic behind a digital communications rail.
 */
public enum DigitalPinValue {
    LOW    (0x00), // Digital Low
    HIGH   (0x01); // Digital High

    private byte byteValue;

    DigitalPinValue(int byteValue) {
        this.byteValue = (byte) byteValue;
    }

    /**
     * Get Byte Value
     * @return Byte representing the digital logic pin value.
     */
    public Byte getByteValue() {
        return byteValue;
    }
}
