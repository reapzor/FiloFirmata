package com.bortbort.arduino.FiloFirmata;

import com.bortbort.arduino.FiloFirmata.Messages.Message;
import net.jodah.typetools.TypeResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SynchronousMessageListener
 * Turns an async design for listening and sending messages into a synchronous design allowing
 * a message to be sent with the return value being the response message.
 */
abstract class SynchronousMessageListener<T extends Message> extends MessageListener<T> {
    private static final Logger log = LoggerFactory.getLogger(SynchronousMessageListener.class);
    private static final int RESPONSE_WAIT_TIME = 5000;
    private T responseMessage;
    private Boolean responseReceived = null;
    private final Object lock = new Object();

    @Override
    public void messageReceived(T message) {
        if (responseReceived != null && responseReceived) {
            log.warn("Received more than one synchronous message reply!");
        }

        if (responseReceived == null) {
            responseMessage = message;
            responseReceived = true;
        }

        synchronized (lock) {
            lock.notify();
        }
    }

    public Boolean getResponseReceived() {
        return responseReceived;
    }

    public Boolean waitForResponse() {
        if (responseReceived != null) {
            return responseReceived;
        }
        
        synchronized (lock) {
            try {
                lock.wait(RESPONSE_WAIT_TIME);
            } catch (InterruptedException e) {
                log.warn("Interrupted waiting for synchronous response.");
            }
        }

        if (responseReceived == null) {
            log.warn("Timed out waiting for synchronous response.");
            responseReceived = false;
        }

        return responseReceived;
    }

    public T getResponseMessage() {
        waitForResponse();
        return responseMessage;
    }

}

