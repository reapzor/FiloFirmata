package com.bortbort.arduino.FiloFirmata;

import com.bortbort.arduino.FiloFirmata.Messages.Message;
import java.util.function.Consumer;

/**
 * Static Consumer wrapper to allow Lambda support with the MessageListener
 * @param <K> Message type to listen to.
 */
class ConsumerMessageListener<K extends Message> extends MessageListener<K> {
     // Lambda to be executed when message of type K is received.
    private Consumer<K> consumer;

    /**
     * Construct a MessageListener that wraps around a Java 8+ lambda to support quick and easy listener generation.
     * @param channelIdentifier DigitalChannel identifier of the channel to listen to.
     * @param consumer Lambda logic to be run against the received message.
     * @param messageType Message type to listen for.
     */
    public ConsumerMessageListener(Integer channelIdentifier, Consumer<K> consumer,
                                     Class<K> messageType) {
        this(consumer, messageType);
        this.channelIdentifier = channelIdentifier;
    }

    /**
     * Construct a MessageListener that wraps around a Java 8+ lambda to support quick and easy listener generation.
     * @param channel DigitalChannel identifier of the channel to listen to.
     * @param consumer Lambda logic to be run against the received message.
     * @param messageType Message type to listen for.
     */
    public ConsumerMessageListener(DigitalChannel channel, Consumer<K> consumer,
                                     Class<K> messageType) {
        this(consumer, messageType);
        this.channel = channel;
        this.channelIdentifier = channel.getIdentifier();
    }

    /**
     * Construct a MessageListener that wraps around a Java 8+ lambda to support quick and easy listener generation.
     * @param consumer Lambda logic to be run against the received message.
     * @param messageType Message type to listen for.
     */
    public ConsumerMessageListener(Consumer<K> consumer, Class<K> messageType) {
        this.consumer = consumer;
        this.messageType = messageType;
        this.messageType = messageType;
    }


    /**
     * When a message is received, call the Lambda and execute its logic.
     * @param message Message implementation containing data about the Firmata command sent from the SerialPort.
     */
    @Override
    public void messageReceived(K message) {
        consumer.accept(message);
    }
}
