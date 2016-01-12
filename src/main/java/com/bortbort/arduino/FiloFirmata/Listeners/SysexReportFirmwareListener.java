package com.bortbort.arduino.FiloFirmata.Listeners;

import com.bortbort.arduino.FiloFirmata.Messages.SysexReportFirmwareMessage;

/**
 * Sysex Report Firmware Listener.
 * Abstract class definition for a listener that can be registered to fire when a SysexReportFirmwareMessage is
 * received over the SerialPort.
 */
public abstract class SysexReportFirmwareListener extends MessageListener<SysexReportFirmwareMessage> {

    /**
     * Register with the base MessageListener class that we are using a SysexReportFirmwareMessage Message.
     */
    public SysexReportFirmwareListener() {
        super(SysexReportFirmwareMessage.class);
    }

}
