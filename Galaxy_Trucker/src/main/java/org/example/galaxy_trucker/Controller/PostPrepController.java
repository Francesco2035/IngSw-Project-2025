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
    public void nextState(GameHandler gh) {
        if (curPlayer.getmyPlayerBoard().checkValidity()){
            gh.setGameMap(gameId,curPlayer,new FlightController(curPlayer, gameId));
        }
        gh.setGameMap(gameId,curPlayer,new PostPrepController(curPlayer, gameId));
    }
}
