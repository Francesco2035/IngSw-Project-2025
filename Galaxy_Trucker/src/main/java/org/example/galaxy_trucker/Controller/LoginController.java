package org.example.galaxy_trucker.Controller;

import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.BuildingShip;

public class LoginController extends Controller {


    public LoginController(Player curPlayer, String gameId) {
        this.curPlayer = curPlayer;
        this.gameId = gameId;
    }

    @Override
    public void nextState(GameHandler gh) {
        curPlayer.setState(new BuildingShip());
        PrepController prep  = new PrepController(curPlayer, gameId);
        curPlayer.getCommonBoard().getHourglass().setListener(prep);
        gh.setGameMap(gameId,curPlayer,prep);
    }
}