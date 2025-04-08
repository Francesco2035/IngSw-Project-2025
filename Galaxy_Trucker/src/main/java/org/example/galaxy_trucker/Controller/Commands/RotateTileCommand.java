package org.example.galaxy_trucker.Controller.Commands;

import org.example.galaxy_trucker.Model.Tiles.Tile;

public class RotateTileCommand implements Command {

    private Tile tile;
    private boolean direction;

    public RotateTileCommand(Tile tile, boolean direction) {
        this.tile = tile;
        this.direction = direction;
    }

    @Override
    public void execute() {
        if (direction) {
            tile.RotateDx();
        }
        else {
            tile.RotateSx();
        }
    }
}
