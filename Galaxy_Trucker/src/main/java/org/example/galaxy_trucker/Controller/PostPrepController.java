package org.example.galaxy_trucker.Controller;

import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;

public class PostPrepController extends Controller {

    private Player curPlayer;

//    public void DestroyTile(IntegerPair coords){
//        CurrentPlayer.getmyPlayerBoard().destroy(coords.getFirst(),  coords.getSecond());
//    }


    public PostPrepController(Player curPlayer) {
        this.curPlayer = curPlayer;
    }

    @Override
    public void nextState(GameHandler gh) {
        gh.getControllerMap().put(curPlayer.GetID(), new FlightController(curPlayer));
    }
}
