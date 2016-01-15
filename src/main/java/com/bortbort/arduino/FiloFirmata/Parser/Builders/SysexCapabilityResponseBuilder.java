package com.bortbort.arduino.FiloFirmata.Parser.Builders;

import com.bortbort.arduino.FiloFirmata.Messages.Message;
import com.bortbort.arduino.FiloFirmata.Messages.PinCapabilities;
import com.bortbort.arduino.FiloFirmata.Messages.SysexCapabilityResponseMessage;
import com.bortbort.arduino.FiloFirmata.Parser.SysexCommandBytes;
import com.bortbort.arduino.FiloFirmata.Parser.SysexMessageBuilder;
import com.bortbort.helpers.DataTypeHelpers;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by chuck on 1/14/2016.
 */
public class SysexCapabilityResponseBuilder extends SysexMessageBuilder {

    public SysexCapabilityResponseBuilder() {
        super(SysexCommandBytes.CAPABILITY_RESPONSE);
    }

    @Override
    public Message buildMessage(byte[] messageBody) {
        ArrayList<byte[]> pinCapabilityBytes = new ArrayList<>();
        ArrayList<byte[]> pinMessages = splitPinMessages(messageBody);

        for (byte[] pinMessage : pinMessages) {
            //  This response is padded with extra bytes that we don't really care about right now in a cadence of
            //    every other byte. Skip over every other byte.
            byte[] capabilityBytes = new byte[pinMessage.length / 2];
            int currentByteIndex = 0;
            for (int x = 0; x < pinMessage.length / 2; x++) {
                capabilityBytes[x] = pinMessage[currentByteIndex];
                currentByteIndex += 2;
            }

            pinCapabilityBytes.add(capabilityBytes);
        }

        return new SysexCapabilityResponseMessage(pinCapabilityBytes);
    }

    private ArrayList<byte[]> splitPinMessages (byte[] messageBody) {
        // Arbitrary buffer size. Each capability has an extra potentially used byte with it.
        ByteBuffer byteBuffer = ByteBuffer.allocate(PinCapabilities.values().length * 2);
        ArrayList<byte[]> pinCapabilities = new ArrayList<>();
        for (byte bodyChunk : messageBody) {
            if (bodyChunk == 0x7f) {
                byte[] pinMessageBody = Arrays.copyOf(byteBuffer.array(), byteBuffer.position());
                pinCapabilities.add(pinMessageBody);
                byteBuffer.clear();
            }
            else {
                byteBuffer.put(bodyChunk);
            }
        }
        return pinCapabilities;
    }
}
