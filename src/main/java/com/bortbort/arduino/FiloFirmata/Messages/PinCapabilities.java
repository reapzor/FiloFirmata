package com.bortbort.arduino.FiloFirmata.Messages;

import java.util.ArrayList;

/**
 * Firmata Pin Capabilities.
 * Holds the byte values representing various Pin Modes that a Firmata device pin could support.
 */
public enum PinCapabilities {
    INPUT          (0x00), // defined in Arduino.h
    OUTPUT         (0x01), // defined in Arduino.h
    ANALOG         (0x02), // analog pin in analogInput mode
    PWM            (0x03), // digital pin in PWM output mode
    SERVO          (0x04), // digital pin in Servo output mode
    SHIFT          (0x05), // shiftIn/shiftOut mode
    I2C            (0x06), // pin included in I2C setup
    ONEWIRE        (0x07), // pin configured for 1-wire
    STEPPER        (0x08), // pin configured for stepper motor
    ENCODER        (0x09), // pin configured for rotary encoders
    SERIAL         (0x0A), // pin configured for serial communication
    INPUT_PULLUP   (0x0B), // enable internal pull-up resistor for pin
    IGNORE         (0x7F); // pin configured to be ignored by digitalWrite and capabilityResponse

    private byte identifierByte;
    PinCapabilities(int identifierByte) {
        this.identifierByte = (byte) identifierByte;
    }

    public byte getIdentifierByte() {
        return identifierByte;
    }

    public static ArrayList<PinCapabilities> getCapabilities(byte... identifierBytes) {
        ArrayList<PinCapabilities> pinCapabilities = new ArrayList<>();
        for (byte identifierByte : identifierBytes) {
            PinCapabilities pinCapability = getCapability(identifierByte);
            if (pinCapability != null) {
                pinCapabilities.add(pinCapability);
            }
        }

        if (pinCapabilities.isEmpty()) {
            pinCapabilities.add(IGNORE);
        }

        return pinCapabilities;
    }

    public static PinCapabilities getCapability(byte identifierByte) {
        for (PinCapabilities pinCapability : PinCapabilities.values()) {
            if (pinCapability.getIdentifierByte() == identifierByte) {
                return pinCapability;
            }
        }

        return null;
    }
}