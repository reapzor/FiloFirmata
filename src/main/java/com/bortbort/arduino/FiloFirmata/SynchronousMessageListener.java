package com.bortbort.arduino.FiloFirmata;

import com.bortbort.arduino.FiloFirmata.Messages.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SynchronousMessageListener
 * Turns an async design for listening and sending messages into a synchronous design allowing
 * a message to be sent with the return value being the response message.
 */
class SynchronousMessageListener extends MessageListener<Message> {
    private static final Logger log = LoggerFactory.getLogger(SynchronousMessageListener.class);
    private static final int RESPONSE_WAIT_TIME = 5000;
    private Message responseMessage;
    private Boolean responseReceived = null;
    private final Object lock = new Object();

    /**
     * Construct a synchronous message listener for a given message type.
     * @param messageType The Class type of message to listen for.
     */
    public SynchronousMessageListener(Class<? extends Message> messageType) {
        super();
        this.messageType = messageType;
    }


    /**
     * When we get our expected message response. Save the message object, and notify the waiting thread
     * that the message is now available to be passed back.
     * This method attempts to handle and warn against cases where multiple messages came in, or a message
     * came in after the timeout period.
     * @param message Message response from the project board that will be saved.
     */
    @Override
    public void messageReceived(Message message) {
        if (responseReceived != null) {
            if (responseReceived) {
                log.warn("Received more than one synchronous message reply!");
            }
            else {
                log.warn("Received response message after timeout was hit!");
            }
        }

        responseMessage = message;
        responseReceived = true;

        synchronized (lock) {
            lock.notify();
        }
    }

    /**
     * Check if there has been a response from the project board with our expected message.
     * @return True if response received. False if not. Null if we have not started waiting yet,
     *   or are in the process of still waiting.
     */
    public Boolean getResponseReceived() {
        return responseReceived;
    }

    /**
     * Wait/Block for a specific response message from the project board. This will block for up to 5 seconds
     *   waiting for a reply message matching the given class T.
     * @return True if the response came through while waiting. False if the timeout was hit while waiting
     *   for the response.
     */
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

    /**
     * Get the response message that was sent up by the project board. Auto casts to the correct specific
     * class type that was used when implementing this class.
     * @return T Message type object holding the response message the client was waiting for.
     */
    public Message getResponseMessage() {
        waitForResponse();
        return responseMessage;
    }

}

