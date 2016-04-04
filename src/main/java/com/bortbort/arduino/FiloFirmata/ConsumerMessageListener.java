package com.bortbort.arduino.FiloFirmata;

import com.bortbort.arduino.FiloFirmata.Messages.Message;
import net.jodah.typetools.TypeResolver;
import java.util.function.Consumer;

/**
 * Static Consumer wrapper to allow Lambda support with the MessageListener
 * @param <T> Message type to listen to.
 */
class ConsumerMessageListener<T extends Message> extends MessageListener<T> {
     // Lambda to be executed when message of type K is received.
    private Consumer<T> consumer;

    /**
     * Construct a MessageListener that wraps around a Java 8+ lambda to support quick and easy listener generation.
     * @param channelIdentifier DigitalChannel identifier of the channel to listen to.
     * @param consumer Lambda logic to be run against the received message.
     */
    public ConsumerMessageListener(Integer channelIdentifier, Consumer<T> consumer) {
        this(consumer);
        this.channelIdentifier = channelIdentifier;
    }

    /**
     * Construct a MessageListener that wraps around a Java 8+ lambda to support quick and easy listener generation.
     * @param channel DigitalChannel identifier of the channel to listen to.
     * @param consumer Lambda logic to be run against the received message.
     */
    public ConsumerMessageListener(DigitalChannel channel, Consumer<T> consumer) {
        this(consumer);
        this.channel = channel;
        this.channelIdentifier = channel.getIdentifier();
    }

    /**
     * Construct a MessageListener that wraps around a Java 8+ lambda to support quick and easy listener generation.
     * @param consumer Lambda logic to be run against the received message.
     */
    @SuppressWarnings("unchecked")
    public ConsumerMessageListener(Consumer<T> consumer) {
        Class typeArguments[] = TypeResolver.resolveRawArguments(Consumer.class, consumer.getClass());
        this.consumer = consumer;
        this.messageType = typeArguments[0];
    }


    /**
     * When a message is received, call the Lambda and execute its logic.
     * @param message Message implementation containing data about the Firmata command sent from the SerialPort.
     */
    @Override
    public void messageReceived(T message) {
        consumer.accept(message);
    }
}
