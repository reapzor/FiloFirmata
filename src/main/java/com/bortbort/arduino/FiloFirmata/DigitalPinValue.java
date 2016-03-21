package com.bortbort.arduino.FiloFirmata;

/**
 * Digital Pin Values.
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
     * Get Byte Value.
     * @return Byte representing the digital logic pin value.
     */
    public Byte getByteValue() {
        return byteValue;
    }

    /**
     * Translate to DigitalPinValue from a supplied Integer.
     * @param pinValue Integer representing the digital value.
     * @return DigitalPinValue representing the integer (High/Low).
     */
    public static DigitalPinValue valueFromInt(Integer pinValue) {
        return pinValue <= 0 ? LOW : HIGH;
    }

    /**
    * Translate to DigitalPinValue from a supplied Byte.
    * @param byteValue Byte representing the digital value.
     * @return DigitalPinValue representing the byte (High/Low).
    */
    public static DigitalPinValue valueFromByte(byte byteValue) {
        return valueFromInt((int) byteValue);
    }
}
