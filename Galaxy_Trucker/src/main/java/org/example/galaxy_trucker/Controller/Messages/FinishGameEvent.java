package org.example.galaxy_trucker.Controller.Messages;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FinishGameEvent implements Event {

    boolean win;
    String message;

    public FinishGameEvent(@JsonProperty("win") boolean win, @JsonProperty("message") String message) {
        this.win = win;
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

    public boolean isWin() {
        return win;
    }
}
