package org.example.galaxy_trucker.Controller.Messages.TileSets;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.galaxy_trucker.Controller.Messages.Event;
import org.example.galaxy_trucker.Controller.Messages.EventVisitor;

public class LogEvent implements Event {


    @JsonProperty("effect")
    String effect;


    public LogEvent() {

    }

    public LogEvent(String effect) {
        this.effect = effect;
    }
    @Override
    public void accept(EventVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String message() {
        return effect;
    }
}
