package org.example.galaxy_trucker.Controller.CardControllers;


import org.example.galaxy_trucker.Model.InputHandlers.Killing;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;
import java.util.ArrayList;


public class KillingController implements CardInputController {
    Player cur;
    ArrayList<IntegerPair> coords;

    public KillingController(ArrayList<IntegerPair> coordinates, Player p) {
        cur = p;
        coords = coordinates;
    }

    @Override
    public void setInput() {
        Killing handler = (Killing) cur.getInputHandler();
        handler.setInput(coords);
        cur.execute();
    }

}
