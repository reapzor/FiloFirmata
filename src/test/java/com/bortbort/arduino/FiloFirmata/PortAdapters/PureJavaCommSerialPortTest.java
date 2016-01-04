package com.bortbort.arduino.FiloFirmata.PortAdapters;

import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

/**
 * Created by chuck on 1/3/2016.
 */
public class PureJavaCommSerialPortTest extends TestCase {
    PureJavaCommSerialPort serialPort = new PureJavaCommSerialPort("COM3", 57600);

//    @Before
//    public void setUp() throws Exception {
//
//    }
//
//    @After
//    public void tearDown() throws Exception {
//
//    }

    @Test
    public void testOpenClosePort() throws Exception {
        assertTrue("Port did not open!", serialPort.openPort());
        assertTrue("Port did not close!", serialPort.closePort());
        assertTrue("Port did not re-open!", serialPort.openPort());
        assertTrue("Port did not re-close!", serialPort.closePort());
    }



//    @Test
//    public void testConnect() throws Exception {
//
//    }
//
//    @Test
//    public void testDisconnect() throws Exception {
//
//    }
}