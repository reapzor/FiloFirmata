package com.bortbort.arduino.FiloFirmata;

import com.bortbort.arduino.FiloFirmata.Listeners.AnalogMessageListener;
import com.bortbort.arduino.FiloFirmata.Messages.AnalogMessage;
import com.bortbort.arduino.FiloFirmata.Messages.ReportAnalogPinMessage;
import com.bortbort.helpers.DataTypeHelpers;
import org.junit.Test;

/**
 * Created by chuck on 1/16/2016.
 */
public class ReportAnalogTest {
    Firmata firmata = new Firmata();

    @Test
    public void testReportAnalogMessageTest() throws Exception {
        AnalogMessageListener analogListener = new AnalogMessageListener() {
            @Override
            public void messageReceived(AnalogMessage message) {
                System.out.println(DataTypeHelpers.bytesToHexString(message.getAnalogValue()));
                System.out.println(message.getChannelByte());
            }
        };

        firmata.addMessageListener(analogListener);
        firmata.start();

        firmata.sendMessage(new ReportAnalogPinMessage(2, true));

        Thread.sleep(2000);

        firmata.sendMessage(new ReportAnalogPinMessage(2, false));

        firmata.stop();
    }
}
