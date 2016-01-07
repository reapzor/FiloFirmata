package com.bortbort.arduino.FiloFirmata.Listeners;

import com.bortbort.arduino.FiloFirmata.Messages.Message;

/**
 * Created by chuck on 1/6/2016.
 */
public abstract class MessageListener<T extends Message> {
    private Class<? extends MessageListener> messageListenerClass;

    public MessageListener(Class<? extends MessageListener> messageListenerClass) {
        this.messageListenerClass = messageListenerClass;
    }

    public abstract void messageReceived(T message);

    public Class<? extends MessageListener> getMessageListenerClass() {
        return messageListenerClass;
    }
}
