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
public class MessageRouter {
    private static final Logger log = LoggerFactory.getLogger(MessageRouter.class);
    private static final HashMap<Byte, MessageParser> messageParsers = new HashMap<>();

    static {
        MessageRouter.addParser(
                new SysexMessageRouter()
        );
    }

    public static void addParser(MessageParser messageParser) {
        messageParsers.put(messageParser.getCommandByte(), messageParser);
    }

    public static void addParser(MessageParser... messageParserList) {
        for (MessageParser messageParser : messageParserList) {
            addParser(messageParser);
        }
    }

    public static Message handleByte(byte commandByte, InputStream inputStream) {
        MessageParser messageParser = messageParsers.get(commandByte);
        if (messageParser != null) {
            Message message = messageParser.buildMessage(inputStream);
            if (message == null) {
                log.error("Error building Firmata messageParser for command byte {}.",
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

    private MessageRouter() {

    }
}
