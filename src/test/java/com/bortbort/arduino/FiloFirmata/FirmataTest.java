package com.bortbort.arduino.FiloFirmata;

import com.bortbort.arduino.FiloFirmata.Listeners.SysexReportFirmwareListener;
import com.bortbort.arduino.FiloFirmata.Messages.SysexReportFirmwareMessage;
import org.junit.Test;

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

        firmata.addMessageListener(firmwareListener);
        firmata.start();
        // Report Firmware
        firmata.sendMessage(new SysexReportFirmwareMessage());

        Thread.sleep(2000);
        firmata.stop();
    }


}