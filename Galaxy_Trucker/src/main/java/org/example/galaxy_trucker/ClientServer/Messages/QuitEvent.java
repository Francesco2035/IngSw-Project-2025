package org.example.galaxy_trucker.ClientServer.Messages;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class QuitEvent implements Event {

    public QuitEvent() {

    }

    @Override
    public void accept(EventVisitor visitor) {
        visitor.visit(this);
    }

    @JsonIgnore
    @Override
    public String message() {
        return "";
    }
}
