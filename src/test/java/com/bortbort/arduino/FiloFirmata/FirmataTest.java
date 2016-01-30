package com.bortbort.arduino.FiloFirmata;

import com.bortbort.arduino.FiloFirmata.Listeners.AnalogListener;
import com.bortbort.arduino.FiloFirmata.Listeners.DigitalPortListener;
import com.bortbort.arduino.FiloFirmata.Listeners.SysexCapabilityListener;
import com.bortbort.arduino.FiloFirmata.Messages.*;

import static com.jayway.awaitility.Awaitility.*;
import static java.util.concurrent.TimeUnit.*;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by chuck on 1/4/2016.
 */
public class FirmataTest {
    Firmata firmata;
    private Boolean receievedCallback = false;

    private void received() {
        receievedCallback = true;
    }

    private void reset() {
        receievedCallback = false;
    }

    private void waitForCallback() {
        await().atMost(1, SECONDS).until(() -> receievedCallback);
        reset();
    }



    @Before
    public void before() throws Exception {
        firmata = new Firmata();
        assertTrue(firmata.start());
        assertTrue(firmata.sendMessage(new SystemResetMessage()));
    }

    @After
    public void after() {
        firmata.removeAllListeners();
        firmata.stop();
        reset();
    }



    @Test
    public void testCapabilityMessage() throws Exception {
        SysexCapabilityListener capabilityListener = new SysexCapabilityListener() {
            @Override
            public void messageReceived(SysexCapabilityMessage message) {
                assertNotNull(message.getPinCapabilities(0));
                received();
            }
        };

        firmata.addMessageListener(capabilityListener);

        assertTrue(firmata.sendMessage(new SysexCapabilityQueryMessage()));

        waitForCallback();

        firmata.removeMessageListener(capabilityListener);
    }



    @Test
    public void testAnalogMessage() throws Exception {
        AnalogListener analogListener = new AnalogListener() {
            @Override
            public void messageReceived(AnalogMessage message) {
                assertTrue(message.getAnalogValue() >= 0);
                received();
            }
        };

        firmata.addMessageListener(analogListener);

        assertTrue(firmata.sendMessage(new ReportAnalogPinMessage(2, true)));

        // Verify we get a message response to channel global listening
        waitForCallback();

        firmata.removeMessageListener(analogListener);
        reset();
        firmata.addMessageListener(2, analogListener);

        // Verify we get a message response to specific channel listening
        waitForCallback();

        firmata.removeMessageListener(2, analogListener);
        reset();
        firmata.addMessageListener(3, analogListener);

        Thread.sleep(500);

        // Verify we do NOT get a message response to listening to 'wrong/inactive' channel
        assertFalse(receievedCallback);

        firmata.removeMessageListener(3, analogListener);

        assertTrue(firmata.sendMessage(new ReportAnalogPinMessage(2, false)));
    }

    @Test
    public void testDigitalMessage() throws Exception {
        DigitalPortListener digitalListener = new DigitalPortListener() {
            @Override
            public void messageReceived(DigitalPortMessage message) {
                assertTrue(message.getChannelInt() >= 0);
                received();
            }
        };

        firmata.addMessageListener(digitalListener);

        assertTrue(firmata.sendMessage(new ReportDigitalPortMessage(1, true)));

        // Verify we get a message response to channel global listening
        waitForCallback();

        firmata.removeMessageListener(digitalListener);
        reset();
        firmata.addMessageListener(1, digitalListener);

        assertTrue(firmata.sendMessage(new ReportDigitalPortMessage(1, true)));

        // Verify we get a message response to specific channel listening
        waitForCallback();

        firmata.removeMessageListener(1, digitalListener);
        reset();
        firmata.addMessageListener(2, digitalListener);

        assertTrue(firmata.sendMessage(new ReportDigitalPortMessage(1, true)));

        Thread.sleep(500);

        // Verify we do NOT get a message response to listening to 'wrong/inactive' channel
        assertFalse(receievedCallback);

        firmata.removeMessageListener(2, digitalListener);

        assertTrue(firmata.sendMessage(new ReportDigitalPortMessage(1, false)));
    }


}