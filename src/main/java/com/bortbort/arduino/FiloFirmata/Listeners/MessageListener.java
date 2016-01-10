package com.bortbort.arduino.FiloFirmata.Listeners;

import com.bortbort.arduino.FiloFirmata.Messages.Message;

/**
 * MessageListener
 * Abstract class definition for a listener that can be registered to fire when specific Message events are
 * given as defined by the Generic. When registered, only messages of the provided type
 * will be evented to messageReceived.
 */
public abstract class MessageListener<T extends Message> {
    /**
     * Holds the message type that this listener fires for. When a message is received, it will be be of the type
     * provided when implementing this class.
     */
    private Class<T> messageType;

    /**
     * Construct a new message listener for the given Message messageType classes
     *
     * @param messageType Message class representing the Message to listen to and handle events from
     */
    public MessageListener(Class<T> messageType) {
        this.messageType = messageType;
    }

    /**
     * Handler for messageReceived() events. Whenever a Firmata message from the SerialPort is received, this event
     * will fire to any corresponding listener that wants to handle the specific type of message.
     *
     * @param message Message implementation containing data about the Firmata command sent from the SerialPort
     */
    public abstract void messageReceived(T message);

    /**
     * Holds the message type that this listener fires for. When a message is received, it will be be of the type
     * provided when implementing this class.
     */
    public Class<T> getMessageType() {
        return messageType;
    }
}
