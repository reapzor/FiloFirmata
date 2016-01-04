package com.bortbort.arduino.FiloFirmata.PortAdapters;

/**
 * Created by chuck on 1/4/2016.
 */
public enum SerialPortDataBits {
    DATABITS_5(5),
    DATABITS_6(6),
    DATABITS_7(7),
    DATABITS_8(8);

    private Integer dataBits;

    SerialPortDataBits(Integer dataBits) {
        this.dataBits = dataBits;
    }

    public Integer getDataBits() {
        return dataBits;
    }
}
