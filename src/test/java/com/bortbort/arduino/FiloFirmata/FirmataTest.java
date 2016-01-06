package com.bortbort.arduino.FiloFirmata;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by chuck on 1/4/2016.
 */
public class FirmataTest {
    Firmata firmata = new Firmata();

    @Test
    public void testFirmataInitializationAndShutdown() throws Exception {
        firmata.start();
        // Report Firmware
        firmata.getSerialPort().getOutputStream().write(new byte[] {(byte) 0xf0, (byte) 0x79, (byte) 0xf7});
        Thread.sleep(1000);
        firmata.stop();
    }


}