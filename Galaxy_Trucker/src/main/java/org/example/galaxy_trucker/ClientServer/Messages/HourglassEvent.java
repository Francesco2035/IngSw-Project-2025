package org.example.galaxy_trucker.ClientServer.Messages;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class HourglassEvent implements Event {

    @JsonProperty("message")
    String message = "";
    @JsonProperty("start")
    boolean start;

    public HourglassEvent() {

    }

    @JsonCreator
    public HourglassEvent(@JsonProperty("message") String message, @JsonProperty("start") boolean start) {
        this.message = (message == null) ? "" : message;
        this.start = start;
    }

    @Override
    public void accept(EventVisitor visitor) {
        visitor.visit(this);
    }

    @JsonIgnore
    @Override
    public String message() {
        return message;
    }

    public boolean getStart() {
        return start;
    }

    public String getMessage(){
        return message;
    }


}
