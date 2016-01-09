package com.bortbort.arduino.FiloFirmata.Parser;

import com.bortbort.arduino.FiloFirmata.Messages.Message;
import com.bortbort.arduino.FiloFirmata.Parser.Builders.SysexReportFirmwareBuilder;
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
public class SysexCommandParser extends MessageBuilder {
    private static final Logger log = LoggerFactory.getLogger(SysexCommandParser.class);
    private static HashMap<Byte, SysexMessageBuilder> messageBuilderMap = new HashMap<>();

    static {
        SysexCommandParser.addParser(
            new SysexReportFirmwareBuilder()
        );
    }

    public static void addParser(SysexMessageBuilder sysexMessageBuilder) {
        messageBuilderMap.put(sysexMessageBuilder.getCommandByte(), sysexMessageBuilder);
    }

    public static void addParser(SysexMessageBuilder... sysexMessageBuilders) {
        for (SysexMessageBuilder sysexMessageBuilder : sysexMessageBuilders) {
            addParser(sysexMessageBuilder);
        }
    }

    public SysexCommandParser() {
        super(CommandBytes.START_SYSEX.getCommandByte());
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

        SysexMessageBuilder messageBuilder = messageBuilderMap.get(commandByte);
        if (messageBuilder == null) {
            log.error("There is no Sysex message parser registered for command {}. Body: {}",
                    DataTypeHelpers.bytesToHexString(commandByte),
                    DataTypeHelpers.bytesToHexString(messageBodyBuilder.toByteArray()));
            return null;
        }

        return messageBuilder.buildMessage(messageBodyBytes);
    }

}
