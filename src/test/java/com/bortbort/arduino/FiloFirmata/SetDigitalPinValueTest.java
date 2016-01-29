package com.bortbort.arduino.FiloFirmata;

import com.bortbort.arduino.FiloFirmata.Listeners.DigitalPortMessageListener;
import com.bortbort.arduino.FiloFirmata.Listeners.SysexPinStateResponseListener;
import com.bortbort.arduino.FiloFirmata.Messages.*;
import org.junit.Test;

/**
 * Created by chuck on 1/16/2016.
 */
public class SetDigitalPinValueTest {

//    @Test
//    public void testSetDigitalPinValueMessage() throws Exception {
//        Firmata firmata = new Firmata();
//        SysexPinStateResponseListener pinStateListener = new SysexPinStateResponseListener() {
//            @Override
//            public void messageReceived(SysexPinStateMessage message) {
//                System.out.println(message.getPinIdentifier());
//                System.out.println(message.getCurrentPinMode());
//                System.out.println(message.getPinValue());
//            }
//        };
//
//        DigitalPortMessageListener digitalPortListener = new DigitalPortMessageListener() {
//            @Override
//            public void messageReceived(DigitalPortMessage message) {
//                System.out.println(message.getChannelByte());
//                System.out.println(message.getPinMappedValues());
//                System.out.println(message.getPortByte());
//            }
//        };
//
//        firmata.addMessageListener(pinStateListener);
//        firmata.addMessageListener(digitalPortListener);
//        firmata.start();
//
//        firmata.sendMessage(new SetPinModeMessage(13, PinCapabilities.OUTPUT));
//        firmata.sendMessage(new SetDigitalPinValueMessage(13, DigitalPinValues.HIGH));
//
//        Thread.sleep(2000);
//
//        firmata.sendMessage(new SysexPinStateQueryMessage(13));
//        firmata.sendMessage(new ReportDigitalPortMessage(1, true));
//
//        Thread.sleep(2000);
//
//        firmata.sendMessage(new ReportDigitalPortMessage(1, false));
//        firmata.sendMessage(new SetDigitalPinValueMessage(13, DigitalPinValues.LOW));
//        firmata.stop();
//
//    }

    @Test
    public void testReadDigitalPinValueMessage() throws Exception {
        Firmata firmata = new Firmata();
        SysexPinStateResponseListener pinStateListener = new SysexPinStateResponseListener() {
            @Override
            public void messageReceived(SysexPinStateMessage message) {
                System.out.println(message.getPinIdentifier());
                System.out.println(message.getCurrentPinMode());
                System.out.println(message.getPinValue());
            }
        };

        DigitalPortMessageListener digitalPortListener = new DigitalPortMessageListener() {
            @Override
            public void messageReceived(DigitalPortMessage message) {
                System.out.println(message.getChannelByte());
                System.out.println(message.getPinMappedValues());
                System.out.println(message.getPortByte());
            }
        };

        firmata.addMessageListener(pinStateListener);
        firmata.addMessageListener(digitalPortListener);
        firmata.start();

        firmata.sendMessage(new SetPinModeMessage(12, PinCapabilities.INPUT));
        //firmata.sendMessage(new SetPinModeMessage(12, PinCapabilities.INPUT_PULLUP));
        Thread.sleep(2000);

        firmata.sendMessage(new ReportDigitalPortMessage(1, true));
        firmata.sendMessage(new SysexPinStateQueryMessage(12));

        Thread.sleep(2000);

        firmata.sendMessage(new ReportDigitalPortMessage(1, false));
        firmata.stop();

    }
}
