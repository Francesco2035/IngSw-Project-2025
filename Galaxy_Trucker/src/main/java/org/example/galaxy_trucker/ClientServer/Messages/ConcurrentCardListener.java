package org.example.galaxy_trucker.ClientServer.Messages;

import org.example.galaxy_trucker.Controller.Listeners.GameLobbyListener;

/**
 * Interface representing a listener for concurrent card events.
 * This listener is intended to handle events triggered when a card's state
 * changes concurrently in a specific phase.
 */
public interface ConcurrentCardListener {

    /**
     * Handles an event triggered by the state change of a card during a concurrent phase.
     * The method is invoked when the specific phase-related card state needs processing or reaction.
     *
     * @param phase indicates the current concurrent phase state.
     *              A value of {@code true} represents that it is in the concurrent phase,
     *              while {@code false} indicates otherwise.
     */
    void onConcurrentCard(boolean phase);

}
