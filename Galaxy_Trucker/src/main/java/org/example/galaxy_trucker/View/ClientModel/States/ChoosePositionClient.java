package org.example.galaxy_trucker.View.ClientModel.States;

import java.util.List;

public class ChoosePositionClient  extends PlayerStateClient{
    @Override
    public List<String> getCommands() {
        return List.of("FinishBuilding");
    }
}
