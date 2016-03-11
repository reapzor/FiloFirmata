package com.bortbort.arduino.FiloFirmata;

import java.util.ArrayList;

/**
 * Firmata Pin Capabilities.
 * Holds the byte values representing various Pin Modes that a Firmata device pin could support.
 */
public enum PinCapability {
    INPUT          (0x00), // defined in Arduino.h DIGITAL IN LOW
    OUTPUT         (0x01), // defined in Arduino.h DIGITAL OUT
    ANALOG         (0x02), // ANALOG INPUT
    PWM            (0x03), // digital pin in PWM output mode "ANALOG" OUTPUT
    SERVO          (0x04), // digital pin in Servo output mode
    SHIFT          (0x05), // shiftIn/shiftOut mode
    I2C            (0x06), // pin included in I2C setup
    ONEWIRE        (0x07), // pin configured for 1-wire
    STEPPER        (0x08), // pin configured for stepper motor
    ENCODER        (0x09), // pin configured for rotary encoders
    SERIAL         (0x0A), // pin configured for serial communication
    INPUT_PULLUP   (0x0B), // enable internal pull-up resistor for pin DIGITAL IN HIGH
    IGNORE         (0x7F); // pin configured to be ignored by digitalWrite and capabilityResponse

    private byte identifierByte;
    PinCapability(int identifierByte) {
        this.identifierByte = (byte) identifierByte;
    }

    public Byte getIdentifierByte() {
        return identifierByte;
    }

    public static ArrayList<PinCapability> getCapabilities(byte... identifierBytes) {
        ArrayList<PinCapability> pinCapabilities = new ArrayList<>();
        for (byte identifierByte : identifierBytes) {
            PinCapability pinCapability = getCapability(identifierByte);
            if (pinCapability != null) {
                pinCapabilities.add(pinCapability);
            }
        }

        if (pinCapabilities.isEmpty()) {
            pinCapabilities.add(IGNORE);
        }

        return pinCapabilities;
    }

    public static PinCapability getCapability(byte identifierByte) {
        for (PinCapability pinCapability : PinCapability.values()) {
            if (pinCapability.getIdentifierByte() == identifierByte) {
                return pinCapability;
            }
        }

        return null;
    }
}