package org.example.galaxy_trucker.Controller;

import org.example.galaxy_trucker.Model.Player;

public class CardsController extends Controller {


    public CardsController(Player curPlayer, String gameId,boolean disconnected) {
        this.curPlayer = curPlayer;
        this.gameId = gameId;
        this.disconnected = disconnected;
    }

    @Override
    public void nextState(GameController gc) {
        if (curPlayer.getCommonBoard().getCardStack().getFullAdventure().isEmpty()){
            gc.setGameOver();
        }
        else{
            gc.setControllerMap(curPlayer,new FlightController(curPlayer, gameId, gc, disconnected));
        }
    }
}
