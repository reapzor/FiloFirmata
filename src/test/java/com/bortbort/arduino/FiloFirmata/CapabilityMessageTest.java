package com.bortbort.arduino.FiloFirmata;

import com.bortbort.arduino.FiloFirmata.Listeners.SysexCapabilityResponseListener;
import com.bortbort.arduino.FiloFirmata.Messages.SysexCapabilityQueryMessage;
import com.bortbort.arduino.FiloFirmata.Messages.SysexCapabilityResponseMessage;
import org.junit.Test;

/**
 * Created by chuck on 1/13/2016.
 */
public class CapabilityMessageTest {
    Firmata firmata = new Firmata();

    @Test
    public void testCapabilityMessage() throws Exception {
        SysexCapabilityResponseListener responseListener = new SysexCapabilityResponseListener() {
            @Override
            public void messageReceived(SysexCapabilityResponseMessage message) {
                System.out.println(message.getPinCapabilities(0));
                System.out.println(message.getPinCapabilities(1));
                System.out.println(message.getPinCapabilities(2));
                System.out.println(message.getPinCapabilities(3));
                System.out.println(message.getPinCapabilities(6));
                System.out.println(message.getPinCapabilities(7));
                System.out.println(message.getPinCapabilities(8));
                System.out.println(message.getPinCapabilities(13));
                System.out.println(message.getPinCapabilities(14));
                System.out.println(message.getPinCapabilities(15));
                System.out.println(message.getPinCapabilities(17));
                System.out.println(message.getPinCapabilities(18));
                System.out.println(message.getPinCapabilities(19));
            }
        };

        firmata.addMessageListener(responseListener);
        firmata.start();

        firmata.sendMessage(new SysexCapabilityQueryMessage());

        Thread.sleep(2000);

    }
}
