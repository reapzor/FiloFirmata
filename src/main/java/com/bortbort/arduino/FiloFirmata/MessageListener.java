package com.bortbort.arduino.FiloFirmata;

import com.bortbort.arduino.FiloFirmata.Messages.Message;
import net.jodah.typetools.TypeResolver;
import java.util.function.Consumer;

/**
 * MessageListener.
 * Abstract class definition for a listener that can be registered to fire when specific Message events are
 * given as defined by the Generic. When registered, only messages of the provided type
 * will be evented to messageReceived.
 */
public abstract class MessageListener<T extends Message> {
    /**
     * Holds the message type that this listener fires for. When a message is received, it will be be of the type
     * provided when implementing this class.
     */
    protected Class<T> messageType;

    /**
     * Holds the channel identifier for the listener, if provided during construction.
     */
    protected Integer channelIdentifier = null;

    /**
     * Holds the DigitalChannel for the listener, if provided during construction.
     */
    protected DigitalChannel channel;


    /**
     * Construct a new message listener for a given channel identifier and Message type.
     */
    public MessageListener(Integer channelIdentifier) {
        this();
        this.channelIdentifier = channelIdentifier;
    }

    /**
     * Construct a new message listener for a given channel and message type.
     * @param channel DigitalChannel to listen on.
     */
    public MessageListener(DigitalChannel channel) {
        this();
        this.channel = channel;
        this.channelIdentifier = channel.getIdentifier();
    }

    /**
     * Construct a new message listener for a given Message type.
     */
    @SuppressWarnings("unchecked")
    public MessageListener() {
        Class[] typeArguments = TypeResolver.resolveRawArguments(MessageListener.class, getClass());
        messageType = typeArguments[0];
    }


    /**
     * Lambda support. Takes in a consumer and wraps a MessageListener object around it, since the message listener is
     * not a functional interface.
     * @param channelIdentifier Integer identifier of the channel/pin to listen to.
     * @param consumer Lambda logic to be run against the received message.
     * @param <K> Message type to listen for.
     * @return New MessageListener for the given type and logic.
     */
    public static <K extends Message> MessageListener<K> from(Integer channelIdentifier, Consumer<K> consumer) {
        return new ConsumerMessageListener<>(channelIdentifier, consumer);
    }

    /**
     * Lambda support. Takes in a consumer and wraps a MessageListener object around it, since the message listener is
     * not a functional interface.
     * @param channel DigitalChannel identifier of the channel to listen to.
     * @param consumer Lambda logic to be run against the received message.
     * @param <K> Message type to listen for.
     * @return New MessageListener for the given type and logic.
     */
    public static <K extends Message> MessageListener<K> from(DigitalChannel channel, Consumer<K> consumer) {
        return new ConsumerMessageListener<>(channel, consumer);
    }

    /**
     * Lambda support. Takes in a consumer and wraps a MessageListener object around it, since the message listener is
     * not a functional interface.
     * @param consumer Lambda logic to be run against the received message.
     * @param <K> Message type to listen for.
     * @return New MessageListener for the given type and logic.
     */
    public static <K extends Message> MessageListener<K> from(Consumer<K> consumer) {
        return new ConsumerMessageListener<>(consumer);
    }


    /**
     * Handler for messageReceived() events. Whenever a Firmata message from the SerialPort is received, this event
     * will fire to any corresponding listener that wants to handle the specific type of message.
     *
     * @param message Message implementation containing data about the Firmata command sent from the SerialPort.
     */
    public abstract void messageReceived(T message);


    /**
     * Holds the message type that this listener fires for. When a message is received, it will be be of the type
     * provided when implementing this class.
     *
     * @return Class representing the Message type this listener fires for.
     */
    public Class<T> getMessageType() {
        return messageType;
    }

    /**
     * Get the channel identifier this listener is listening to (If used).
     * @return Integer value of the channel this listener is for
     */
    public Integer getChannelIdentifier() {
        return channelIdentifier;
    }

    /**
     * Get the channel this listener is listening to (If used).
     * @return DigitalChannel this listener is for
     */
    public DigitalChannel getChannel() {
        return channel;
    }

}
