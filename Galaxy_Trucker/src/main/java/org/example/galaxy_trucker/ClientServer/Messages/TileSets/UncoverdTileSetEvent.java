package org.example.galaxy_trucker.ClientServer.Messages.TileSets;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.galaxy_trucker.ClientServer.Messages.Event;
import org.example.galaxy_trucker.ClientServer.Messages.EventVisitor;
import org.example.galaxy_trucker.Model.Connectors.Connectors;

import java.util.ArrayList;

/**
 * The UncoverdTileSetEvent class represents an event where a set of tiles is uncovered
 * within the game's context. It encapsulates the state or data related to the action
 * of uncovering tiles, including the ID of the uncovered tile set and the associated connectors.
 *
 * This class implements the Event interface, allowing it to be handled uniformly within
 * a system that processes various types of events. It also uses the visitor pattern
 * through the accept method, enabling an EventVisitor to process the event-specific behavior.
 *
 * The class is designed to be serialized and deserialized using JSON annotations, making
 * it compatible with systems that rely on JSON-based communication or persistence.
 */
public class UncoverdTileSetEvent implements Event {

    private Integer id;
    private ArrayList<Connectors> connectors;

    /**
     * Constructs a new UncoverdTileSetEvent instance with the specified parameters.
     * This constructor initializes the event with a unique identifier for the uncovered
     * tile set and a list of connectors associated with the tiles. The data is intended
     * to represent the state or outcome of uncovering a specific tile set in the game.
     *
     * @param id         The unique identifier of the uncovered tile set. This value
     *                   links the event to a specific tile set within the game's context.
     * @param connectors A list of connectors associated with the uncovered tile set.
     *                   Each connector provides additional information about the
     *                   adjacency and properties of the tiles in the set.
     */
    @JsonCreator
    public UncoverdTileSetEvent(
            @JsonProperty("id") Integer id,
            @JsonProperty("connectors") ArrayList<Connectors> connectors
    ) {
        this.id = id;
        this.connectors = connectors;
    }

    /**
     * Default constructor for the UncoverdTileSetEvent class.
     *
     * This constructor initializes a new instance of the UncoverdTileSetEvent class
     * with default values for its member variables. It is used primarily in situations
     * where no specific initialization data is provided, such as during deserialization
     * or when creating a placeholder instance of the event.
     *
     * The UncoverdTileSetEvent represents an event triggered when a set of tiles is uncovered,
     * and this constructor allows for creating the instance without defining initial properties.
     */
    public UncoverdTileSetEvent() {
    }

    /**
     * Retrieves the ID of the uncovered tile set associated with this event.
     *
     * The ID is a unique identifier representing the specific tile set that
     * has been uncovered within the context of the game. This value is
     * used to track and differentiate between various tile sets.
     *
     * @return an Integer representing the ID of the uncovered tile set, or null if not set.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Retrieves the list of connectors associated with this event.
     *
     * This method returns the connectors linked to the tile set event, which
     * represent various types of connections related to the uncovered tiles.
     * The connectors are stored in an ArrayList and can include different
     * implementations of the Connectors interface, such as SINGLE, DOUBLE,
     * UNIVERSAL, and others.
     *
     * @return an ArrayList containing instances of Connectors associated with this event.
     */
    public ArrayList<Connectors> getConnectors() {
        return connectors;
    }

    /**
     * Provides a descriptive message associated with this UncoverdTileSetEvent.
     *
     * This method is intended to return a string message that provides
     * additional information or context about this specific event type.
     * Currently, it returns an empty string as no additional message
     * is specified in this implementation.
     *
     * @return an empty string, representing the message of the event.
     */
    @Override
    public String message() {
        return "";
    }

    /**
     * Accepts an EventVisitor to perform operations specific to this event type.
     * This method is part of the visitor pattern implementation, where event-specific
     * behavior is handled by the given EventVisitor instance.
     *
     * @param visitor the EventVisitor that performs the operation on this event
     *                instance. It must not be null.
     */
    @Override
    public void accept(EventVisitor visitor) {
        visitor.visit(this);
    }

}
