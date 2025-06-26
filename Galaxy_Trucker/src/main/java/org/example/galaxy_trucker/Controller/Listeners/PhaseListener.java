package org.example.galaxy_trucker.Controller.Listeners;

import org.example.galaxy_trucker.ClientServer.Messages.PhaseEvent;

/**
 * The PhaseListener interface defines a contract for classes that are interested
 * in receiving notifications or handling specific events related to phase changes.
 * Implementing classes must provide the functionality for responding when
 * a phase has changed.
 * <p>
 * The method declared in this interface is invoked whenever a relevant
 * phase-related event occurs, passing a PhaseEvent instance that provides
 * details about the event.
 */
public interface PhaseListener {
    /**
     * Handles the event triggered when a phase change occurs during the execution
     * of a process. This method is invoked with details about the phase event,
     * allowing the implementing class to perform the necessary actions or updates.
     *
     * @param event the PhaseEvent containing information about the phase change,
     *              including its associated state and contextual data.
     */
    void PhaseChanged(PhaseEvent event);
}
