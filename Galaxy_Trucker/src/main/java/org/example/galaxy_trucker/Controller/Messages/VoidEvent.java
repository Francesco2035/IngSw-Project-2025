package org.example.galaxy_trucker.Controller.Messages;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class VoidEvent implements Event {

    private int x;
    private int y;

    @JsonCreator
    public VoidEvent(
            @JsonProperty("x") int x,
            @JsonProperty("y") int y
    ) {
        this.x = x;
        this.y = y;
    }

    public VoidEvent() {}

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public String message() {
        return "VirtualView settata";
    }

    @Override
    public void accept(EventVisitor visitor) {
        visitor.visit(this);
    }

}
