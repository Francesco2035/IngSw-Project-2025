package org.example.galaxy_trucker.ClientServer.Messages;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents an hourglass-related event in the system. This event contains a message and a flag
 * indicating whether it signals the start of a process or action. The class is part of the event-handling
 * system and implements the Event interface.
 *
 * The HourglassEvent is typically used to handle events related to time-dependent operations,
 * where the "start" property indicates whether the hourglass (or process) is starting or stopping.
 * The message property provides additional context or information about the event.
 *
 * This class supports JSON serialization and deserialization via Jackson annotations.
 */
public class HourglassEvent implements Event {

    /**
     * Represents the message property of the HourglassEvent.
     *
     * The `message` variable contains contextual or descriptive information
     * related to the event. It is initialized with an empty string by default
     * and can later be updated with a specific message. This property is typically
     * used to convey details about the purpose or state of the event in a human-readable format.
     *
     * The `@JsonProperty` annotation is used to bind this field to the JSON key "message"
     * during the serialization and deserialization process, ensuring consistent mapping
     * between the object and its JSON representation.
     */
    @JsonProperty("message")
    String message = "";
    /**
     * Indicates whether an event signifies the start of a process or action.
     *
     * This boolean property represents the initiation state of an event,
     * where a value of true implies that the event signifies the start
     * of a particular process or operation, and false indicates otherwise.
     */
    @JsonProperty("start")
    boolean start;

    /**
     * Default constructor for the HourglassEvent class.
     *
     * Initializes a new instance of the HourglassEvent class with default values.
     * By default, the `message` property is an empty string, and the `start` property
     * is not explicitly set. This constructor is primarily used when creating
     * an HourglassEvent instance with no initial parameters, such as during simple
     * instantiations or deserialization.
     */
    public HourglassEvent() {

    }

    /**
     * Constructs an HourglassEvent with the specified message and start status.
     *
     * @param message the message associated with the event. If null, it defaults to an empty string.
     * @param start a boolean indicating whether the event signifies the start of a process or action.
     */
    @JsonCreator
    public HourglassEvent(@JsonProperty("message") String message, @JsonProperty("start") boolean start) {
        this.message = (message == null) ? "" : message;
        this.start = start;
    }

    /**
     * Accepts a visitor for processing this {@code HourglassEvent}.
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
     * This method provides the value of the message property associated
     * with the HourglassEvent. The message typically contains contextual
     * information or details relevant to the event's occurrence.
     *
     * @return the message as a string, or an empty string if no message is set.
     */
    @JsonIgnore
    @Override
    public String message() {
        return message;
    }

    /**
     * Retrieves the value of the start property.
     *
     * The start property indicates whether the associated event
     * represents the initiation of a process or action.
     *
     * @return true if the event signals the start of a process; false otherwise.
     */
    public boolean getStart() {
        return start;
    }

    /**
     * Retrieves the message associated with this event.
     *
     * @return the message as a string, providing additional context or information related to this event.
     */
    public String getMessage(){
        return message;
    }


}
