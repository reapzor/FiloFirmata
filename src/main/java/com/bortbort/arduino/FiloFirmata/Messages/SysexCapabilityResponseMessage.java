package com.bortbort.arduino.FiloFirmata.Messages;

import java.util.ArrayList;

/**
 * Created by chuck on 1/14/2016.
 */
public class SysexCapabilityResponseMessage implements Message {
    ArrayList<ArrayList<PinCapabilities>> pinCapabilities = new ArrayList<>();

    public SysexCapabilityResponseMessage(ArrayList<byte[]> pinCapabilitiesBytes) {
        for (byte[] identifierBytes : pinCapabilitiesBytes) {
            pinCapabilities.add(PinCapabilities.getCapabilities(identifierBytes));
        }
    }

    public ArrayList<ArrayList<PinCapabilities>> getPinCapabilities() {
        return pinCapabilities;
    }

    public ArrayList<PinCapabilities> getPinCapabilities(Integer pin) {
        if (pinCapabilities.size() >= pin) {
            return pinCapabilities.get(pin);
        }
        return null;
    }

}
