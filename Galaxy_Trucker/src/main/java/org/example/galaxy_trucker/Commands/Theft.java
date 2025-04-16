package org.example.galaxy_trucker.Commands;

import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.PlayerState;

public class Theft extends Command {

    IntegerPair pair;
    int position;

    public Theft(int position,IntegerPair pair,String gameId, String playerId, int lv, String title) {
        super(gameId, playerId, lv, title);
        this.pair = pair;
        this.position = position;
    }

    @Override
    public boolean allowedIn(PlayerState playerState) {
        return playerState.allows(this);
    }

    @Override
    public void execute(Player player) {
        player.getCurrentCard().loseCargo(pair,position);
    }
}
