package com.bortbort.arduino.FiloFirmata.Messages;

import com.bortbort.arduino.FiloFirmata.Parser.CommandBytes;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by chuck on 1/11/2016.
 */
public class DigitalPortMessage extends TransmittableChannelMessage {
    private ArrayList<Integer> pinValues;
    private HashMap<Integer, Integer> pinMappedValues;
    private Byte portByte;

    public DigitalPortMessage(int channelPort) {
        this((byte) channelPort, null, null, null);
    }

    public DigitalPortMessage(byte channelPort, ArrayList<Integer> pinValues,
                              HashMap<Integer, Integer> pinMappedValues, Byte portByte) {
        super(CommandBytes.DIGITAL_MESSAGE,
                channelPort);
        this.pinValues = pinValues;
        this.pinMappedValues = pinMappedValues;
        this.portByte = portByte;
    }

    public ArrayList<Integer> getPinValues() {
        return pinValues;
    }

    public Byte getPortByte() {
        return portByte;
    }

    public HashMap<Integer, Integer> getPinMappedValues() {
        return pinMappedValues;
    }

    @Override
    protected byte[] serialize() {
        return null;
    }
}
