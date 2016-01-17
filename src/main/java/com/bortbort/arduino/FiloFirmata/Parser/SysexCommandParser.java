package com.bortbort.arduino.FiloFirmata.Parser;

import com.bortbort.arduino.FiloFirmata.Messages.Message;
import com.bortbort.arduino.FiloFirmata.Parser.Builders.SysexCapabilityResponseBuilder;
import com.bortbort.arduino.FiloFirmata.Parser.Builders.SysexPinStateResponseBuilder;
import com.bortbort.arduino.FiloFirmata.Parser.Builders.SysexReportFirmwareBuilder;
import com.bortbort.arduino.FiloFirmata.Parser.Builders.SysexStringDataBuilder;
import com.bortbort.helpers.DataTypeHelpers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

/**
 * Firmata Sysex Command Parser.
 * Handles parsing of a Firmata sysex command message discovered over the SerialPort.
 * This is essentially a re-implementation of CommandParser, with one extra layer to handle
 * the padded START_SYSEX and END_SYSEX bytes.
 */
public class SysexCommandParser extends MessageBuilder {
    private static final Logger log = LoggerFactory.getLogger(SysexCommandParser.class);

    /**
     * Map of sysexMessageBuilders used to parse an array/inputstream of bytes into a Firmata Sysex Message object.
     */
    private static HashMap<Byte, SysexMessageBuilder> messageBuilderMap = new HashMap<>();

    /**
     * Register all pre-built Message builder objects available in the Firmata library.
     */
    static {
        SysexCommandParser.addParser(
                // Support Sysex Report Firmware
                new SysexReportFirmwareBuilder(),
                // Support Sysex String Data
                new SysexStringDataBuilder(),
                // Support Sysex Capability Response
                new SysexCapabilityResponseBuilder(),
                // Support Sysex Pin State Response
                new SysexPinStateResponseBuilder()
        );
    }

    /**
     * Add a custom sysex parser to the Firmata library. When the sysex command byte for the parser is received,
     * the parser will be responsible for turning the the data that follows into a Firmata sysex message.
     *
     * @param sysexMessageBuilder SysexMessageBuilder object that translates the byte message into
     *                            a sysex Message object.
     */
    public static void addParser(SysexMessageBuilder sysexMessageBuilder) {
        messageBuilderMap.put(sysexMessageBuilder.getCommandByte(), sysexMessageBuilder);
    }

    /**
     * Array input for addParser(SysexMessageBuilder).
     *
     * @param sysexMessageBuilders Array of SysexMessageBuilder objects that translate
     *                             the byte message into a sysex Message object.
     */
    public static void addParser(SysexMessageBuilder... sysexMessageBuilders) {
        for (SysexMessageBuilder sysexMessageBuilder : sysexMessageBuilders) {
            addParser(sysexMessageBuilder);
        }
    }

    /**
     * Implement the MessageBuilder object using the START_SYSEX command byte to handle all sysex messaged passed
     * from the Firmata SerialPort.
     */
    public SysexCommandParser() {
        super(CommandBytes.START_SYSEX);
    }

    /**
     * Attempts to identify the corresponding Firmata Sysex Message object that responds to the
     * identified sysexCommandByte. If found, it will ask for the sysex message to be built using
     * the identified bytes between the sysexCommandByte and the END_SYSEX commandByte later in the input stream.
     *
     * @param channelByte channelByte indicating the pin/port to perform the command against.
     * @param inputStream InputStream representing the data following the START_SYSEX command byte.
     * @return Message representing the Firmata Sysex Message that the SerialPort communications device sent.
     */
    @Override
    public Message buildMessage(Byte channelByte, InputStream inputStream) {
        byte sysexCommandByte;
        try {
            sysexCommandByte = (byte) inputStream.read();
        } catch (IOException e) {
            log.error("Error reading Sysex command byte.");
            return null;
        }

        ByteArrayOutputStream messageBodyBuilder = new ByteArrayOutputStream(16);
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
                    DataTypeHelpers.bytesToHexString(sysexCommandByte));
            return null;
        }

        byte[] messageBodyBytes = messageBodyBuilder.toByteArray();

        try {
            messageBodyBuilder.close();
        } catch (IOException e) {
            log.error("Programming error. Cannot close our byte buffer.");
        }

        SysexMessageBuilder messageBuilder = messageBuilderMap.get(sysexCommandByte);
        if (messageBuilder == null) {
            log.error("There is no Sysex message parser registered for command {}. Body: {}",
                    DataTypeHelpers.bytesToHexString(sysexCommandByte),
                    DataTypeHelpers.bytesToHexString(messageBodyBuilder.toByteArray()));
            return null;
        }

        log.info(DataTypeHelpers.bytesToHexString(messageBodyBytes));

        return messageBuilder.buildMessage(messageBodyBytes);
    }

}
