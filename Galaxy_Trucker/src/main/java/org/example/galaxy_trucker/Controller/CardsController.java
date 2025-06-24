package org.example.galaxy_trucker.Controller;

import org.example.galaxy_trucker.Model.Player;

public class CardsController extends Controller {


    public CardsController(Player curPlayer, String gameId,boolean disconnected) {
        this.curPlayer = curPlayer;
        this.gameId = gameId;
        //this.disconnected = disconnected;
    }

    @Override
    public void nextState(GameController gc) {
//        if (!gc.getVirtualViewMap().get(curPlayer.GetID()).getDisconnected()){ ///  la virtual view sa sempre se è disconnesso, questo è il caso in cui il player si sia riconnesso
//            this.setDisconnected(false);
//        }
        if (curPlayer.getCommonBoard().getCardStack().getFullAdventure().isEmpty()){
            gc.setGameOver();
        }

        else{
            FlightController newController = new FlightController(curPlayer, gameId, gc, getDisconnected());
            newController.setExceptionListener(exceptionListener);
            gc.setFlightCount(1);
            gc.setControllerMap(curPlayer,newController);
        }
    }
}
