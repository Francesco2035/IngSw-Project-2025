package org.example.galaxy_trucker.Controller;

import org.example.galaxy_trucker.Model.Player;

public class CardsController extends Controller {


    public CardsController(Player curPlayer, String gameId) {
        this.curPlayer = curPlayer;
        this.gameId = gameId;
    }

    @Override
    public void nextState(GameHandler gh) {
        if (curPlayer.getCommonBoard().getCardStack().getFullAdventure().isEmpty()){
            //gestisce la fine del gioco
        }
        else{
            gh.setGameMap(gameId,curPlayer,new FlightController(curPlayer, gameId));
        }
    }
}
