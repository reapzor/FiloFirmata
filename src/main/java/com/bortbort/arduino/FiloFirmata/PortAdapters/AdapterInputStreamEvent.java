package com.bortbort.arduino.FiloFirmata.PortAdapters;

/**
 * Created by chuck on 1/4/2016.
 */
public class AdapterInputStreamEvent {
    private Integer byteCount;

    public AdapterInputStreamEvent(Integer byteCount) {
        this.byteCount = byteCount;
    }

    public Integer getByteCount() {
        return byteCount;
    }
}
