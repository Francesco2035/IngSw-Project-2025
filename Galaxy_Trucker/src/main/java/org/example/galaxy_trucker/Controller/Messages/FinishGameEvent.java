package org.example.galaxy_trucker.Controller.Messages;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class FinishGameEvent implements Event {

    boolean win;

    public FinishGameEvent(@JsonProperty("win") boolean win) {
        this.win = win;
    }

    @Override
    public void accept(EventVisitor visitor) {

    }

    @Override
    public String message() {
        return "";
    }

    public boolean isWin() {
        return win;
    }
}
