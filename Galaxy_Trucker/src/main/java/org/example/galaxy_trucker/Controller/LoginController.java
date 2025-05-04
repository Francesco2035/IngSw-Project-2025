package org.example.galaxy_trucker.Controller;

import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.BuildingShip;

public class LoginController extends Controller {


    public LoginController(Player curPlayer, String gameId) {
        this.curPlayer = curPlayer;
        this.gameId = gameId;
        this.disconnected = false;
    }

    @Override
    public void nextState(GameController gc) {
        curPlayer.setState(new BuildingShip());
        PrepController prep  = new PrepController(curPlayer, gameId, gc,disconnected);
        curPlayer.getCommonBoard().getHourglass().setListener(prep);
        gc.setBuildingCount(1);
        gc.setControllerMap(curPlayer,prep);
    }
}