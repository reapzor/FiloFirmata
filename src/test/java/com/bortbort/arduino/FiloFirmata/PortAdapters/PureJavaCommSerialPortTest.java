package com.bortbort.arduino.FiloFirmata.PortAdapters;

import com.bortbort.helpers.DataTypeHelpers;
import junit.framework.TestCase;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by chuck on 1/3/2016.
 */
public class PureJavaCommSerialPortTest extends TestCase {
    protected static final Logger log = LoggerFactory.getLogger(PureJavaCommSerialPortTest.class);

    private static final String expectedResponseBytes = "F07902055300740061006E006400610072006400460069" +
            "0072006D006100740061002E0069006E006F00F7"; // f0 79 02 05 StandardFirmata.ino f7
    PureJavaCommSerialPort serialPort = new PureJavaCommSerialPort("COM3", 57600);


    @Test
    public void testDataRXTX() throws Exception {
        assertTrue("Unable to connect serial port!",
                serialPort.connect());

        // Clear out the buffer if theres anything in it
        while (serialPort.getInputStream().available() != 0) {
            serialPort.getInputStream().read();
        }

        // Report Firmware
        serialPort.getOutputStream().write(new byte[] {(byte) 0xf0, (byte) 0x79, (byte) 0xf7});

        // Sleep a small amount of time to get reply through
        Thread.sleep(500);

        Integer byteCount = serialPort.getInputStream().available();
        byte[] bytes = new byte[byteCount];
        Integer readBytes = serialPort.getInputStream().read(bytes, 0, byteCount);

        String returnedBytes = DataTypeHelpers.bytesToHexString(bytes);

        log.info("Expected Bytes: {}", expectedResponseBytes);
        log.info("Returned Bytes: {}", returnedBytes);

        assertEquals("Bytes read does not match bytes available!" +
                " Be sure to use StandardFirmata.ino on test ardunio.",
                byteCount, readBytes);

        assertEquals("Returned byte pattern is longer than expected pattern!",
                returnedBytes.length(), expectedResponseBytes.length());

        assertEquals("Returned byte pattern does not match expected pattern!",
                returnedBytes, expectedResponseBytes);

        assertTrue("Unable to disconnect Serial Port!",
                serialPort.disconnect());
    }

}