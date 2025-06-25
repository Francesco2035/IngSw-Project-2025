package org.example.galaxy_trucker.Commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.PlayerState;

import java.io.Serializable;

public class Theft extends Command implements Serializable {

    @JsonProperty("pair")
    IntegerPair pair;
    @JsonProperty("position")
    int position;


    public Theft(){}

    public Theft(int position,IntegerPair pair,String gameId, String playerId, int lv, String title, String token) {
        super(gameId, playerId, lv, title, token,-1);
        this.pair = pair;
        this.position = position;
    }

    @Override
    public boolean allowedIn(PlayerState playerState) {
        return playerState.allows(this);
    }

    @Override
    public void execute(Player player) {
        try {
            player.getCurrentCard().loseCargo(pair,position);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
