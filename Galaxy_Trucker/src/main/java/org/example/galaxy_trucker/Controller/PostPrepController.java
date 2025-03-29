package org.example.galaxy_trucker.Controller;

import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;

public class PostPrepController {

    private Player CurrentPlayer;

    public void DestroyTile(IntegerPair coords){
        CurrentPlayer.getMyPlance().destroy(coords.getFirst(),  coords.getSecond());
    }
}
