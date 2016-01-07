package com.bortbort.arduino.FiloFirmata.Messages;

import com.bortbort.arduino.FiloFirmata.Listeners.MessageListener;

/**
 * Created by chuck on 1/6/2016.
 */
public abstract class Message {
    private Class<? extends MessageListener> messageListenerClass;

    public Message(Class<? extends MessageListener> messageListenerClass) {
        this.messageListenerClass = messageListenerClass;
    }

    public Class<? extends MessageListener> getMessageListenerClass() {
        return messageListenerClass;
    }
}
