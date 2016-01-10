package com.bortbort.arduino.FiloFirmata.Parser.Builders;

import com.bortbort.arduino.FiloFirmata.Messages.Message;
import com.bortbort.arduino.FiloFirmata.Messages.SysexStringDataMessage;
import com.bortbort.arduino.FiloFirmata.Parser.SysexCommandBytes;
import com.bortbort.arduino.FiloFirmata.Parser.SysexMessageBuilder;
import com.bortbort.helpers.DataTypeHelpers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.UnsupportedEncodingException;

/**
 * Created by chuck on 1/9/2016.
 */
public class SysexStringDataBuilder extends SysexMessageBuilder {
    private static final Logger log = LoggerFactory.getLogger(SysexStringDataBuilder.class);

    public SysexStringDataBuilder() {
        super(SysexCommandBytes.STRING_DATA);
    }

    @Override
    public Message buildMessage(byte[] messageBody) {
        try {
            String stringData = DataTypeHelpers.decodeTwoSevenBitByteString(messageBody);

            return new SysexStringDataMessage(stringData);
        } catch (UnsupportedEncodingException e) {
            log.error("Unable to convert 'two seven bit byte' array to String with bytes {}",
                    DataTypeHelpers.bytesToHexString(messageBody));
        }

        return null;
    }
}
