package com.bortbort.arduino.FiloFirmata.Parser.Builders;

import com.bortbort.arduino.FiloFirmata.Messages.AnalogMessage;
import com.bortbort.arduino.FiloFirmata.Messages.DigitalPortMessage;
import com.bortbort.arduino.FiloFirmata.Messages.Message;
import com.bortbort.arduino.FiloFirmata.Parser.CommandBytes;
import com.bortbort.arduino.FiloFirmata.Parser.MessageBuilder;
import com.bortbort.helpers.DataTypeHelpers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by chuck on 1/11/2016.
 */
public class DigitalPortBuilder extends MessageBuilder {
    private static final Logger log = LoggerFactory.getLogger(DigitalPortBuilder.class);

    public DigitalPortBuilder() {
        super(CommandBytes.DIGITAL_MESSAGE);
    }

    @Override
    public Message buildMessage(Byte channelByte, InputStream inputStream) {
        try {
            byte[] valueByteBody = new byte[2];
            if (inputStream.read(valueByteBody, 0, 2) != -1) {
                byte valueByte = DataTypeHelpers.decodeTwoSevenBitByteSequence(valueByteBody[0], valueByteBody[1]);
                int pin = channelByte * 3;
                ArrayList<Integer> pinValues = new ArrayList<>();
                HashMap<Integer, Integer> pinMappedValues = new HashMap<>();
                for (int x = 0; x < 8; x++) {
                    int pinValue = ((valueByte >>> x) & 0x01);
                    pinValues.add(pinValue);
                    pinMappedValues.put(pin + x, pinValue);
                }
                return new DigitalPortMessage(channelByte, pinValues, pinMappedValues, valueByte);
            }
            else {
                log.error("Unable to read digital port message value for channel {}", channelByte);
            }
        } catch (IOException e) {
            log.error("Error reading from serial port. Unable to read digital port message.");
        }

        return null;
    }
}
