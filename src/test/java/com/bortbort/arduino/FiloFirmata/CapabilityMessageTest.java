package com.bortbort.arduino.FiloFirmata;

import com.bortbort.arduino.FiloFirmata.Listeners.SysexCapabilityResponseListener;
import com.bortbort.arduino.FiloFirmata.Messages.PinCapabilities;
import com.bortbort.arduino.FiloFirmata.Messages.SysexCapabilityQueryMessage;
import com.bortbort.arduino.FiloFirmata.Messages.SysexCapabilityMessage;
import org.junit.Test;

import java.util.ArrayList;

/**
 * Created by chuck on 1/13/2016.
 */
public class CapabilityMessageTest {
    Firmata firmata = new Firmata();

    @Test
    public void testCapabilityMessage() throws Exception {
        SysexCapabilityResponseListener responseListener = new SysexCapabilityResponseListener() {
            @Override
            public void messageReceived(SysexCapabilityMessage message) {
                for (ArrayList<PinCapabilities> pinCapabilities : message.getPinCapabilities()) {
                    System.out.println(pinCapabilities);
                }
            }
        };

        firmata.addMessageListener(responseListener);
        firmata.start();

        firmata.sendMessage(new SysexCapabilityQueryMessage());

        Thread.sleep(2000);

    }
}
