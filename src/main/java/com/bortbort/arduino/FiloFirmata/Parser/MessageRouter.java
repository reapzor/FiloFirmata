package com.bortbort.arduino.FiloFirmata.Parser;

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
    private static final HashMap<Byte, Message> messageParsers = new HashMap<>();

    static {
        MessageRouter.registerParser(

        );
    }

    public static void registerParser(Message message) {
        messageParsers.put(message.getIdentifierByte(), message);
    }

    public static void registerParser(Message... messageList) {
        for (Message message : messageList) {
            registerParser(message);
        }
    }

    protected static void handleByte(byte messageByte, InputStream inputStream) {
        Message message = messageParsers.get(messageByte);
        if (message != null) {
            message.buildMessage(inputStream);
        }
        else {
            log.warn("Dropped byte {}", DataTypeHelpers.bytesToHexString(messageByte));
        }
    }

    private MessageRouter() {

    }
}
