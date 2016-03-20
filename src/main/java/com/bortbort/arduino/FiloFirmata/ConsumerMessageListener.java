package com.bortbort.arduino.FiloFirmata;

import com.bortbort.arduino.FiloFirmata.Messages.Message;
import java.util.function.Consumer;

/**
 * Static Consumer wrapper to allow Lambda support with the MessageListener
 * @param <K> Message type to listen to.
 */
public class ConsumerMessageListener<K extends Message> extends MessageListener<K> {
    Consumer<K> consumer;

    public ConsumerMessageListener(Integer channelIdentifier, Consumer<K> consumer,
                                     Class<K> messageType) {
        this(consumer, messageType);
        this.channelIdentifier = channelIdentifier;
    }

    public ConsumerMessageListener(DigitalChannel channel, Consumer<K> consumer,
                                     Class<K> messageType) {
        this(consumer, messageType);
        this.channel = channel;
        this.channelIdentifier = channel.getIdentifier();
    }

    public ConsumerMessageListener(Consumer<K> consumer, Class<K> messageType) {
        this.consumer = consumer;
        this.messageType = messageType;
        this.messageType = messageType;
    }

    @Override
    public void messageReceived(K message) {
        consumer.accept(message);
    }
}
