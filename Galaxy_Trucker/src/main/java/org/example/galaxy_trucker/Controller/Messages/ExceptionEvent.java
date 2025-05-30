package org.example.galaxy_trucker.Controller.Messages;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ExceptionEvent implements Event{

    @JsonProperty("exception")
    String exception;


    public ExceptionEvent(String exception) {
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
