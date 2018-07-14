package com.bortbort.arduino.FiloFirmata.Parser;

import com.bortbort.arduino.FiloFirmata.Messages.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.HashMap;

/**
 * Firmata Command Parser.
 * Handles parsing of a Firmata command message discovered over the SerialPort.
 */
public class CommandParser {
    private static final Logger log = LoggerFactory.getLogger(CommandParser.class);

    public static final CommandParserInstance DEFAULT_INSTANCE = new CommandParserInstance(log, new HashMap<>());
    static {
        DEFAULT_INSTANCE.addParser(SysexCommandParser.DEFAULT_INSTANCE);
    }

    /**
     * Add a custom parser to the Firmata library. When the command byte for the parser is received, the parser
     * will be responsible for turning the the data that follows into a Firmata message.
     *
     * @param messageBuilder MessageBuilder object that translates the byte message into a Message object.
     */
    public static void addParser(MessageBuilder messageBuilder) {
        DEFAULT_INSTANCE.addParser(messageBuilder);
    }

    /**
     * Array input for addParser(MessageBuilder).
     *
     * @param messageBuilderList Array of MessageBuilder objects that translate the byte message into a Message object.
     */
    public static void addParser(MessageBuilder... messageBuilderList) {
        for (MessageBuilder messageBuilder : messageBuilderList) {
            DEFAULT_INSTANCE.addParser(messageBuilder);
        }
    }

    /**
     * Attempts to identify the corresponding Firmata Message object that responds to the given commandByte
     * If found, it will ask for the message to be built using given inputStream. The builder will take away
     * any bytes necessary to build the message and then return. To attempt to identify the correct message
     * we must mask the incoming byte against 0xF0 if the byte is less than 0xF0, as commands below 0xF0 are
     * only legal within this mask, due to potential data padding in the same byte, per the Firmata spec.
     *
     * @param commandByte CommandByte representing the identify of a specific command Message packet.
     * @param inputStream InputStream representing the data following the command byte.
     * @return Message representing the Firmata Message that the SerialPort communications device sent.
     */
    public static Message handleByte(byte commandByte, InputStream inputStream) {
        return DEFAULT_INSTANCE.handleByte(commandByte, inputStream);
    }

    /**
     * CommandParser is a singleton object.
     */
    private CommandParser() {}
}
