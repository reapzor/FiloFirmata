package com.bortbort.arduino.FiloFirmata;


import java.util.ArrayList;
import java.util.Arrays;

/**
 * Provides details about Arduino "Channels", and methods around mapping to and from one.
 */
public enum DigitalChannel {
    // Map all pins to their digital channel ports
    // First int is channel ID. Remaining are list of pins in channel.
    Channel0 (0, 0, 1, 2, 3, 4, 5, 6),
    Channel1 (1, 7, 8, 9, 10, 11, 12, 13),
    Channel2 (2, 14, 15, 16, 17, 18, 19, 20),
    Channel3 (3, 21, 22, 23, 24, 25, 26, 27);

    // Collection of the pins mapped to the channel
    private ArrayList<Integer> pins;

    // Integer identifier of the enum
    private Integer identifier;

    DigitalChannel(Integer identifier, Integer... pins) {
        this.identifier = identifier;
        this.pins = new ArrayList<>(Arrays.asList(pins));
    }

    /**
     * Get a list of arduino pins associated with this channel
     * @return ArrayList of 8 pins representing the channel
     */
    public ArrayList<Integer> getPins() {
        return pins;
    }

    /**
     * Get the identifier integer for this DigitanChannel
     * @return Integer representing this DigitalChannel
     */
    public Integer getIdentifier() {
        return identifier;
    }

    /**
     * Check if this channel contains a specific Arduino pin within it.
     * @param pin Integer value of the pin to check
     * @return True if the channel contains the pin. False if not.
     */
    public Boolean containsPin(Integer pin) {
        return pins.contains(pin);
    }

    /**
     * Get the digital channel associated with the integer value given
     * @param channelIdentifier Integer representing the channel to return (0 for channel 0)
     * @return DigitalChannel representing the integer provided
     */
    public static DigitalChannel getChannel(Integer channelIdentifier) {
        for (DigitalChannel channel : DigitalChannel.values()) {
            if (channel.getIdentifier().equals(channelIdentifier)) {
                return channel;
            }
        }

        return null;
    }

    /**
     * Get a list of arduino pins associated with this channel
     * @param channelIdentifier Integer value representing the channel (0 for channel0)
     * @return ArrayList of 8 pins representing the channel
     */
    public static ArrayList<Integer> getPins(Integer channelIdentifier) {
        DigitalChannel channel = getChannel(channelIdentifier);

        if (channel != null) {
            return channel.getPins();
        }

        return null;
    }

    /**
     * Check if a channel contains a specific Arduino pin within it.
     * @param channel DigitalChannel to be checked
     * @param pin Integer value of the pin to check
     * @return True if the channel contains the pin. False if not.
     */
    public static Boolean channelContainsPin(DigitalChannel channel, Integer pin) {
        return channel.getPins().contains(pin);
    }

    /**
     * Gets the specific DigitalChannel that contains the requested Arduino pin
     * @param pin Integer value of the pin that the desired channel should contain
     * @return DigitalChannel that contains the given pin
     */
    public static DigitalChannel getChannelForPin(Integer pin) {
        for (DigitalChannel digitalChannel : DigitalChannel.values()) {
            if (digitalChannel.getPins().contains(pin)) {
                return digitalChannel;
            }
        }

        return null;
    }
}
