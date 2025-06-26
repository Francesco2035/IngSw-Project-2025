package org.example.galaxy_trucker.ClientServer.Messages;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents an event related to an exception in the system.
 * This event is designed to handle and propagate information
 * about an exception that occurs during the application's runtime.
 *
 * The {@code ExceptionEvent} class implements the {@code Event} interface
 * and is managed as part of a system of events that can be visited
 * by an {@code EventVisitor}.
 */
public class ExceptionEvent implements Event{


    String exception;

    /**
     * Constructs a new {@code ExceptionEvent} with the given exception message.
     *
     * This constructor initializes the {@code ExceptionEvent}, which is designed
     * to handle and propagate information about an exception that occurs during
     * the application's runtime. The exception message provides details about the
     * specific exception event.
     *
     * @param exception the exception message describing the error or issue
     */
    @JsonCreator
    public ExceptionEvent(@JsonProperty("exception") String exception) {
        this.exception = exception;
    }


    /**
     * Constructs a new ExceptionEvent instance.
     *
     * This constructor initializes an ExceptionEvent with no specific exception message.
     * The ExceptionEvent represents an event related to exceptions that occur
     * during the application's runtime, allowing them to be handled or propagated
     * within the system. This default constructor is typically used for creating
     * an empty ExceptionEvent instance to populate later or during deserialization.
     */
    public ExceptionEvent() {

    }

    /**
     * Accepts a visitor to process this ExceptionEvent instance.
     *
     * @param visitor the EventVisitor instance responsible for visiting and processing this event
     */
    @Override
    public void accept(EventVisitor visitor) {
        visitor.visit(this);
    }

    /**
     * Retrieves the exception associated with this event.
     *
     * This method provides access to the exception information
     * related to this {@code ExceptionEvent}, which can be used
     * to identify or log the specific issue that occurred during runtime.
     *
     * @return a string representing the exception message or identifier.
     */
    public String getException(){
        return exception;
    }

    /**
     * Retrieves the message associated with this event.
     *
     * This method is intended to provide a descriptive message for the current event.
     * In this implementation, it always returns an empty string.
     *
     * @return a descriptive string representing the event message, or an empty string
     *         if no specific message is associated with the event.
     */
    @JsonIgnore
    @Override
    public String message() {
        return "";
    }
}
