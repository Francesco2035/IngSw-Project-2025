package org.example.galaxy_trucker.ClientServer.Messages;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.galaxy_trucker.Model.Connectors.Connectors;

import java.util.ArrayList;

/**
 * Represents an event associated with a hand in the context of the application.
 * This class implements the Event interface, allowing it to be processed
 * by an EventVisitor. A HandEvent contains an identifier and a list of connectors.
 */
public class HandEvent implements Event {

    private int id;
    private ArrayList<Connectors> connectors;

    /**
     * Constructs a HandEvent instance with the specified identifier and list of connectors.
     * This class represents an event related to a player's hand that contains a unique ID
     * and a collection of connectors associated with it.
     *
     * @param id the unique identifier for the HandEvent
     * @param connectors the list of connectors associated with this event
     */
    @JsonCreator
    public HandEvent(
            @JsonProperty("id") int id,
            @JsonProperty("connectors") ArrayList<Connectors> connectors
    ) {
        this.id = id;
        this.connectors = connectors;
    }

    /**
     * Default constructor for the HandEvent class.
     *
     * This no-argument constructor initializes a new instance of HandEvent without
     * setting any initial values for its properties. This is typically used for
     * deserialization or creating empty HandEvent instances to populate later.
     */
    public HandEvent() {}

    /**
     * Accepts a visitor to process this HandEvent instance.
     *
     * @param visitor the EventVisitor instance that will process this event
     */
    @Override
    public void accept(EventVisitor visitor) {
        visitor.visit(this);
    }

    /**
     * Retrieves the message associated with this event.
     *
     * This method is intended to return a descriptive message
     * relevant to the specific event it belongs to. For this implementation,
     * the method returns an empty string.
     *
     * @return a descriptive string representing the message of the event, or an
     *         empty string if no specific message is associated with this event.
     */
    @Override
    public String message() {
        return "";
    }

    /**
     * Retrieves the identifier associated with this instance.
     *
     * @return the identifier as an integer.
     */
    public int getId() {
        return id;
    }

    /**
     * Retrieves the list of connectors associated with this instance.
     *
     * @return an ArrayList containing the connectors of type {@code Connectors}.
     */
    public ArrayList<Connectors> getConnectors() {
        return connectors;
    }
}
