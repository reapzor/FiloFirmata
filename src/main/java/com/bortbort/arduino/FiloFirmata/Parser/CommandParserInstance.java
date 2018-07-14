package com.bortbort.arduino.FiloFirmata.Parser;

import com.bortbort.arduino.FiloFirmata.FirmataHelper;
import com.bortbort.arduino.FiloFirmata.Messages.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Firmata Command Parser.
 * Handles parsing of a Firmata command message discovered over the SerialPort.
 */
public class CommandParserInstance {

    private final Logger log;

    /**
     * Map of messageBuilders used to parse an array/inputstream of bytes into a Firmata Message object.
     */
    private Map<Byte, MessageBuilder> messageBuilderMap = new HashMap<>();

    public CommandParserInstance() {
        this(LoggerFactory.getLogger(CommandParserInstance.class));
    }

    public CommandParserInstance(Logger log) {
        this.log = log;
        this.messageBuilderMap = new HashMap<>();
    }

    public CommandParserInstance(CommandParserInstance commandParserInstance) {
        this.log = commandParserInstance.log;
        this.messageBuilderMap = new HashMap<>(commandParserInstance.messageBuilderMap);
    }

    /**
     * Add a custom parser to the Firmata library. When the command byte for the parser is received, the parser
     * will be responsible for turning the the data that follows into a Firmata message.
     *
     * @param messageBuilder MessageBuilder object that translates the byte message into a Message object.
     */
    public void addParser(MessageBuilder messageBuilder) {
        messageBuilderMap.put(messageBuilder.getCommandByte(), messageBuilder);
    }

    /**
     * Array input for addParser(MessageBuilder).
     *
     * @param messageBuilderList Array of MessageBuilder objects that translate the byte message into a Message object.
     */
    public void addParser(MessageBuilder... messageBuilderList) {
        for (MessageBuilder messageBuilder : messageBuilderList) {
            addParser(messageBuilder);
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
    public Message handleByte(byte commandByte, InputStream inputStream) {
        // Firmata command bytes are limited to a 0xF0 mask, or bytes 0xF0-0xFF
        //   The extra data in the byte is for routing the command to various 'midi channels'
        //   or in our case Firmata device pins.
        byte firmataCommandByte = commandByte < (byte) 0xF0 ? (byte) (commandByte & 0xF0) : commandByte;
        MessageBuilder messageBuilder = messageBuilderMap.get(firmataCommandByte);
        if (messageBuilder != null) {
            // Pull out the pin identifier bits that were padded into the byte (if any)
            byte channelByte = (byte) (commandByte & 0x0F);
            Message message = messageBuilder.buildMessage(channelByte, inputStream);
            if (message == null) {
                log.error("Error building Firmata messageBuilder for command byte {}.",
                        FirmataHelper.bytesToHexString(commandByte));
            } else {
                return message;
            }
        } else {
            log.warn("Dropped byte {}.", FirmataHelper.bytesToHexString(commandByte));
        }

        return null;
    }
}
