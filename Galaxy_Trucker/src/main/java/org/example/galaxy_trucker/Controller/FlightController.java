package org.example.galaxy_trucker.Controller;

import org.example.galaxy_trucker.Commands.Command;
import org.example.galaxy_trucker.Commands.ReadyCommand;
import org.example.galaxy_trucker.Model.Boards.GameBoard;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.BaseState;

public class FlightController extends Controller {


    public FlightController(Player curPlayer, String gameId, GameController gc) {
        this.curPlayer = curPlayer;
        this.gameId = gameId;
        curPlayer.setState(new BaseState());
    }



    @Override
    public void nextState(GameController gc) {
        gc.setControllerMap(curPlayer, new CardsController(curPlayer, gameId));
    }
}
