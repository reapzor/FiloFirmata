package com.bortbort.arduino.FiloFirmata.Messages;


import com.bortbort.arduino.FiloFirmata.DigitalPinValue;
import com.bortbort.arduino.FiloFirmata.PinCapability;

/**
 * Sysex Pin State Response Message
 * Contains the details for a specific Firmata pin, including its current PinCapability pinMode, the pin
 * value (High/Low) that was set by the user or PinMode (Pullup has pinValue or logic HIGH always since
 * it is enabling internal pullup).
 */
public class SysexPinStateMessage implements Message {
    private Integer pinIdentifier;
    private PinCapability currentPinMode;
    private Integer pinValue;
    private DigitalPinValue digitalPinValue;

    public SysexPinStateMessage(Integer pinIdentifier, PinCapability currentPinMode, Integer pinValue) {
        this.pinIdentifier = pinIdentifier;
        this.currentPinMode = currentPinMode;
        this.pinValue = pinValue;
        digitalPinValue = DigitalPinValue.valueFromInt(pinValue);
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
     * @return PinCapability mode that is currently set on the Firmata pin.
     */
    public PinCapability getCurrentPinMode() {
        return currentPinMode;
    }

    /**
     * Get Pin Value
     * Gets the pin value that was set on this pin.
     * Representing the logic level 'out' of the pin, either set by user or by PinCapability pin mode.
     * @return Integer value that has been set to the pin (NOT READ!)
     */
    public Integer getPinValue() {
        return pinValue;
    }

    /**
     * Get Digital Pin Value
     * Gets the pin value that was set on this pin, translated to digital HIGH/LOW enum values.
     * @return DigitalPinValue representing the current Pin value. (Pullup or down, this does NOT represent
     * what is being written to the digital pin if it is set as output.)
     */
    public DigitalPinValue getDigitalPinValue() {
        return digitalPinValue;
    }
}
