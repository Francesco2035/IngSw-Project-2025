package org.example.galaxy_trucker.Model.Tiles.ComponentCheckers;

import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.Connectors.ENGINE;
import org.example.galaxy_trucker.Model.Tiles.Tile;

public class HotWaterHeatersChecker implements ComponentChecker{


    PlayerBoard pb;
    int x;
    int y;
    public HotWaterHeatersChecker(PlayerBoard pb, int x, int y){
        this.pb = pb;
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean Check(){
        Tile tile = pb.getTile(x,y);
        int[][] mat = pb.getValidPlayerBoard();

        int index = tile.getConnectors().indexOf((tile.getConnectors().stream().
                filter(p -> p.getClass().equals(ENGINE.class)).toList().getFirst()));


        if (index != 3 || (x+1 < 10 && mat[x + 1][y]==1) ){
            System.out.println(index + " " + x + " : " + y);
            return false;
        }
        return true;
    }

}
