package com.bortbort.arduino.FiloFirmata.PortAdapters;

/**
 * Created by chuck on 1/4/2016.
 */
public enum SerialPortParity {
    PARITY_NONE(0),
    PARITY_ODD(1),
    PARITY_EVEN(2),
    PARITY_MARK(3),
    PARITY_SPACE(4);

    private Integer parity;

    SerialPortParity(Integer parity) {
        this.parity = parity;
    }

    public Integer getParity() {
        return parity;
    }
}
