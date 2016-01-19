package com.bortbort.arduino.FiloFirmata.Messages;

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
    private ArrayList<Integer> pinValues;
    private HashMap<Integer, Integer> pinMappedValues;
    private Byte portByte;

    public DigitalPortMessage(byte channelPort, ArrayList<Integer> pinValues,
                              HashMap<Integer, Integer> pinMappedValues, Byte portByte) {
        super(channelPort);
        this.pinValues = pinValues;
        this.pinMappedValues = pinMappedValues;
        this.portByte = portByte;
    }

    /**
     * Get Pin Values
     * An ordered array of pin values where index 0 could be pin 0, pin 8, or pin 16, depending on the Channel.
     * If you know the channel you will be able to identify the pins naturally, but if you are un-aware of the channel
     * or don't want to handle any mapping of values to pin indexes, use getPortMappedValues().
     * @return ArrayList of digital pin logic levels from pin 0-7 (c1), 8-15 (c2), 16-23 (c3)
     */
    public ArrayList<Integer> getPinValues() {
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

    /**
     * Get Pin Mapped Values
     * HashMap variant of getPinValues() array, however this maps all actual pin indexes to the captured values.
     * The keys will be 0-7 for channel 1, 8-15 for channel 2, 16-23 for channel 3.
     * @return HashMap containing a map of all pin values and their actual index in the Firmata device.
     */
    public HashMap<Integer, Integer> getPinMappedValues() {
        return pinMappedValues;
    }

}
