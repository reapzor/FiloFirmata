package com.bortbort.arduino.FiloFirmata.Parser.Builders;

import com.bortbort.arduino.FiloFirmata.Messages.AnalogMessage;
import com.bortbort.arduino.FiloFirmata.Messages.Message;
import com.bortbort.arduino.FiloFirmata.Parser.CommandBytes;
import com.bortbort.arduino.FiloFirmata.Parser.MessageBuilder;
import com.bortbort.helpers.DataTypeHelpers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by chuck on 1/11/2016.
 */
public class AnalogMessageBuilder extends MessageBuilder {
    private static final Logger log = LoggerFactory.getLogger(AnalogMessageBuilder.class);

    public AnalogMessageBuilder() {
        super(CommandBytes.ANALOG_MESSAGE);
    }

    @Override
    public Message buildMessage(Byte channelByte, InputStream inputStream) {
        try {
            byte[] valueByteBody = new byte[2];
            if (inputStream.read(valueByteBody, 0, 2) != -1) {
                byte valueByte = DataTypeHelpers.decodeTwoSevenBitByteSequence(valueByteBody[0], valueByteBody[1]);
                return new AnalogMessage(channelByte, valueByte);
            }
            else {
                log.error("Unable to read analog message value for channel {}", channelByte);
            }
        } catch (IOException e) {
            log.error("Error reading from serial port. Unable to read analog message.");
        }

        return null;
    }
}
