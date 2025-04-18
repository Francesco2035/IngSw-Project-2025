package org.example.galaxy_trucker.Controller;

import org.example.galaxy_trucker.Model.Player;

public class CardsController extends Controller {


    public CardsController(Player curPlayer, String gameId) {
        this.curPlayer = curPlayer;
        this.gameId = gameId;
    }

    @Override
    public void nextState(GameController gc) {
        if (curPlayer.getCommonBoard().getCardStack().getFullAdventure().isEmpty()){
            //gestisce la fine del gioco
        }
        else{
            gc.setControllerMap(curPlayer,new FlightController(curPlayer, gameId));
        }
    }
}
