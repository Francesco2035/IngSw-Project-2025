package org.example.galaxy_trucker.Messages;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ConnectionRefusedEvent implements Event{

    String message;


    @JsonCreator
    public ConnectionRefusedEvent(@JsonProperty("message") String message) {
        this.message = message;
    }

    @Override
    public void accept(EventVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String message() {
        return message;
    }
}
