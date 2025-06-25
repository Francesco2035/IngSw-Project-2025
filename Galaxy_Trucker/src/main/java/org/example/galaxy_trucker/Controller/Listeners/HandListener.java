package org.example.galaxy_trucker.Controller.Listeners;

import org.example.galaxy_trucker.Controller.Messages.HandEvent;

/**
 * The HandListener interface is used to listen for changes or updates to a hand event.
 * Implementations of this interface can handle specific behavior when a hand event occurs.
 *
 * This interface should be implemented by classes that need to respond
 * to actions or state changes related to HandEvent objects.
 */
public interface HandListener {

        /**
         * Handles changes related to a hand event. Implementations of this method
         * should define the specific behavior to be performed when a hand event occurs.
         *
         * @param event the HandEvent object containing details about the hand change,
         *              including its associated connectors and unique identifier.
         */
        void handChanged(HandEvent event);
}
