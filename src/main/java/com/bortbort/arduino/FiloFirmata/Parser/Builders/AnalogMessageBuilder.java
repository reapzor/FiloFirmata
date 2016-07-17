package com.bortbort.arduino.FiloFirmata.Parser.Builders;

import com.bortbort.arduino.FiloFirmata.FirmataHelper;
import com.bortbort.arduino.FiloFirmata.Messages.AnalogMessage;
import com.bortbort.arduino.FiloFirmata.Messages.Message;
import com.bortbort.arduino.FiloFirmata.Parser.CommandBytes;
import com.bortbort.arduino.FiloFirmata.Parser.MessageBuilder;
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
            if (FirmataHelper.fastReadBytesWithTimeout(inputStream, valueByteBody, 2000)) {
                byte valueByte = FirmataHelper.decodeTwoSevenBitByteSequence(valueByteBody[0], valueByteBody[1]);
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
