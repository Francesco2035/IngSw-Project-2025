package org.example.galaxy_trucker.ClientServer.Messages;

/**
 * The ReadyListener interface serves as a callback mechanism for events or states
 * that signify readiness. Implementations of this interface should define specific
 * behaviors to execute when the onReady method is triggered.
 */
public interface ReadyListener {
    /**
     * Method to be invoked when a readiness event occurs.
     *
     * The implementation of this method should define the behavior to execute
     * when the associated object or component signals that it is ready.
     * Typically used in listener interfaces or callback mechanisms to react
     * to readiness notifications in a program workflow.
     */
    void onReady();
}
