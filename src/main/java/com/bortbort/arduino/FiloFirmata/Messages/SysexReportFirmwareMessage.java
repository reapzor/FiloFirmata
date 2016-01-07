package com.bortbort.arduino.FiloFirmata.Messages;

import com.bortbort.arduino.FiloFirmata.Listeners.SysexReportFirmwareListener;

/**
 * Created by chuck on 1/6/2016.
 */
public class SysexReportFirmwareMessage extends Message {
    private Integer majorVersion;
    private Integer minorVersion;
    private String firmwareName;

    public SysexReportFirmwareMessage(int majorVersion, int minorVersion, String firmwareName) {
        super(SysexReportFirmwareListener.class);
        this.majorVersion = majorVersion;
        this.minorVersion = minorVersion;
        this.firmwareName = firmwareName;
    }

    public int getMajorVersion() {
        return majorVersion;
    }

    public int getMinorVersion() {
        return minorVersion;
    }

    public String getFirmwareName() {
        return firmwareName;
    }
}
