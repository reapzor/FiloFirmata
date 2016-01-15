package com.bortbort.arduino.FiloFirmata.Messages;


/**
 * Created by chuck on 1/14/2016.
 */
public class SysexPinStateResponseMessage implements Message {
    private Integer pinIdentifier;
    private PinCapabilities currentPinMode;
    private Integer pinValue;

    public SysexPinStateResponseMessage(Integer pinIdentifier, PinCapabilities currentPinMode, Integer pinValue) {
        this.pinIdentifier = pinIdentifier;
        this.currentPinMode = currentPinMode;
        this.pinValue = pinValue;
    }

    public Integer getPinIdentifier() {
        return pinIdentifier;
    }

    public PinCapabilities getCurrentPinMode() {
        return currentPinMode;
    }

    public Integer getPinValue() {
        return pinValue;
    }
}
