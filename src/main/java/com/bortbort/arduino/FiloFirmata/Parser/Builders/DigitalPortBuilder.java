package com.bortbort.arduino.FiloFirmata.Parser.Builders;

import com.bortbort.arduino.FiloFirmata.DigitalPinValue;
import com.bortbort.arduino.FiloFirmata.Messages.AnalogMessage;
import com.bortbort.arduino.FiloFirmata.Messages.DigitalPortMessage;
import com.bortbort.arduino.FiloFirmata.Messages.Message;
import com.bortbort.arduino.FiloFirmata.Parser.CommandBytes;
import com.bortbort.arduino.FiloFirmata.Parser.MessageBuilder;
import com.bortbort.helpers.DataTypeHelpers;
import com.bortbort.helpers.InputStreamHelpers;
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
            if (InputStreamHelpers.fastReadBytesWithTimeout(inputStream, valueByteBody, 2000)) {
                byte valueByte = DataTypeHelpers.decodeTwoSevenBitByteSequence(valueByteBody[0], valueByteBody[1]);
                int pin = channelByte * 8;
                HashMap<Integer, Integer> pinValues = new HashMap<>(8);
                HashMap<Integer, DigitalPinValue> digitalPinValues = new HashMap<>(8);
                for (int x = 0; x < 8; x++) {
                    int pinValue = ((valueByte >>> x) & 0x01);
                    int pinID = pin + x;
                    pinValues.put(pinID, pinValue);
                    digitalPinValues.put(pinID, DigitalPinValue.valueFromInt(pinValue));
                }
                return new DigitalPortMessage(channelByte, pinValues, digitalPinValues, valueByte);
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
