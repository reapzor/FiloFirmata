package com.bortbort.arduino.FiloFirmata.Messages;

/**
 * Created by chuck on 1/16/2016.
 */
public enum DigitalPinValues {
    LOW    (0x00),
    HIGH   (0x01);

    private byte byteValue;

    DigitalPinValues(int byteValue) {
        this.byteValue = (byte) byteValue;
    }

    public byte getByteValue() {
        return byteValue;
    }
}
