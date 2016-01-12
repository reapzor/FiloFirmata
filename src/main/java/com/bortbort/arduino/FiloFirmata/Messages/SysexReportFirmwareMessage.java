package com.bortbort.arduino.FiloFirmata.Messages;

import com.bortbort.arduino.FiloFirmata.Parser.SysexCommandBytes;

/**
 * SysexReportFirmwareMessage packet.
 * https://github.com/firmata/protocol/blob/master/protocol.md
 */
public class SysexReportFirmwareMessage extends TransmittableSysexMessage {
    /**
     * Firmata Major version (x.y or 2.5) where x or 2 is "major".
     */
    private Integer majorVersion;

    /**
     * Firmata Minor version (x.y or 2.5) where y or 5 is "minor".
     */
    private Integer minorVersion;

    /**
     * Firmata Firmare Name. (For Arduino, is generally the name of the .ino Arduino Processing file.)
     */
    private String firmwareName;

    public SysexReportFirmwareMessage() {
        this(null, null, null);
    }

    public SysexReportFirmwareMessage(Integer majorVersion, Integer minorVersion, String firmwareName) {
        super(SysexCommandBytes.REPORT_FIRMWARE);
        this.majorVersion = majorVersion;
        this.minorVersion = minorVersion;
        this.firmwareName = firmwareName;
    }

    /**
     * Firmata Major version (x.y or 2.5) where x or 2 is "major".
     *
     * @return int representing the Firmata device's library "Major" version.
     */
    public int getMajorVersion() {
        return majorVersion;
    }

    /**
     * Firmata Minor version (x.y or 2.5) where y or 5 is "minor".
     *
     * @return int representing the Firmata device's library "Minor" version.
     */
    public int getMinorVersion() {
        return minorVersion;
    }

    /**
     * Firmata Firmware Name. (For Arduino, is generally the name of the .ino Arduino Processing file)
     *
     * @return String representing the Firmata device's firmware library name.
     */
    public String getFirmwareName() {
        return firmwareName;
    }

    /**
     * When transmitting the request for a SysexReportFirmware message, we need no body data, only the command
     * to be sent, which is handled by the parent class. Treat the implementation as a no-op and return
     * no body. This will ensure the output is {0xF9 0x79 0xF7} (start_sysex, report_firmware, end_sysex).
     *
     * @return null.
     */
    @Override
    protected byte[] serialize() {
        return null;
    }
}
