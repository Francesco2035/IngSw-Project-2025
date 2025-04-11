package org.example.galaxy_trucker.Controller;

import org.example.galaxy_trucker.Model.Game;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.State;

import java.io.File;

public class LoginController extends Controller {


    Player curPlayer;
    public LoginController(Player curPlayer) {
        this.curPlayer = curPlayer;
    }

    @Override
    public void nextState(GameHandler gh) {
        gh.getControllerMap().put(curPlayer.GetID(), new PrepController(curPlayer));
    }
}