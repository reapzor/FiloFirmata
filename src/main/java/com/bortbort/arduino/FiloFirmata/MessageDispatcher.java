package com.bortbort.arduino.FiloFirmata;

import com.bortbort.arduino.FiloFirmata.Listeners.MessageListener;
import com.bortbort.arduino.FiloFirmata.Messages.Message;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by chuck on 1/6/2016.
 */
public class MessageDispatcher {
    private final HashMap<Class<? extends MessageListener>,
            ArrayList<MessageListener>> messageListenerMap = new HashMap<>();

    public MessageDispatcher() {
    }

    public void addMessageListener(MessageListener<? extends Message> messageListener) {
        Class<? extends MessageListener> messageListenerClass = messageListener.getMessageListenerClass();

        if (!messageListenerMap.containsKey(messageListenerClass)) {
            ArrayList<MessageListener> listenerArray = new ArrayList<>();
            listenerArray.add(messageListener);
            messageListenerMap.put(messageListenerClass, listenerArray);
        }
        else {
            messageListenerMap.get(messageListenerClass).add(messageListener);
        }
    }


    public void removeMessageListener(MessageListener messageListener) {
        Class<? extends MessageListener> messageListenerClass = messageListener.getClass();

        if (messageListenerMap.containsKey(messageListenerClass)) {
            messageListenerMap.get(messageListenerClass).remove(messageListener);
        }
    }

    // It seems this is necessary, since this is the only part of the design where we need to translate from generics
    //   to implementations, and the only objects that know what Message implementation this is are the MessageListener
    //   implementations, which we are also generic here.
    @SuppressWarnings("unchecked")
    public void dispatchMessage(Message message) {
        Class<? extends MessageListener> messageListenerClass = message.getMessageListenerClass();
        if (messageListenerMap.containsKey(messageListenerClass)) {
            ArrayList<MessageListener> messageListeners = messageListenerMap.get(messageListenerClass);
            for (MessageListener listener : messageListeners) {
                listener.messageReceived(message);
            }
        }
    }


}
