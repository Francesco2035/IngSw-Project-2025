package org.example.galaxy_trucker.Messages;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ExceptionEvent implements Event{


    String exception;

    @JsonCreator
    public ExceptionEvent(@JsonProperty("exception") String exception) {
        this.exception = exception;
    }


    public ExceptionEvent() {

    }

    @Override
    public void accept(EventVisitor visitor) {
        visitor.visit(this);
    }

    public String getException(){
        return exception;
    }

    @Override
    public String message() {
        return "";
    }
}
