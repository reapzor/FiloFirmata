package com.bortbort.arduino.FiloFirmata.Messages;

import com.bortbort.arduino.FiloFirmata.Parser.SysexCommandBytes;


import java.io.ByteArrayOutputStream;

/**
 * Sysex Report Firmware Message
 * Asks the Firmata device to reply with its protocol version and firmware/file title/name
 * https://github.com/firmata/protocol/blob/master/protocol.md
 */
public class SysexReportFirmwareMessage implements Message {
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


    public SysexReportFirmwareMessage(Integer majorVersion, Integer minorVersion, String firmwareName) {
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

}
