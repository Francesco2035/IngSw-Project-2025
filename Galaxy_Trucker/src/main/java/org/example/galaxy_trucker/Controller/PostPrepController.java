package org.example.galaxy_trucker.Controller;

import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.CheckValidity;

public class PostPrepController extends Controller {

    public PostPrepController(Player curPlayer, String gameId) {
        this.curPlayer = curPlayer;
        this.gameId = gameId;
    }

    @Override
    public void nextState(GameController gc) {
        if (curPlayer.getmyPlayerBoard().checkValidity()){
            gc.setControllerMap(curPlayer,new FlightController(curPlayer, gameId));
        }
        gc.setControllerMap(curPlayer,new PostPrepController(curPlayer, gameId));
    }
}
