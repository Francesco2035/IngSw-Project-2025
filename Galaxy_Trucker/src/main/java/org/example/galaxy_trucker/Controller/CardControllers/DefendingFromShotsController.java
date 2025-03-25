package org.example.galaxy_trucker.Controller.CardControllers;

import org.example.galaxy_trucker.Model.InputHandlers.DefendingFromShots;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;


public class DefendingFromShotsController implements CardInputController{
    Player cur;
    IntegerPair coords;

    public DefendingFromShotsController(IntegerPair coordinates, Player p) {
        cur = p;
        coords = coordinates;
    }

    @Override
    public void setInput() {
        DefendingFromShots handler = (DefendingFromShots) cur.getInputHandler();
        handler.setInput(coords);
        cur.execute();
    }
}
