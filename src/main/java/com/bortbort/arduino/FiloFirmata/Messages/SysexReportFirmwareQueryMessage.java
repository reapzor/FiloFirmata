package com.bortbort.arduino.FiloFirmata.Messages;

import com.bortbort.arduino.FiloFirmata.Parser.SysexCommandBytes;

import java.io.ByteArrayOutputStream;

/**
 * Sysex Report Firmware Message
 * Asks the Firmata device to reply with its protocol version and firmware/file title/name
 * https://github.com/firmata/protocol/blob/master/protocol.md
 */
public class SysexReportFirmwareQueryMessage extends TransmittableSysexMessage {

    public SysexReportFirmwareQueryMessage() {
        super(SysexCommandBytes.REPORT_FIRMWARE);
    }

    /**
     * When transmitting the request for a SysexReportFirmware message, we need no body data, only the command
     * to be sent, which is handled by the parent class. Treat the implementation as a no-op and return
     * no body. This will ensure the output is {0xF9 0x79 0xF7} (start_sysex, report_firmware, end_sysex).
     *
     * @return true.
     */
    @Override
    protected Boolean serialize(ByteArrayOutputStream outputStream) {
        return true;
    }

}
