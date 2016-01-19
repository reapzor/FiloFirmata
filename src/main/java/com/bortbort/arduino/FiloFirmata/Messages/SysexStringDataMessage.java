package com.bortbort.arduino.FiloFirmata.Messages;

/**
 * Sysex String Data Message
 * Holds a String message sent by the Firmata device.
 */
public class SysexStringDataMessage implements Message {
    private String stringData;

    public SysexStringDataMessage(String stringData) {
        this.stringData = stringData;
    }

    /**
     * Get String Data
     * @return String that was sent up by the Firmata device.
     */
    public String getStringData() {
        return stringData;
    }
}
