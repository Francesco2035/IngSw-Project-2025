package org.example.galaxy_trucker.Commands;

import org.example.galaxy_trucker.Model.Cards.Card;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.PlayerState;

import java.util.ArrayList;

public class KillCommand extends Command {
    ArrayList<IntegerPair> coordinates;

    public KillCommand(ArrayList<IntegerPair> coordinates,String gameId, String playerId, int lv, String title, String token) {
        super(gameId, playerId, lv, title, token);
        this.coordinates = coordinates;
    }

    @Override
    public void execute(Player player) {
        player.getCurrentCard().killHumans(coordinates);
    }

    @Override
    public boolean allowedIn(PlayerState playerState) {
        return playerState.allows(this);
    }

}

