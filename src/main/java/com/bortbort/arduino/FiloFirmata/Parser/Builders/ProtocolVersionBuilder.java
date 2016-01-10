package com.bortbort.arduino.FiloFirmata.Parser.Builders;

import com.bortbort.arduino.FiloFirmata.Messages.Message;
import com.bortbort.arduino.FiloFirmata.Messages.ProtocolVersionMessage;
import com.bortbort.arduino.FiloFirmata.Parser.CommandBytes;
import com.bortbort.arduino.FiloFirmata.Parser.MessageBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by chuck on 1/9/2016.
 */
public class ProtocolVersionBuilder extends MessageBuilder {
    private static final Logger log = LoggerFactory.getLogger(ProtocolVersionBuilder.class);

    public ProtocolVersionBuilder() {
        super(CommandBytes.PROTOCOL_VERSION);
    }

    @Override
    public Message buildMessage(InputStream inputStream) {
        byte[] versionBytes = new byte[2];

        try {
            int bytesRead = inputStream.read(versionBytes, 0, 2);

            if (bytesRead != 2) {
                log.error("Unable to parse Protocol Version command from SerialPort.");
                return null;
            }

            return new ProtocolVersionMessage((int) versionBytes[0], (int) versionBytes[1]);
        } catch (IOException e) {
            log.error("Unable to read Serial Port.");
        }

        return null;
    }
}
