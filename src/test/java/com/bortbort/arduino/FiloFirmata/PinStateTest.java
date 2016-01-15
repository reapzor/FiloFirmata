package com.bortbort.arduino.FiloFirmata;

import com.bortbort.arduino.FiloFirmata.Listeners.SysexCapabilityResponseListener;
import com.bortbort.arduino.FiloFirmata.Listeners.SysexPinStateResponseListener;
import com.bortbort.arduino.FiloFirmata.Messages.*;
import org.junit.Test;

import java.util.ArrayList;

/**
 * Created by chuck on 1/14/2016.
 */
public class PinStateTest {
    Firmata firmata = new Firmata();

    @Test
    public void testCapabilityMessage() throws Exception {
        SysexPinStateResponseListener responseListener = new SysexPinStateResponseListener() {
            @Override
            public void messageReceived(SysexPinStateResponseMessage message) {
                System.out.println(message.getPinIdentifier());
                System.out.println(message.getCurrentPinMode());
                System.out.println(message.getPinValue());
            }
        };

        firmata.addMessageListener(responseListener);
        firmata.start();

        firmata.sendMessage(new SysexPinStateQueryMessage(19));

        Thread.sleep(2000);

    }
}
