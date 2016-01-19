package com.bortbort.arduino.FiloFirmata.Messages;


/**
 * Protocol Version Message
 * Holds details on the firmware version the Firmata device is running.
 */
public class ProtocolVersionMessage implements Message {
    Integer majorVersion;
    Integer minorVersion;

    public ProtocolVersionMessage(Integer majorVersion, Integer minorVersion) {
        this.majorVersion = majorVersion;
        this.minorVersion = minorVersion;
    }

    /**
     * Get Major Version
     * @return the whole version value from the firmware version (Eg: 2.5, Major is "2")
     */
    public Integer getMajorVersion() {
        return majorVersion;
    }

    /**
     * Get Minor Version
     * @return the partial version value from the firmware version (Eg: 2.5, Minor is "5")
     */
    public Integer getMinorVersion() {
        return minorVersion;
    }

}
