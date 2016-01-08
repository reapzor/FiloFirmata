package com.bortbort.arduino.FiloFirmata.Listeners;

import com.bortbort.arduino.FiloFirmata.Messages.Message;

/**
 * Created by chuck on 1/6/2016.
 */
public abstract class MessageListener<T extends Message> {
    private Class<T> messageType;

    public MessageListener(Class<T> messageType) {
        this.messageType = messageType;
    }

    public abstract void messageReceived(T message);

    public Class<T> getMessageType() {
        return messageType;
    }
}
