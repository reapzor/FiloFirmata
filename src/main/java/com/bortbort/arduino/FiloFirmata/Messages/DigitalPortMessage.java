package com.bortbort.arduino.FiloFirmata.Messages;

import com.bortbort.arduino.FiloFirmata.Parser.CommandBytes;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by chuck on 1/11/2016.
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

    public ArrayList<Integer> getPinValues() {
        return pinValues;
    }

    public Byte getPortByte() {
        return portByte;
    }

    public HashMap<Integer, Integer> getPinMappedValues() {
        return pinMappedValues;
    }

}
