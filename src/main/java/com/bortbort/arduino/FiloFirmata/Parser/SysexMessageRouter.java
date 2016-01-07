package com.bortbort.arduino.FiloFirmata.Parser;

import com.bortbort.arduino.FiloFirmata.Messages.Message;
import com.bortbort.arduino.FiloFirmata.Parser.Messages.SysexReportFirmwareParser;
import com.bortbort.helpers.DataTypeHelpers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

/**
 * Created by chuck on 1/6/2016.
 */
public class SysexMessageRouter extends MessageParser {
    private static final Logger log = LoggerFactory.getLogger(SysexMessageRouter.class);
    private static HashMap<Byte, SysexMessageParser> sysexMessageParsers = new HashMap<>();

    static {
        SysexMessageRouter.addParser(
            new SysexReportFirmwareParser()
        );
    }

    public static void addParser(SysexMessageParser sysexMessageParser) {
        sysexMessageParsers.put(sysexMessageParser.getCommandByte(), sysexMessageParser);
    }

    public static void addParser(SysexMessageParser... sysexMessageParsers) {
        for (SysexMessageParser sysexMessageParser : sysexMessageParsers) {
            addParser(sysexMessageParser);
        }
    }

    public SysexMessageRouter() {
        super(CommandBytes.START_SYSEX);
    }

    @Override
    public Message buildMessage(InputStream inputStream) {
        byte commandByte;
        try {
            commandByte = (byte) inputStream.read();
        } catch (IOException e) {
            log.error("Error reading Sysex command byte.");
            return null;
        }

        ByteArrayOutputStream messageBodyBuilder = new ByteArrayOutputStream();
        try {
            int messagePiece;
            while ((messagePiece = inputStream.read()) != -1) {
                if ((byte) messagePiece == CommandBytes.END_SYSEX.getCommandByte()) {
                    break;
                }
                messageBodyBuilder.write(messagePiece);
            }
        } catch (IOException e) {
            log.error("Error reading Sysex message body for command {}.",
                    DataTypeHelpers.bytesToHexString(commandByte));
            return null;
        }

        byte[] messageBodyBytes = messageBodyBuilder.toByteArray();

        try {
            messageBodyBuilder.close();
        } catch (IOException e) {
            log.error("Programming error. Cannot close our byte buffer.");
        }

        SysexMessageParser messageParser = sysexMessageParsers.get(commandByte);
        if (messageParser == null) {
            log.error("There is no Sysex message parser registered for command {}. Body: {}",
                    DataTypeHelpers.bytesToHexString(commandByte),
                    DataTypeHelpers.bytesToHexString(messageBodyBuilder.toByteArray()));
            return null;
        }

        return messageParser.buildMessage(messageBodyBytes);
    }

}
