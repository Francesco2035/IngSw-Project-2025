package org.example.galaxy_trucker.Controller;

import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.BaseState;

public class FlightController extends Controller {


    public FlightController(Player curPlayer, String gameId) {
        this.curPlayer = curPlayer;
        this.gameId = gameId;
        curPlayer.setState(new BaseState());
    }



    @Override
    public void nextState(GameController gc) {
        curPlayer.getCommonBoard().NewCard();
        gc.setControllerMap(curPlayer, new CardsController(curPlayer, gameId));
    }
}
