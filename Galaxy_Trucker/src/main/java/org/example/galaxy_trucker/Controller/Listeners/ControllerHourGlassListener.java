package org.example.galaxy_trucker.Controller.Listeners;

import org.example.galaxy_trucker.Controller.Messages.HourglassEvent;

/**
 * Defines an interface for listening to hourglass-related events within the game controller.
 * Implementers of this interface are expected to handle updates related to the hourglass timer and its state.
 */
public interface ControllerHourGlassListener {

    /**
     * Invoked when the hourglass timer has completely finished its countdown.
     * Implementations of this method should define the actions to be taken when
     * the timer has run out of all usages and is no longer active.
     */
    void onFinish();

    /**
     * Handles updates related to the hourglass timer and its state. This method is invoked
     * whenever there is an event concerning the hourglass, such as starting or ending its duration.
     *
     * @param event the HourglassEvent object containing details about the hourglass timer event,
     *              including its state (start or stop) and a related message.
     */
    void hourglassUpdate(HourglassEvent event);
}
