package org.example.galaxy_trucker.Controller.CardControllers;

import org.example.galaxy_trucker.Model.InputHandlers.DefendingFromMeteorites;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;


public class DefendingFromMeteoritesController implements CardInputController {
    Player cur;
    IntegerPair shield;
    IntegerPair cannon;
    IntegerPair energy;

    public DefendingFromMeteoritesController(IntegerPair cannonCoords, IntegerPair shieldCoords, IntegerPair energy,Player p) {
        cur = p;
        shield = cannonCoords;
        cannon = shieldCoords;
        energy = energy;
    }

    @Override
    public void setInput() {
        DefendingFromMeteorites handler = (DefendingFromMeteorites) cur.getInputHandler();
        handler.setInput(cannon,shield,energy);
        cur.execute();
    }
}
