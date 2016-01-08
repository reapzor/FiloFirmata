package com.bortbort.arduino.FiloFirmata.Parser;

import com.bortbort.arduino.FiloFirmata.Messages.Message;
import com.bortbort.helpers.DataTypeHelpers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.InputStream;
import java.util.HashMap;

/**
 * Created by chuck on 1/5/2016.
 */
public class CommandParser {
    private static final Logger log = LoggerFactory.getLogger(CommandParser.class);
    private static final HashMap<Byte, MessageBuilder> messageBuilderMap = new HashMap<>();

    static {
        CommandParser.addParser(
                new SysexCommandParser()
        );
    }

    public static void addParser(MessageBuilder messageBuilder) {
        messageBuilderMap.put(messageBuilder.getCommandByte(), messageBuilder);
    }

    public static void addParser(MessageBuilder... messageBuilderList) {
        for (MessageBuilder messageBuilder : messageBuilderList) {
            addParser(messageBuilder);
        }
    }

    public static Message handleByte(byte commandByte, InputStream inputStream) {
        MessageBuilder messageBuilder = messageBuilderMap.get(commandByte);
        if (messageBuilder != null) {
            Message message = messageBuilder.buildMessage(inputStream);
            if (message == null) {
                log.error("Error building Firmata messageBuilder for command byte {}.",
                        DataTypeHelpers.bytesToHexString(commandByte));
            }
            else {
                return message;
            }
        }
        else {
            log.warn("Dropped byte {}.", DataTypeHelpers.bytesToHexString(commandByte));
        }

        return null;
    }

    private CommandParser() {

    }
}
