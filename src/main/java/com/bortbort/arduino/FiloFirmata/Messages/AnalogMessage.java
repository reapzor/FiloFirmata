package com.bortbort.arduino.FiloFirmata.Messages;

/**
 * Analog Channel Message
 * Holds details on the channel (pin) that the message is for as well as the 0-255 ADC value that was read.
 */
public class AnalogMessage extends ChannelMessage {
    private byte analogValueByte;

    public AnalogMessage(byte channelByte, byte analogValueByte) {
        super(channelByte);
        this.analogValueByte = analogValueByte;
    }

    /**
     * Get Masked Analog Value.
     * Masks the AnalogValue byte with 0xFF to get us the 0-255 integer we are used to when reading an Analog value.
     * @return Integer 0-255 value representing the Analog ADC reading that took place in the Firmata device.
     */
    public Integer getAnalogValue() {
        return analogValueByte & 0xff;
    }

    /**
     * Get Analog Value Byte
     * @return Byte representing the Analog ADC reasing that took place in the Firmata device.
     */
    public Byte getAnalogValueByte() {
        return analogValueByte;
    }
}
