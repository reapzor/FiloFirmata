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
    private static final HashMap<Byte, Message> messages = new HashMap<>();

    static {
        MessageRouter.registerMessageParsers(

        );
    }

    public static void registerMessageParser(Message message) {
        messages.put(message.getIdentifierByte(), message);
    }

    public static void registerMessageParsers(Message... messageList) {
        for (Message message : messageList) {
            registerMessageParser(message);
        }
    }

    protected static void handleByte(byte messageByte, InputStream inputStream) {
        Message message = messages.get(messageByte);
        if (message != null) {
            message.parseMessage(inputStream);
        }
        else {
            log.warn("Dropped byte {}", DataTypeHelpers.bytesToHexString(messageByte));
        }
    }

    private MessageRouter() {

    }
}
