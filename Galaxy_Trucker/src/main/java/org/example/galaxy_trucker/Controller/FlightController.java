package org.example.galaxy_trucker.Controller;

import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.BaseState;
import org.example.galaxy_trucker.Model.State;

import java.io.File;

public class FlightController extends Controller {


    public FlightController(Player curPlayer) {
        this.curPlayer = curPlayer;
        curPlayer.setState(new BaseState());
    }


    @Override
    public void nextState(GameHandler gh) {

    }
}
