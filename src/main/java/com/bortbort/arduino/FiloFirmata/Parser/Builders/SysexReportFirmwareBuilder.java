package com.bortbort.arduino.FiloFirmata.Parser.Builders;

import com.bortbort.arduino.FiloFirmata.Messages.Message;
import com.bortbort.arduino.FiloFirmata.Messages.SysexReportFirmwareMessage;
import com.bortbort.arduino.FiloFirmata.Parser.SysexCommandBytes;
import com.bortbort.arduino.FiloFirmata.Parser.SysexMessageBuilder;
import com.bortbort.helpers.DataTypeHelpers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;

/**
 * SysexReportFirmwareBuilder to build a SysexReportFirmwareMessage packet.
 * https://github.com/firmata/protocol/blob/master/protocol.md
 */
public class SysexReportFirmwareBuilder extends SysexMessageBuilder {
    private static final Logger log = LoggerFactory.getLogger(SysexReportFirmwareBuilder.class);

    /**
     * Register the SysexMessageBuilder to handle REPORT_FIRMWARE commands from the SerialPort.
     */
    public SysexReportFirmwareBuilder() {
        super(SysexCommandBytes.REPORT_FIRMWARE);
    }

    /**
     * Build a SysexReportFirmwareMessage from the given byte array.
     * The protocol dictates the packet should be byte 0 = Major version int, byte 1 = Minor version int,
     * bytes 2-n = Firmware name in ASCII (Firmata 'TwoSevenBitBytes' format).
     *
     * @param messageBody byte array representing the SysexReportFirmwareMessage response from the SerialPort.
     * @return SysexReportFirmwareMessage built from the byte array.
     */
    @Override
    public Message buildMessage(byte[] messageBody) {
        String firmwareName;

        // Error handling
        if (messageBody.length < 2) {
            log.error("Invalid message body length for SysexReportFirmware. Length: {}.", messageBody.length);
            return null;
        }

        if (messageBody.length == 2) {
            // No name was given
            firmwareName = "";
        }
        else {
            try {
                firmwareName = DataTypeHelpers.decodeTwoSevenBitByteString(messageBody, 2, messageBody.length - 2);
            } catch (UnsupportedEncodingException e) {
                log.error("Unable to decode firmware name. Bytes: {}. E: {}.",
                        DataTypeHelpers.bytesToHexString(messageBody),
                        e.getMessage());
                e.printStackTrace();
                firmwareName = null;
            }
        }

        return new SysexReportFirmwareMessage(
                (int) messageBody[0],
                (int) messageBody[1],
                firmwareName
        );
    }
}
