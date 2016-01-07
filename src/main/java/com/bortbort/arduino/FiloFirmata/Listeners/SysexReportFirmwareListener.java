package com.bortbort.arduino.FiloFirmata.Listeners;

import com.bortbort.arduino.FiloFirmata.Messages.SysexReportFirmwareMessage;

/**
 * Created by chuck on 1/7/2016.
 */
public abstract class SysexReportFirmwareListener extends MessageListener<SysexReportFirmwareMessage> {

    public SysexReportFirmwareListener() {
        super(SysexReportFirmwareListener.class);
    }

    @Override
    public abstract void messageReceived(SysexReportFirmwareMessage message);
}
