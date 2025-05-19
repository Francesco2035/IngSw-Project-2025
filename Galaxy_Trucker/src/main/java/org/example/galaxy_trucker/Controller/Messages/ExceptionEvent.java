package org.example.galaxy_trucker.Controller.Messages;

public class ExceptionEvent implements Event{
    @Override
    public void accept(EventVisitor visitor) {

    }

    @Override
    public String message() {
        return "";
    }
}
