package org.example.galaxy_trucker.ClientServer.Messages;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class FinishGameEvent implements Event {

    boolean win;
    String message;

    public FinishGameEvent() {

    }

    public FinishGameEvent(@JsonProperty("win") boolean win, @JsonProperty("message") String message) {
        this.win = win;
        this.message = message;
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

    public boolean isWin() {
        return win;
    }

    public String getMessage() {
        return message;
    }
}
