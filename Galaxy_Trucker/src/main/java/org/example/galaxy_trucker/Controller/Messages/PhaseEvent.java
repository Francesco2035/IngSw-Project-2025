package org.example.galaxy_trucker.Controller.Messages;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.galaxy_trucker.View.ClientModel.States.PlayerStateClient;

public class PhaseEvent implements Event{

    private PlayerStateClient stateClient;



    @JsonCreator
    public PhaseEvent(@JsonProperty("phase") PlayerStateClient stateClient){
        this.stateClient = stateClient;
    }

    @Override
    public void accept(EventVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String message() {
        return "";
    }

    public PlayerStateClient getStateClient() {
        return stateClient;
    }
}
