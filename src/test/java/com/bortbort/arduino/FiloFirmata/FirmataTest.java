package com.bortbort.arduino.FiloFirmata;

import com.bortbort.arduino.FiloFirmata.Listeners.SysexReportFirmwareListener;
import com.bortbort.arduino.FiloFirmata.Messages.SysexReportFirmwareMessage;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by chuck on 1/4/2016.
 */
public class FirmataTest {
    Firmata firmata = new Firmata();

    @Test
    public void testFirmataInitializationAndShutdown() throws Exception {
        SysexReportFirmwareListener firmwareListener = new SysexReportFirmwareListener() {
            @Override
            public void messageReceived(SysexReportFirmwareMessage message) {
                System.out.println(message.getFirmwareName());
            }
        };

        firmata.getMessageDispatcher().addMessageListener(firmwareListener);
        firmata.start();
        // Report Firmware
        firmata.getSerialPort().getOutputStream().write(new byte[] {(byte) 0xf0, (byte) 0x79, (byte) 0xf7});


        Thread.sleep(20000);
        firmata.stop();
    }


}