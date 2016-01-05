package com.bortbort.arduino.FiloFirmata;

import com.bortbort.arduino.FiloFirmata.PortAdapters.SerialPort;
import com.bortbort.arduino.FiloFirmata.PortAdapters.SerialPortDataBits;
import com.bortbort.arduino.FiloFirmata.PortAdapters.SerialPortParity;
import com.bortbort.arduino.FiloFirmata.PortAdapters.SerialPortStopBits;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by chuck on 1/4/2016.
 */
public class Firmata {
    private static final Logger log = LoggerFactory.getLogger(Firmata.class);
    private SerialPort serialPort;
    private FirmataConfiguration configuration;
    private FirmataSerialPortListener serialPortListener;
    private Boolean started = false;



    public Firmata() {
        this.configuration = new FirmataConfiguration();
    }

    public Firmata(FirmataConfiguration configuration) {
        this.configuration = new FirmataConfiguration(configuration);
    }



    public SerialPort getSerialPort() {
        return serialPort;
    }


    public synchronized Boolean start() {
        if (started) {
            return true;
        }

        createSerialPort();

        serialPortListener = new FirmataSerialPortListener(serialPort);
        serialPort.addEventListener(serialPortListener);

        if (!serialPort.connect()) {
            log.error("Failed to start Firmata Library. Cannot connect to Serial Port.");
            log.error("Configuration is {}", configuration);
            stop();
            return false;
        }

        started = true;
        return true;
    }

    public synchronized  Boolean stop() {
        if (serialPort != null) {
            serialPort.removeEventListener(serialPortListener);
            serialPortListener = null;
        }

        if (!removeSerialPort()) {
            log.error("Failed to stop Firmata Library. Cannot close Serial Port.");
            log.error("Configuration is {}", configuration);
            return false;
        }

        started = false;
        return true;
    }


    // Generate the SerialPort object using the adapter class provided in the configuration
    private void createSerialPort() {
        if (serialPort != null) {
            log.warn("Trying to create serial port while one is already made!");
            removeSerialPort();
        }

        Constructor<? extends SerialPort> constructor;

        try {
            constructor = configuration.getSerialPortAdapterClass().getDeclaredConstructor(
                    String.class, Integer.class,
                    SerialPortDataBits.class, SerialPortStopBits.class, SerialPortParity.class);
        } catch (NoSuchMethodException e) {
            log.error("Unable to construct SerialPort object. Programming error. Your class adapter must support " +
                    "a construction with input args of" +
                    "YourSerialPort(String.class, Integer.class, SerialPortDataBits.class, " +
                    "SerialPortStopBits.class, SerialPortParity.class);");
            e.printStackTrace();
            throw new RuntimeException("Cannot construct SerialPort adapter!");
        }

        try {
            serialPort = constructor.newInstance(
                    configuration.getSerialPortID(),
                    configuration.getSerialPortBaudRate(),
                    configuration.getSerialPortDataBits(),
                    configuration.getSerialPortStopBits(),
                    configuration.getSerialPortParity());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            log.error("Unable to construct SerialPort object. Programming error. Instantiation error. {}",
                    e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Cannot construct SerialPort adapter!");
        }
    }

    private Boolean removeSerialPort() {
        Boolean ret = true;

        if (serialPort != null) {
            ret = serialPort.disconnect();
            serialPort = null;
        }

        return ret;
    }
}
