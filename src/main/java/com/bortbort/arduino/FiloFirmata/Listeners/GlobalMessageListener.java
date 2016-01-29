package com.bortbort.arduino.FiloFirmata.Listeners;

import com.bortbort.arduino.FiloFirmata.Messages.Message;

/**
 * GlobalMessageListener.
 * Abstract class definition for a listener that can be registered to fire when any Message events are fired.
 */
public abstract class GlobalMessageListener {

    /**
     * Construct a new global message listener.
     *
     */
    public GlobalMessageListener() {

    }

    /**
     * Handler for messageReceived() events. Whenever a Firmata message from the SerialPort is received, this event
     * will fire to any corresponding listener that wants to handle the message.
     *
     * @param message Message containing data about the Firmata command sent from the SerialPort.
     */
    public abstract void messageReceived(Message message);

}
