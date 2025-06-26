package org.example.galaxy_trucker.Controller.Messages;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.example.galaxy_trucker.Commands.QuitCommand;
import org.example.galaxy_trucker.Controller.VirtualView;

/**
 * The QuitEvent class represents an event indicating that a participant has quit.
 * It implements the Event interface, allowing it to be processed by an EventVisitor.
 */
public class QuitEvent implements Event {

    /**
     * Constructs a new QuitEvent instance.
     *
     * This constructor initializes the QuitEvent, which represents an event
     * indicating that a participant has quit. The QuitEvent can be processed
     * by an EventVisitor and may contain additional behaviors or actions
     * defined in its class implementation.
     */
    public QuitEvent() {}

    /**
     * Accepts a visitor for processing this {@code QuitEvent}.
     *
     * @param visitor the {@link EventVisitor} instance that will handle this event
     */
    @Override
    public void accept(EventVisitor visitor) {
        visitor.visit(this);
    }

    /**
     * Retrieves the message associated with this event.
     *
     * This method is intended to return a descriptive string message
     * relevant to the specific event it belongs to. For this implementation,
     * the method returns an empty string.
     *
     * @return a descriptive string representing the message of the event, or an
     *         empty string if no specific message is associated with this event.
     */
    @JsonIgnore
    @Override
    public String message() {
        return "";
    }
}
