package org.example.galaxy_trucker.View.ClientModel.States;

import java.util.List;

public class AcceptClient  extends PlayerStateClient{
    @Override
    public List<String> getCommands() {
        return List.of("Accept", "Decline");
    }
}
