package com.bortbort.arduino.FiloFirmata.PortAdapters;

/**
 * Created by chuck on 1/4/2016.
 */
public enum SerialPortStopBits {
    STOPBITS_NONE(0),
    STOPBITS_1(1),
    STOPBITS_2(2),
    STOPBITS_1_5(3);

    private Integer stopBits;

    SerialPortStopBits(Integer stopBits) {
        this.stopBits = stopBits;
    }

    public Integer getStopBitsInt() {
        return stopBits;
    }
}