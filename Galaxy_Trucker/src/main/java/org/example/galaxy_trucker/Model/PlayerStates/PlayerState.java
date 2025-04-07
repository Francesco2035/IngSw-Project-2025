package org.example.galaxy_trucker.Model.PlayerStates;
import org.example.galaxy_trucker.Controller.Commands.Command;
import org.example.galaxy_trucker.Model.Boards.Actions.*;
import org.example.galaxy_trucker.Model.Cards.Card;
import org.example.galaxy_trucker.Model.Player;

import java.util.Optional;

public abstract class PlayerState {
    public abstract Command PlayerAction(String json, Player player, Optional<Card> card);



    public boolean allows(AddCrewAction action) {
        return false;
    }

    public boolean allows(AddGoodAction action) {
        return false;
    }

    public boolean allows(GetEnginePower action) {
        return false;
    }

    public boolean allows(GetGoodAction action) {
        return false;
    }

    public boolean allows(GetPlasmaDrillPower action) {
        return false;
    }

    public boolean allows(KillCrewAction action) {
        return false;
    }

    public boolean allows(UseEnergyAction action) {
        return false;
    }



}
