package org.example.galaxy_trucker.Controller;

import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;

public class PostPrepController extends Controller {

    private Player CurrentPlayer;

    public void DestroyTile(IntegerPair coords){
        CurrentPlayer.getmyPlayerBoard().destroy(coords.getFirst(),  coords.getSecond());
    }

    @Override
    public void nextState(GameHandler gh) {

    }
}
