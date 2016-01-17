package com.bortbort.arduino.FiloFirmata;

import com.bortbort.arduino.FiloFirmata.Listeners.AnalogMessageListener;
import com.bortbort.arduino.FiloFirmata.Listeners.DigitalPortMessageListener;
import com.bortbort.arduino.FiloFirmata.Messages.AnalogMessage;
import com.bortbort.arduino.FiloFirmata.Messages.DigitalPortMessage;
import com.bortbort.arduino.FiloFirmata.Messages.ReportAnalogPinMessage;
import com.bortbort.arduino.FiloFirmata.Messages.ReportDigitalPortMessage;
import org.junit.Test;

/**
 * Created by chuck on 1/16/2016.
 */
public class ReportDigitalTest {
    Firmata firmata = new Firmata();

    @Test
    public void testReportDigitalMessageTest() throws Exception {
        DigitalPortMessageListener digitalListener = new DigitalPortMessageListener() {
            @Override
            public void messageReceived(DigitalPortMessage message) {
                System.out.println(message.getPinMappedValues());
                System.out.println(message.getChannelByte());
            }
        };

        firmata.addMessageListener(digitalListener);
        firmata.start();

        firmata.sendMessage(new ReportDigitalPortMessage(1, true));

        Thread.sleep(2000);

        firmata.sendMessage(new ReportDigitalPortMessage(1, false));

        firmata.stop();
    }
}
