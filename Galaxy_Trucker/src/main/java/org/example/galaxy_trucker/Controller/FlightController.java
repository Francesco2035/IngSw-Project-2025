package org.example.galaxy_trucker.Controller;

import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.State;

import java.io.File;

public class FlightController extends Controller {

    //attributi privati letti dal json

    Player curPlayer;

//    public FlightController(File json, State GameState, Player curPlayer) {
//        this.curPlayer = curPlayer;
//    }


    public FlightController(Player curPlayer) {
        this.curPlayer = curPlayer;
    }


    @Override
    public void nextState(GameHandler gh) {

    }
}
