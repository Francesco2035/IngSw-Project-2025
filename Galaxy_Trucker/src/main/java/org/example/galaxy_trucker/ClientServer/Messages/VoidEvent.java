package org.example.galaxy_trucker.ClientServer.Messages;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a VoidEvent in the system. This event is a type of {@link Event}
 * and is typically utilized to signify specific actions or states with no additional
 * accompanying data beyond the provided coordinates.
 *
 * This class supports the Jackson library's JSON serialization and deserialization
 * annotations, enabling its integration with external systems requiring JSON formats.
 */
public class VoidEvent implements Event {

    /**
     * The X-coordinate associated with this VoidEvent.
     *
     * This variable represents a specific horizontal position or attribute
     * of the event within the system. It is integral to the event's functionality
     * and is typically used alongside the Y-coordinate to define the event's
     * spatial or contextual location.
     */
    private int x;
    /**
     * Represents the Y-coordinate associated with the VoidEvent.
     *
     * This variable stores the vertical position or coordinate as an integer.
     * It is used in conjunction with the X-coordinate to designate a specific
     * location, primarily in the context of the VoidEvent class.
     */
    private int y;

    /**
     * Constructs a VoidEvent instance with the specified coordinates.
     * This constructor is primarily used for JSON deserialization to initialize
     * a VoidEvent object with the given x and y values.
     *
     * @param x the X-coordinate associated with this event
     * @param y the Y-coordinate associated with this event
     */
    @JsonCreator
    public VoidEvent(
            @JsonProperty("x") int x,
            @JsonProperty("y") int y
    ) {
        this.x = x;
        this.y = y;
    }

    /**
     * Default constructor for the VoidEvent class.
     *
     * This no-argument constructor initializes a VoidEvent instance without specifying
     * any initial values for its properties. This can be used for deserialization
     * or cases where the event needs to be constructed and set up later.
     */
    public VoidEvent() {}

    /**
     * Retrieves the X-coordinate associated with this event.
     *
     * @return the X-coordinate as an integer.
     */
    public int getX() {
        return x;
    }

    /**
     * Retrieves the y-coordinate associated with this event.
     *
     * @return the y-coordinate of this event as an integer.
     */
    public int getY() {
        return y;
    }

    /**
     * Returns a message indicating that the VirtualView has been set.
     *
     * @return a string message stating "VirtualView settata".
     */
    @Override
    public String message() {
        return "VirtualView settata";
    }

    /**
     * Accepts a visitor for processing this {@code VoidEvent}.
     *
     * @param visitor the {@link EventVisitor} that will process this event
     */
    @Override
    public void accept(EventVisitor visitor) {
        visitor.visit(this);
    }

}
