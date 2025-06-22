package org.example.galaxy_trucker.Controller;

import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.BuildingShip;

public class LoginController extends Controller {


    public LoginController(Player curPlayer, String gameId) {
        this.curPlayer = curPlayer;
        this.gameId = gameId;
        //this.disconnected = false;
    }

    @Override
    public void nextState(GameController gc) {
        System.out.println("login change state for "+ curPlayer.GetID());
        if (!gc.getVirtualViewMap().get(curPlayer.GetID()).getDisconnected()){ ///  la virtual view sa sempre se è disconnesso, questo è il caso in cui il player si sia riconnesso
            setDisconnected(false);
        }

        curPlayer.setState(new BuildingShip());
        ;
        PrepController newController  = new PrepController(curPlayer, gameId, gc, getDisconnected());
        curPlayer.getCommonBoard().getHourglass().setListener(newController);
        newController.setExceptionListener(exceptionListener);
        newController.setVv(gc.getVirtualViewMap().get(curPlayer.GetID()));
        gc.setBuildingCount(1);
        gc.setControllerMap(curPlayer,newController);
    }
}