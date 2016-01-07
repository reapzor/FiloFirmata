package com.bortbort.arduino.FiloFirmata.Parser.Messages;

import com.bortbort.arduino.FiloFirmata.Messages.SysexReportFirmwareMessage;
import com.bortbort.arduino.FiloFirmata.Parser.SysexCommandBytes;
import com.bortbort.arduino.FiloFirmata.Parser.SysexMessageParser;
import com.bortbort.helpers.DataTypeHelpers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;

/**
 * Created by chuck on 1/6/2016.
 */
public class SysexReportFirmwareParser extends SysexMessageParser {
    private static final Logger log = LoggerFactory.getLogger(SysexReportFirmwareParser.class);

    public SysexReportFirmwareParser() {
        super(SysexCommandBytes.REPORT_FIRMWARE.getCommandByte());
    }

    @Override
    public Boolean buildMessage(byte[] messageBody) {
        String firmwareName;

        try {
            firmwareName = DataTypeHelpers.decodeTwoSevenBitByteString(messageBody, 2, messageBody.length-2);
        } catch (UnsupportedEncodingException e) {
            log.error("Unable to decode firmware name. Bytes: {}. E: {}.",
                    DataTypeHelpers.bytesToHexString(messageBody),
                    e.getMessage());
            e.printStackTrace();
            firmwareName = null;
        }

        SysexReportFirmwareMessage message = new SysexReportFirmwareMessage(
                messageBody[0],
                messageBody[1],
                firmwareName
        );

        return true;
    }
}
