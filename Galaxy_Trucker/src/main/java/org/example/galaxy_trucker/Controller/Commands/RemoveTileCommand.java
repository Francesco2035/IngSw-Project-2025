package org.example.galaxy_trucker.Controller.Commands;

import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;

public class RemoveTileCommand implements Command {
    Player player;
    IntegerPair tile;

    public RemoveTileCommand(Player player, IntegerPair tile) {
        this.player = player;
        this.tile = tile;
    }

    @Override
    public void execute() {
        player.getmyPlayerBoard().removeTile(tile.getFirst(), tile.getSecond());
    }
}
