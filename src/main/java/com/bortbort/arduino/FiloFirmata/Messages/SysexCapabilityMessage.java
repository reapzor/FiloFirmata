package com.bortbort.arduino.FiloFirmata.Messages;

import com.bortbort.arduino.FiloFirmata.PinCapability;

import java.util.ArrayList;

/**
 * Sysex Capability Response Message
 * Responds with a map of Firmata device pins and the capabilities supported on each of them.
 */
public class SysexCapabilityMessage implements Message {
    ArrayList<ArrayList<PinCapability>> pinCapabilities = new ArrayList<>();

    public SysexCapabilityMessage(ArrayList<byte[]> pinCapabilitiesBytes) {
        for (byte[] identifierBytes : pinCapabilitiesBytes) {
            pinCapabilities.add(PinCapability.getCapabilities(identifierBytes));
        }
    }

    /**
     * Get Pin Capabilities
     * Get a list of all pins and the modes each one supports.
     * @return an ArrayList of pins whose value is an ArrayList of PinCapability
     */
    public ArrayList<ArrayList<PinCapability>> getPinCapabilities() {
        return pinCapabilities;
    }

    /**
     * Get Pin Capabilities
     * Get a list of modes that a given pin supports.
     * @param pin Integer value representing the Firmata pin that we want to know the supported capabilities of.
     * @return ArrayList of PinCapability that the given pin index supports.\
     *         Null if there is no pin at the given index.
     */
    public ArrayList<PinCapability> getPinCapabilities(Integer pin) {
        if (pinCapabilities.size() >= pin) {
            return pinCapabilities.get(pin);
        }
        return null;
    }

}
