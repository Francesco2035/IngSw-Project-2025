package org.example.galaxy_trucker.ClientServer.Messages.TileSets;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.galaxy_trucker.ClientServer.Messages.Event;
import org.example.galaxy_trucker.ClientServer.Messages.EventVisitor;
/**
 * The LogEvent class represents an event that contains specific logging information
 * such as the event's effects, coordinates (x and y), direction, and type.
 * It implements the Event interface, making it part of a broader event-handling system
 * where various event types can be processed uniformly.
 *
 * The class allows the storage and retrieval of specific logging data which can be utilized
 * in game states or application states requiring tracking of actions or changes.
 * It uses JSON annotations to handle its serialization and deserialization for networking
 * or storage purposes.
 *
 * This class supports the visitor design pattern, where EventVisitors are used
 * to process the event's information dynamically based on the type of event.
 */
public class LogEvent implements Event {

    /**
     * Represents the effect description associated with a LogEvent.
     *
     * This variable stores a string that provides a context-specific explanation
     * or message about the impact or outcome of the event. It can be used to describe
     * the nature of the event, such as its purpose, the changes it induces, or any other
     * related information deemed necessary for understanding the event's behavior
     * within the application.
     *
     * The `effect` variable is initialized to an empty string by default, ensuring
     * that it always contains a value, even if no specific effect is defined.
     */
    String effect = "";
    /**
     * Represents the x-coordinate of the event within the system.
     *
     * This variable indicates the horizontal position associated with this specific
     * event. It is commonly used in spatial contexts or grid-based calculations,
     * where events are positioned or interpreted based on x and y coordinates.
     *
     * The value of x is initially set during the construction of the event object
     * and can be retrieved for processing purposes or system understanding of the
     * event's spatial attributes.
     */
    int x;
    /**
     * Represents the y-coordinate associated with the event.
     *
     * The y-coordinate typically corresponds to the vertical position related to this
     * event in a spatial or grid-based context. It is used to indicate the specific
     * vertical location at which the event occurs or is associated.
     */
    int y;
    /**
     * Represents the direction associated with the event.
     *
     * This variable holds an integer value that typically describes the orientation
     * or movement aspect of the event. The specific interpretation of this value
     * depends on the context in which the event is being used. It may be used
     * for navigation, spatial representation, or other directional purposes.
     *
     * The value is set during the creation or update of a LogEvent instance
     * and can be accessed using the corresponding getter method.
     */
    int direction;
    /**
     * Represents the type of the event within the LogEvent class.
     *
     * This integer value serves as a categorization or classification identifier for the event,
     * facilitating event-specific processing or handling logic. The `type` is often used to
     * differentiate between various kinds of events and to determine the appropriate response
     * or behavior associated with the event during its lifecycle.
     */
    int type;


    /**
     * Default constructor for the LogEvent class.
     *
     * This constructor initializes a new instance of the LogEvent class with
     * default values for its member variables. It is primarily used when no
     * specific initialization data is provided, allowing the creation of a LogEvent
     * instance with empty or zero values. This constructor supports cases where
     * the event data will be set at a later stage or when an instance is required
     * for deserialization or general initialization purposes.
     */
    public LogEvent(){

    }



    /**
     * Constructs a new LogEvent instance with the specified properties. This constructor
     * initializes the event with a detailed effect description, coordinates (x, y),
     * a direction, and a type. It supports JSON serialization and deserialization
     * through the use of Jackson annotations.
     *
     * @param effect     A string describing the effect of the event. If null is provided,
     *                   the effect will default to an empty string.
     * @param x          The x-coordinate associated with the event.
     * @param y          The y-coordinate associated with the event.
     * @param direction  An integer representing the direction associated with the event.
     * @param type       An integer indicating the type of the event.
     */
    @JsonCreator
    public LogEvent( @JsonProperty("effect") String effect, @JsonProperty("x") int x, @JsonProperty("y") int y, @JsonProperty("direction") int direction, @JsonProperty("type") int type) {

        if (effect == null){
            this.effect = "";
        }
        else {
            this.effect = effect;
        }
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.type = type;
    }

    /**
     * Accepts a visitor for processing this LogEvent instance as part of the visitor pattern.
     *
     * @param visitor the EventVisitor instance responsible for handling the logic
     *                specific to this LogEvent type.
     */
    @Override
    public void accept(EventVisitor visitor) {
        visitor.visit(this);
    }

    /**
     * Provides a descriptive message associated with this LogEvent.
     *
     * This method returns the effect of the event, which is a specific
     * string representing the outcome or impact of the event. The effect
     * is expected to be a meaningful and context-specific message for the
     * event's behavior.
     *
     * @return a string representing the effect of the event.
     */
    @JsonIgnore
    @Override
    public String message() {
        return effect;
    }

    /**
     * Retrieves the effect associated with this event.
     *
     * The effect is represented as a string and provides additional
     * context or information about the specific state or behavior
     * associated with the event.
     *
     * @return a string representing the effect of this event.
     */
    public String getEffect() {
        return effect;
    }

    /**
     * Retrieves the x-coordinate of the event.
     *
     * This value represents the horizontal position associated
     * with this event, often used in contexts where spatial
     * information or grid-based positioning is required.
     *
     * @return the x-coordinate as an integer
     */
    public int getX() {
        return x;
    }
    /**
     * Retrieves the y-coordinate value associated with the LogEvent.
     *
     * This method is used to access the y-coordinate of the event, which
     * represents a vertical coordinate in the context of this event's position.
     *
     * @return an integer representing the y-coordinate of the LogEvent.
     */
    public int getY() {
        return y;
    }
    /**
     * Retrieves the direction associated with this event.
     *
     * The direction typically represents the orientation or movement
     * aspect of the event, described in an integer format. This value
     * is specific to the event instance and is determined when the event
     * is created or updated.
     *
     * @return the integer value representing the direction of the event
     */
    public int getDirection() {
        return direction;
    }
    /**
     * Retrieves the type identifier associated with this event.
     *
     * The type typically represents a specific categorization or classification
     * of the event within the system, which can be used for processing or handling
     * logic based on the event's type.
     *
     * @return an integer value representing the type of this event
     */
    public int getType() {
        return type;
    }

}
