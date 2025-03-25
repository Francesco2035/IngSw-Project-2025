package org.example.galaxy_trucker.Controller.CardControllers;

import org.example.galaxy_trucker.Model.InputHandlers.Accept;

import org.example.galaxy_trucker.Model.Player;


public class AcceptController implements CardInputController {
    Player cur;
    boolean choice;

    public AcceptController(boolean choice, Player p) {
        this.choice = choice;
        cur = p;
    }

    @Override
    public void setInput() {
        Accept handler = (Accept) cur.getInputHandler();
        handler.setInput(choice);
        cur.execute();
    }
}
