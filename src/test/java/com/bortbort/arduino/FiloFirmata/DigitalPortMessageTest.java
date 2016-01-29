package com.bortbort.arduino.FiloFirmata;

import com.bortbort.arduino.FiloFirmata.Listeners.DigitalPortMessageListener;
import com.bortbort.arduino.FiloFirmata.Messages.DigitalPortMessage;
import org.junit.Test;

/**
 * Created by chuck on 1/12/2016.
 */
public class DigitalPortMessageTest {
    Firmata firmata = new Firmata();

    @Test
    public void testDigitalPortMessage() throws Exception {
        DigitalPortMessageListener messageListener = new DigitalPortMessageListener() {
            @Override
            public void messageReceived(DigitalPortMessage message) {
                System.out.println(message.getPinMappedValues());
            }
        };

        firmata.addMessageListener(messageListener);
        firmata.start();
        // Report Digital
        firmata.sendRaw((byte) 0xD2, (byte) 0x7);

        Thread.sleep(2000);
        firmata.stop();
    }

}
