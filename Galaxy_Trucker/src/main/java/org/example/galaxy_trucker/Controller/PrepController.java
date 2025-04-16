package org.example.galaxy_trucker.Controller;

import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.CheckValidity;
import org.example.galaxy_trucker.Model.PlayerStates.Waiting;

public class PrepController extends Controller implements HourGlassListener {

    GameHandler gh;
    public PrepController(Player currentPlayer, String gameId, GameHandler gh) {
        curPlayer = currentPlayer;
        this.gameId = gameId;
        this.gh = gh;
    }

    @Override
    public void nextState(GameHandler gh) {
        if (curPlayer.getmyPlayerBoard().checkValidity()){
            curPlayer.setState(new Waiting());
            gh.setGameMap(gameId,curPlayer,new FlightController(curPlayer, gameId));
        }
        gh.setGameMap(gameId,curPlayer,new PostPrepController(curPlayer, gameId));
        curPlayer.setState(new CheckValidity());
    }

    @Override
    public void onFinish() {
        curPlayer.SetReady(true);
        gh.changeState(gameId);

    }
}
