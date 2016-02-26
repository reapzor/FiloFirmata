package com.bortbort.arduino.FiloFirmata;

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
        firmata = new Firmata("COM4");
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
        MessageListener<SysexCapabilityMessage> capabilityListener = new MessageListener<SysexCapabilityMessage>() {
            @Override
            public void messageReceived(SysexCapabilityMessage message) {
                assertNotNull(message.getPinCapabilities(0));
                received();
            }
        };

        firmata.addMessageListener(capabilityListener);
        assertTrue(firmata.sendMessage(new SysexCapabilityQueryMessage()));

        // Verify the listener receives the capabilities message.
        waitForCallback();

        assertTrue(firmata.sendMessage(new SysexReportFirmwareQueryMessage()));
        Thread.sleep(500);

        // Verify the listener does not get messages of a type it is not listening to.
        assertFalse(receievedCallback);

        firmata.removeMessageListener(capabilityListener);
    }



    @Test
    public void testAnalogMessage() throws Exception {
        MessageListener<AnalogMessage> analogListener = new MessageListener<AnalogMessage>() {
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
        MessageListener<DigitalPortMessage> digitalListener = new MessageListener<DigitalPortMessage>() {
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


    @Test
    public void testSynchronousCommunication() throws Exception {
        SysexCapabilityMessage message = firmata.sendMessageSynchronous(new SysexCapabilityQueryMessage());

        assertNotNull(message);
        assertNotNull(message.getPinCapabilities());
    }


    @Test
    public void testGlobalListener() throws Exception {
        MessageListener<Message> messageListener = new MessageListener<Message>() {
            @Override
            public void messageReceived(Message message) {
                assertNotNull(message);
                received();
            }
        };

        firmata.addMessageListener(messageListener);
        assertTrue(firmata.sendMessage(new SysexReportFirmwareQueryMessage()));

        // Verify we get any generic message sent up from the board
        waitForCallback();

        assertTrue(firmata.sendMessage(new ReportDigitalPortMessage(1, true)));

        // Verify we get any generic message sent up from the board (part 2)
        waitForCallback();

        assertTrue(firmata.sendMessage(new ReportDigitalPortMessage(1, false)));
        firmata.removeMessageListener(messageListener);
        reset();
        firmata.addMessageListener(1, messageListener);
        assertTrue(firmata.sendMessage(new ReportDigitalPortMessage(1, true)));

        // Verify we get any generic channel message send up from the board
        waitForCallback();

        assertTrue(firmata.sendMessage(new ReportDigitalPortMessage(2, true)));
        assertTrue( firmata.sendMessage(new SysexReportFirmwareQueryMessage()));
        Thread.sleep(500);

        // Verify we do not get channel messages sent from other areas
        assertFalse(receievedCallback);

        assertTrue(firmata.sendMessage(new ReportDigitalPortMessage(1, false)));
        assertTrue(firmata.sendMessage(new ReportDigitalPortMessage(2, false)));
        firmata.removeMessageListener(1, messageListener);
        reset();
        firmata.addMessageListener(SysexReportFirmwareMessage.class, messageListener);
        assertTrue(firmata.sendMessage(new ReportDigitalPortMessage(1, true)));
        Thread.sleep(500);

        // Verify we do not get generic messages while listening to specific messages
        assertFalse(receievedCallback);

        assertTrue(firmata.sendMessage(new ReportDigitalPortMessage(1, false)));
        assertTrue(firmata.sendMessage(new SysexReportFirmwareQueryMessage()));

        // Verify we do get specific messages routed to the generic listener when configured
        waitForCallback();

        firmata.removeMessageListener(SysexReportFirmwareMessage.class, messageListener);
    }


}