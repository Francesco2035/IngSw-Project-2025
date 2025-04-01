package org.example.galaxy_trucker.Model.Tiles.ComponentCheckers;

import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.Connectors.CANNON;
import org.example.galaxy_trucker.Model.Tiles.Tile;

public class PlasmaDrillsChecker implements ComponentChecker{


    PlayerBoard pb;
    int x;
    int y;
    public PlasmaDrillsChecker(PlayerBoard pb, int x, int y){
        this.pb = pb;
        this.x = x;
        this.y = y;

    }

    @Override
    public boolean Check() {

        Tile tile = pb.getTile(x,y);
        int[][] mat = pb.getValidPlayerBoard();

        int index = tile.getConnectors().indexOf((tile.getConnectors().stream().
                filter(p -> p.getClass().equals(CANNON.class)).toList().getFirst()));

        if (index == 0 && y-1 >= 0 && mat[x][y-1]==1) return false;
        if (index == 1 && x-1 >= 0 && mat[x-1][y]==1) return false;
        if (index == 2 && y+1 < 10 && mat[x][y+1]==1) return false;
        if (index == 3 && x+1 < 10 && mat[x+1][y]==1) return false;
        return true;

    }

}
