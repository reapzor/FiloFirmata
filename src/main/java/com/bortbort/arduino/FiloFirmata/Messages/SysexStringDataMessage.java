package com.bortbort.arduino.FiloFirmata.Messages;

/**
 * Created by chuck on 1/9/2016.
 */
public class SysexStringDataMessage extends Message {
    private String stringData;

    public SysexStringDataMessage(String stringData) {
        this.stringData = stringData;
    }

    public void setStringData(String stringData) {
        this.stringData = stringData;
    }
}
