package com.bortbort.arduino.FiloFirmata.Messages;

import com.bortbort.arduino.FiloFirmata.Parser.SysexCommandBytes;

/**
 * SysexReportFirmwareMessage packet
 * https://github.com/firmata/protocol/blob/master/protocol.md
 */
public class SysexReportFirmwareMessage extends TransmittableSysexMessage {
    private Integer majorVersion;
    private Integer minorVersion;
    private String firmwareName;

    public SysexReportFirmwareMessage(int majorVersion, int minorVersion, String firmwareName) {
        super(SysexCommandBytes.REPORT_FIRMWARE);
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

    @Override
    public byte[] serializeSysex() {
        return null;
    }
}
