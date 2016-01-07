package com.bortbort.arduino.FiloFirmata.Messages;

/**
 * Created by chuck on 1/6/2016.
 */
public class SysexReportFirmwareMessage {
    private Integer majorVersion;
    private Integer minorVersion;
    private String firmwareName;

    public SysexReportFirmwareMessage(int majorVersion, int minorVersion, String firmwareName) {
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
