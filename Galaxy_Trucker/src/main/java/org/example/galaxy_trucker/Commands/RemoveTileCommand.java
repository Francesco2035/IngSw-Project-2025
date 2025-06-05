package org.example.galaxy_trucker.Commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.PlayerState;

import java.io.Serializable;

public class RemoveTileCommand extends Command implements Serializable {

    @JsonProperty("commandType")
    private final String commandType = "RemoveTileCommand";


    //IntegerPair tile;
    @JsonProperty("x")
    int x;
    @JsonProperty("y")
    int y;

    public RemoveTileCommand(int x, int y,String gameId, String playerId, int lv, String title, String token) {
        super(gameId, playerId, lv, title, token,-1);
        this.x = x;
        this.y = y;
        //this.tile = new IntegerPair(x,y);
    }

    public RemoveTileCommand(){

    }

    @Override
    public boolean allowedIn(PlayerState playerState) {
        return playerState.allows(this);
    }

    @Override
    public void execute(Player player) {
        player.getmyPlayerBoard().removeTile(x, y);
    }
}
