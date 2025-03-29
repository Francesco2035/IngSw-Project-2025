package org.example.galaxy_trucker.Controller.CardControllers;

import org.example.galaxy_trucker.Model.InputHandlers.GiveAttack;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;
import java.util.ArrayList;


public class GiveAttackInputController implements CardInputController {

    ArrayList<IntegerPair> coords;
    Player cur;

    public GiveAttackInputController(ArrayList<IntegerPair> c, Player p) {
        this.coords=c;
        cur = p;
    }

    @Override
    public void setInput() {
        GiveAttack handler = (GiveAttack) cur.getInputHandler();
         handler.setInput(coords);
         cur.execute();
    }

}
