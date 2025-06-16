package org.example.galaxy_trucker.Controller.Messages.TileSets;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.galaxy_trucker.Controller.Messages.Event;
import org.example.galaxy_trucker.Controller.Messages.EventVisitor;

public class LogEvent implements Event {


    String effect = "";


    @JsonCreator
    public LogEvent( @JsonProperty("effect") String Effect) {

        if (Effect == null){
            this.effect = "";
        }
        else {
            this.effect = Effect;
        }
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
