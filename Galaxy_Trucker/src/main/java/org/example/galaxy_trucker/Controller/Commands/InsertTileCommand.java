package org.example.galaxy_trucker.Controller.Commands;

import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.Tiles.Tile;

public class InsertTileCommand implements Command{

    private Tile tile;
    private int x;
    private int y;
    private PlayerBoard playerBoard;

    public InsertTileCommand(Tile tile, int x, int y, PlayerBoard playerBoard) {
        this.tile = tile;
        this.x = x;
        this.y = y;
        this.playerBoard = playerBoard;
    }

    @Override
    public void execute() {
        playerBoard.insertTile(tile, x, y);
    }
}
