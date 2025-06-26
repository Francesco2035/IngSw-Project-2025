package org.example.galaxy_trucker.Controller.Messages;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents an event triggered when a connection attempt is refused.
 * This class is a specific implementation of the Event interface and
 * contains a message providing further details about the refused connection.
 *
 * The ConnectionRefusedEvent is typically used in scenarios where a client
 * attempts to establish a connection to the server but is denied, and the
 * event serves as notification or feedback of this occurrence.
 *
 * The class provides a no-argument constructor for default initialization
 * and a parameterized constructor for initializing the event with a specific
 * refusal message. The associated message can be accessed using the message method.
 *
 * Additionally, this event supports the visitor pattern by implementing
 * the accept method, allowing processing by an EventVisitor.
 *
 * Serialization and deserialization are facilitated using JSON annotations.
 */
public class ConnectionRefusedEvent implements Event{

    String message;

    public ConnectionRefusedEvent(){

    }

    /**
     * Constructs a ConnectionRefusedEvent with a specified message.
     * This constructor initializes the event with a message that provides
     * additional details about why the connection was refused.
     *
     * @param message the message explaining the reason for the refused connection
     */
    @JsonCreator
    public ConnectionRefusedEvent(@JsonProperty("message") String message) {
        this.message = message;
    }

    /**
     * Accepts an EventVisitor to perform operations based on the specific
     * type of this event. This method enables the visitor pattern, which
     * allows external classes to execute customized behaviors associated
     * with this event without modifying its code.
     *
     * @param visitor the EventVisitor that processes this event
     */
    @Override
    public void accept(EventVisitor visitor) {
        visitor.visit(this);
    }

    /**
     * Retrieves the descriptive message associated with this ConnectionRefusedEvent.
     *
     * This method provides the message detailing the reason or context of the refused connection.
     *
     * @return a string representing the message associated with this event.
     */
    @Override
    public String message() {
        return message;
    }
}
