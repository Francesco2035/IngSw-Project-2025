package org.example.galaxy_trucker.Controller.Messages;

import com.fasterxml.jackson.annotation.JsonProperty;

public class HourglassEvent implements Event {

    String message;
    boolean start;

    public HourglassEvent(@JsonProperty("message") String message, @JsonProperty("start") boolean start) {
        this.message = message;
        this.start = start;
    }

    @Override
    public void accept(EventVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String message() {
        return message;
    }

    public boolean getStart() {
        return start;
    }
}
