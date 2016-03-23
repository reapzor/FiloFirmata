package com.bortbort.arduino.FiloFirmata.Messages;

import com.bortbort.arduino.FiloFirmata.DigitalPinValue;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Digital Port Channel Message
 * Holds details on the pin that the message is for, as well as a map of Firmata pins and their read in
 * digital logic level.
 * Firmata Digital Port communicates up 8 device pins per message, based on channel
 * Channel 0 is pins 0-7. Channel 1 is pines 8-15.
 */
public class DigitalPortMessage extends ChannelMessage {
    private HashMap<Integer, Integer> pinValues;
    private Byte portByte;

    public DigitalPortMessage(byte channelPort, HashMap<Integer, Integer> pinValues, Byte portByte) {
        super(channelPort);
        this.pinValues = pinValues;
        this.portByte = portByte;
    }

    /**
     * Get Pin Integer Values
     * The keys will be 0-7 for channel 1, 8-15 for channel 2, 16-23 for channel 3.
     * @return HashMap containing a map of all pin values and their actual index in the Firmata device.
     */
    public HashMap<Integer, Integer> getPinIntegerValues() {
         return pinValues;
    }

    /**
     * Get Port Byte
     * Helpful for parallel data communications that are using all 8 pins to transmit a message.
     * In an order of 8 bits, where the byte is 0x02, indicating "00000010" where "Pin 1" is high and all
     * other pins are low.
     * @return Byte value representing the logic levels identified on the 7 pins. Ordered by pin.
     */
    public Byte getPortByte() {
        return portByte;
    }

}
