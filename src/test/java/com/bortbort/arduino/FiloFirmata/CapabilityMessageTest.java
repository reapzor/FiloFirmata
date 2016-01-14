package com.bortbort.arduino.FiloFirmata;

import com.bortbort.arduino.FiloFirmata.Messages.SysexCapabilityQueryMessage;
import org.junit.Test;

/**
 * Created by chuck on 1/13/2016.
 */
public class CapabilityMessageTest {
    Firmata firmata = new Firmata();

    @Test
    public void testCapabilityMessage() throws Exception {
        firmata.start();

        firmata.sendMessage(new SysexCapabilityQueryMessage());

        Thread.sleep(2000);

    }
}
