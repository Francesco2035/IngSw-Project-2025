package org.example.galaxy_trucker.Controller.Messages;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PhaseEvent implements Event{

    private int phase;



    @JsonCreator
    public PhaseEvent(@JsonProperty("phase") int phase){
        this.phase = phase;
    }

    @Override
    public void accept(EventVisitor visitor) {

    }

    @Override
    public String message() {
        return "";
    }
}
