package org.example.galaxy_trucker.Controller.CardControllers;


import org.example.galaxy_trucker.Model.InputHandlers.ChoosingPlanet;
import org.example.galaxy_trucker.Model.Player;


public class ChoosingPlanetController implements CardInputController{
    Player cur;
    int planet;
    boolean choice;

    public ChoosingPlanetController(int planet, boolean accepted, Player p) {
        cur = p;
    }

    @Override
    public void setInput() {
        ChoosingPlanet handler = (ChoosingPlanet) cur.getInputHandler();
        handler.setInput(planet, choice);
        cur.execute();
    }
}
