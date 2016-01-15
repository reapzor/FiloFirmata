package com.bortbort.arduino.FiloFirmata.Messages;

import java.util.ArrayList;

/**
 * Created by chuck on 1/14/2016.
 */
public enum PinCapabilities {
    INPUT                   (0x00), // defined in Arduino.h
    OUTPUT                  (0x01), // defined in Arduino.h
    PIN_MODE_ANALOG         (0x02), // analog pin in analogInput mode
    PIN_MODE_PWM            (0x03), // digital pin in PWM output mode
    PIN_MODE_SERVO          (0x04), // digital pin in Servo output mode
    PIN_MODE_SHIFT          (0x05), // shiftIn/shiftOut mode
    PIN_MODE_I2C            (0x06), // pin included in I2C setup
    PIN_MODE_ONEWIRE        (0x07), // pin configured for 1-wire
    PIN_MODE_STEPPER        (0x08), // pin configured for stepper motor
    PIN_MODE_ENCODER        (0x09), // pin configured for rotary encoders
    PIN_MODE_SERIAL         (0x0A), // pin configured for serial communication
    PIN_MODE_PULLUP         (0x0B), // enable internal pull-up resistor for pin
    PIN_MODE_IGNORE         (0x7F); // pin configured to be ignored by digitalWrite and capabilityResponse

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
            pinCapabilities.add(PIN_MODE_IGNORE);
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