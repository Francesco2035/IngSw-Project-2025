package org.example.galaxy_trucker.Controller;

import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;

public class PrepController extends Controller {


    public PrepController(Player currentPlayer) {
        curPlayer = currentPlayer;
    }

    @Override
    public void nextState(GameHandler gh) {
        if (curPlayer.getmyPlayerBoard().checkValidity()){
            gh.getControllerMap().put(curPlayer.GetID(), new FlightController(curPlayer));
        }
        gh.getControllerMap().put(curPlayer.GetID(), new PostPrepController(curPlayer));
    }
}
