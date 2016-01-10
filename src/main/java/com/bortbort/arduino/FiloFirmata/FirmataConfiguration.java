package com.bortbort.arduino.FiloFirmata;

import com.bortbort.arduino.FiloFirmata.PortAdapters.*;

/**
 * Firmata Configuration.
 * Defines the requirements necessary to implement the Firmata protocol over your desired SerialPort
 * communications line.
 */
public class FirmataConfiguration {
    // Anything added here must also be added to:
    // Constructor method "protected FirmataConfiguration(FirmataConfiguration configuration)"
    // String method "public String toString()"
    // Getter & Setter methods generated

    /**
     * The Serial Port Identifier used to bind our SerialPort adapter to.
     * For Windows this is generally "COMX" where X is a number. (Arduinos use "3" for default.)
     * For Mac:
     * For Linux:
     */
    private String serialPortID = "COM3";

    /**
     * Serial Port baud rate used to communicate with the Firmata Device.
     * Default StandardFirmata.ino uses 57600.
     */
    private Integer serialPortBaudRate = 57600;

    /**
     * Serial Port DataBits configuration (generally 8).
     */
    private SerialPortDataBits serialPortDataBits = SerialPortDataBits.DATABITS_8;

    /**
     * Serial Port StopBits configuration (generally 1).
     */
    private SerialPortStopBits serialPortStopBits = SerialPortStopBits.STOPBITS_1;

    /**
     * Serial Port ParityBit configuration (generally N).
     */
    private SerialPortParity serialPortParity = SerialPortParity.PARITY_NONE;

    /**
     * Serial Port adapter class to use with this library.
     * Default uses an implementation of the PureJavaComm Serial Port library, however you can
     * easily extend the Firmata SerialPort adapter to support "JSSC" or "RXTX" as well if needed.
     * See docs:
     * https://github.com/nyholku/purejavacomm
     * https://github.com/scream3r/java-simple-serial-connector
     * https://github.com/rxtx/rxtx ??
     */
    private Class<? extends SerialPort> serialPortAdapterClass = PureJavaCommSerialPort.class;


    /**
     * Construct a new FirmataConfiguration object using default configurations and custom serialPortIdentifier.
     *
     * @param serialPortID String indicating the SerialPort identifier to use with the library.
     */
    public FirmataConfiguration(String serialPortID) {
        this.serialPortID = serialPortID;
    }

    /**
     * Construct a new FirmataConfiguration object using default configurations and custom baud rate.
     *
     * @param serialPortBaudRate Baud rate to use with the SerialPort connected communications device.
     */
    public FirmataConfiguration(Integer serialPortBaudRate) {
        this.serialPortBaudRate = serialPortBaudRate;
    }

    /**
     * Construct a new FirmataConfiguration object using default configurations and
     * custom serialPortIdentifier with custom baud rate.
     *
     * @param serialPortID String indicating the SerialPort identifier to use with the library.
     * @param serialPortBaudRate Baud rate to use with the SerialPort connected communications device.
     */
    public FirmataConfiguration(String serialPortID, Integer serialPortBaudRate) {
        this.serialPortID = serialPortID;
        this.serialPortBaudRate = serialPortBaudRate;
    }

    /**
     * Construct a new FirmataConfiguration object using default configurations.
     */
    public FirmataConfiguration() {
    }

    /**
     * Construct and clone a FirmataConfiguration object from another FirmataConfiguration object.
     *
     * @param configuration FirmataConfiguration object to be cloned.
     */
    protected FirmataConfiguration(FirmataConfiguration configuration) {
        serialPortID = configuration.serialPortID;
        serialPortBaudRate = configuration.serialPortBaudRate;
        serialPortDataBits = configuration.serialPortDataBits;
        serialPortStopBits = configuration.serialPortStopBits;
        serialPortParity = configuration.serialPortParity;
        serialPortAdapterClass = configuration.serialPortAdapterClass;
    }

    /**
     * The Serial Port Identifier used to bind our SerialPort adapter to.
     * For Windows this is generally "COMX" where X is a number. (Arduinos use "3" for default.)
     * For Mac:
     * For Linux:
     */
    public String getSerialPortID() {
        return serialPortID;
    }

    /**
     * The Serial Port Identifier used to bind our SerialPort adapter to.
     * For Windows this is generally "COMX" where X is a number. (Arduinos use "3" for default.)
     * For Mac:
     * For Linux:
     *
     * @param serialPortID String representing the serial port.
     */
    public void setSerialPortID(String serialPortID) {
        this.serialPortID = serialPortID;
    }

    /**
     * Serial Port baud rate used to communicate with the Firmata Device.
     * Default StandardFirmata.ino uses 57600.
     */
    public Integer getSerialPortBaudRate() {
        return serialPortBaudRate;
    }

    /**
     * Serial Port baud rate used to communicate with the Firmata Device.
     * Default StandardFirmata.ino uses 57600.
     *
     * @param serialPortBaudRate Integer baud rate to use for library.
     */
    public void setSerialPortBaudRate(Integer serialPortBaudRate) {
        this.serialPortBaudRate = serialPortBaudRate;
    }

    /**
     * Serial Port DataBits configuration (generally 8).
     */
    public SerialPortDataBits getSerialPortDataBits() {
        return serialPortDataBits;
    }

    /**
     * Serial Port DataBits configuration (generally 8).
     *
     * @param serialPortDataBits SerialPortDataBits configuration enum option.
     */
    public void setSerialPortDataBits(SerialPortDataBits serialPortDataBits) {
        this.serialPortDataBits = serialPortDataBits;
    }

    /**
     * Serial Port StopBits configuration (generally 1).
     */
    public SerialPortStopBits getSerialPortStopBits() {
        return serialPortStopBits;
    }

    /**
     * Serial Port StopBits configuration (generally 1).
     *
     * @param serialPortStopBits SerialPortStopBits configuration enum option.
     */
    public void setSerialPortStopBits(SerialPortStopBits serialPortStopBits) {
        this.serialPortStopBits = serialPortStopBits;
    }

    /**
     * Serial Port ParityBit configuration (generally N).
     */
    public SerialPortParity getSerialPortParity() {
        return serialPortParity;
    }

    /**
     * Serial Port ParityBit configuration (generally N).
     *
     * @param serialPortParity SerialPortParity configuration enum option.
     */
    public void setSerialPortParity(SerialPortParity serialPortParity) {
        this.serialPortParity = serialPortParity;
    }

    public Class<? extends SerialPort> getSerialPortAdapterClass() {
        return serialPortAdapterClass;
    }

    public void setSerialPortAdapterClass(Class<? extends SerialPort> serialPortAdapterClass) {
        this.serialPortAdapterClass = serialPortAdapterClass;
    }


    // Auto generated by IDEA
    @Override
    public String toString() {
        return "FirmataConfiguration{" +
                "serialPortID='" + serialPortID + '\'' +
                ", serialPortBaudRate=" + serialPortBaudRate +
                ", serialPortDataBits=" + serialPortDataBits +
                ", serialPortStopBits=" + serialPortStopBits +
                ", serialPortParity=" + serialPortParity +
                ", serialPortAdapterClass=" + serialPortAdapterClass.getName() +
                '}';
    }
}
