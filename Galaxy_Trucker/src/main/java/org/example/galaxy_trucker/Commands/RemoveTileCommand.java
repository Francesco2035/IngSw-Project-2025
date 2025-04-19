package org.example.galaxy_trucker.Commands;

import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.PlayerState;

public class RemoveTileCommand extends Command {

    IntegerPair tile;

    public RemoveTileCommand(IntegerPair tile,String gameId, String playerId, int lv, String title) {
        super(gameId, playerId, lv, title);
        this.tile = tile;
    }

    @Override
    public boolean allowedIn(PlayerState playerState) {
        return playerState.allows(this);
    }

    @Override
    public void execute(Player player) {
        player.getmyPlayerBoard().removeTile(tile.getFirst(), tile.getSecond());
    }
}
