package com.bortbort.arduino.FiloFirmata.Parser;

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
        SysexMessageRouter.registerParsers(
            new SysexReportFirmwareParser()
        );
    }

    public static void registerParser(SysexMessageParser sysexMessageParser) {
        sysexMessageParsers.put(sysexMessageParser.getCommandByte(), sysexMessageParser);
    }

    public static void registerParsers(SysexMessageParser... sysexMessageParsers) {
        for (SysexMessageParser sysexMessageParser : sysexMessageParsers) {
            registerParser(sysexMessageParser);
        }
    }

    public SysexMessageRouter() {
        super(CommandBytes.START_SYSEX);
    }

    @Override
    public Boolean buildMessage(InputStream inputStream) {
        ByteArrayOutputStream messageBodyBuilder = new ByteArrayOutputStream();
        byte commandByte;
        byte[] messageBodyBytes;

        try {
            commandByte = (byte) inputStream.read();
        } catch (IOException e) {
            log.error("Error reading Sysex command byte.");
            return false;
        }

        try {
            int messagePiece;
            while ((messagePiece = inputStream.read()) != -1) {
                if ((byte) messagePiece == CommandBytes.END_SYSEX.getCommandByte()) {
                    break;
                }
                messageBodyBuilder.write(messagePiece);
            }
        } catch (IOException e) {
            log.error("Error reading Sysex message body.");
            return false;
        }

        messageBodyBytes = messageBodyBuilder.toByteArray();
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
            return false;
        }
        else {
            return messageParser.buildMessage(messageBodyBytes);
        }
    }

}
