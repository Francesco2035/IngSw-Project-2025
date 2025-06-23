package org.example.galaxy_trucker.Model.PlayerStates;

import org.example.galaxy_trucker.Model.Boards.Actions.KillCrewAction;

public class WaitingEpidemic extends Waiting{

    @Override
    public boolean allows(KillCrewAction action) {
        return true;
    }
}
