package org.example.galaxy_trucker.ClientServer.Messages.TileSets;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.galaxy_trucker.ClientServer.Messages.Event;
import org.example.galaxy_trucker.ClientServer.Messages.EventVisitor;

import java.util.ArrayList;

/**
 * Represents an event related to a deck in the system, implementing the Event interface.
 * This event encapsulates data related to deck operations, specifically a list of IDs,
 * which could represent the unique identifiers of cards within the deck.
 *
 * DeckEvent is serialized/deserialized using JSON annotations, enabling integration
 * with other components that rely on JSON-based data exchange. This helps in ensuring
 * seamless handling of event-specific functionality within the event processing framework.
 *
 * The class also follows the visitor pattern, allowing a concrete EventVisitor
 * implementation to process this event's behavior through the accept method.
 */
public class DeckEvent implements Event {

    private ArrayList<Integer> ids;

    /**
     * Constructs a new DeckEvent instance with a list of IDs representing
     * the unique identifiers of cards or entities associated with the deck.
     *
     * The provided list of IDs allows the event to encapsulate data relevant
     * to deck-related operations, which can be processed by the event handling system.
     *
     * @param ids the list of integer IDs representing elements within the deck
     */
    @JsonCreator
    public DeckEvent(@JsonProperty("ids") ArrayList<Integer> ids) {
        this.ids = ids;
    }

    /**
     * Retrieves the list of IDs associated with this event.
     *
     * @return an ArrayList of integers representing the IDs encapsulated by this event
     */
    public ArrayList<Integer> getIds() {
        return ids;
    }


    /**
     * Allows the EventVisitor to process this specific instance of DeckEvent.
     * Implements the visitor pattern to delegate the handling logic to the provided visitor.
     *
     * @param visitor the EventVisitor that processes this DeckEvent instance
     */
    @Override
    public void accept(EventVisitor visitor) {
        visitor.visit(this);
    }


    /**
     * Retrieves a descriptive message associated with this DeckEvent.
     *
     * This method provides a string message that represents additional
     * context or information about the event. For this implementation,
     * the method returns an empty string by default.
     *
     * @return a string containing the event-specific message. Returns an empty string by default.
     */
    @Override
    public String message() {
        return "";
    }
}
