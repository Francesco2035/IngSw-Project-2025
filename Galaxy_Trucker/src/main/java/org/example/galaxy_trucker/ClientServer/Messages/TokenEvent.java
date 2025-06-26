package org.example.galaxy_trucker.ClientServer.Messages;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The TokenEvent class represents an event containing a token. It serves as a
 * message in the application and implements the Event interface to allow
 * handling by an EventVisitor.
 *
 * This event can be used to encapsulate token information for communication
 * or processing within the system.
 */
public class TokenEvent implements Event{

    /**
     * The token associated with the event.
     *
     * This variable holds a string representation of a token used within the context
     * of the event. The token may be utilized for identification, authentication, or
     * other purposes depending on the specific requirements of the system.
     */
    String token;


    /**
     * Constructs a new TokenEvent instance with the specified token.
     *
     * @param token the token associated with this event, provided as a string.
     */
    public TokenEvent(@JsonProperty("token") String token) {
        this.token = token;
    }

    /**
     * Default constructor for the TokenEvent class.
     *
     * Initializes a new instance of the TokenEvent without setting the token.
     * This constructor is useful for deserialization or cases where the token
     * will be added after the object is created.
     */
    public TokenEvent() {

    }

    /**
     * Accepts a visitor for processing this {@code TokenEvent}.
     *
     * @param visitor the {@link EventVisitor} implementation that will handle this event
     */
    @Override
    public void accept(EventVisitor visitor) {
        visitor.visit(this);
    }

    /**
     * Provides a message representation of the event.
     *
     * This method is typically used to retrieve a string
     * representation of an event, although the implementation
     * in this case returns an empty string.
     *
     * @return an empty string as the message representation.
     */
    @JsonIgnore
    @Override
    public String message() {
        return "";
    }

    /**
     * Retrieves the token associated with this event.
     *
     * @return the token string associated with this event.
     */
    public String getToken() {
        return token;
    }

}
