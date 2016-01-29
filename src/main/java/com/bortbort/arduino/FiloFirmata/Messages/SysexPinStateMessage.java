package com.bortbort.arduino.FiloFirmata.Messages;


/**
 * Sysex Pin State Response Message
 * Contains the details for a specific Firmata pin, including its current PinCapabilities pinMode, the pin
 * value (High/Low) that was set by the user or PinMode (Pullup has pinValue or logic HIGH always since
 * it is enabling internal pullup).
 */
public class SysexPinStateMessage implements Message {
    private Integer pinIdentifier;
    private PinCapabilities currentPinMode;
    private Integer pinValue;

    public SysexPinStateMessage(Integer pinIdentifier, PinCapabilities currentPinMode, Integer pinValue) {
        this.pinIdentifier = pinIdentifier;
        this.currentPinMode = currentPinMode;
        this.pinValue = pinValue;
    }

    /**
     * Get Pin Identifier
     * @return Integer index value of the Firmata pin.
     */
    public Integer getPinIdentifier() {
        return pinIdentifier;
    }

    /**
     * Get Current Pin Mode
     * @return PinCapabilities mode that is currently set on the Firmata pin.
     */
    public PinCapabilities getCurrentPinMode() {
        return currentPinMode;
    }

    /**
     * Get Pin Value
     * Gets the pin value that was set on this pin. For pullup, this will be 1. For output, this could be 1 or 0.
     * For input, this is always 0.
     * Representing the logic level 'out' of the pin, either set by user or by PinCapabilities pin mode.
     * @return Integer value that has been set to the pin (NOT READ!)
     */
    public Integer getPinValue() {
        return pinValue;
    }

}
