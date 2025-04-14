package org.example.galaxy_trucker.Controller;

import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.CheckValidity;

public class PostPrepController extends Controller {

    public PostPrepController(Player curPlayer) {
        this.curPlayer = curPlayer;
    }

    @Override
    public void nextState(GameHandler gh) {
        if (curPlayer.getmyPlayerBoard().checkValidity()){
            gh.getControllerMap().put(curPlayer.GetID(), new FlightController(curPlayer));
        }
        gh.getControllerMap().put(curPlayer.GetID(), new PostPrepController(curPlayer));
    }
}
