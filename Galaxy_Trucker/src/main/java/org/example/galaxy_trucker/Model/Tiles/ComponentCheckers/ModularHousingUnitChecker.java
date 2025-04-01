package org.example.galaxy_trucker.Model.Tiles.ComponentCheckers;

import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Tiles.*;
import org.example.galaxy_trucker.Model.Tiles.ComponentGetters.AlienGetter;

public class ModularHousingUnitChecker implements ComponentChecker{


    PlayerBoard pb;
    int x;
    int y;
    ModularHousingUnit component;

    public ModularHousingUnitChecker(PlayerBoard pb, int x, int y, ModularHousingUnit component){
        this.pb = pb;
        this.x = x;
        this.y = y;
        this.component = component;

    }

    @Override
    public boolean Check() {
        Tile tile = pb.getTile(x,y);
        component.setNearPurple(false);
        component.setNearBrown(false);
        int[][] vb = pb.getValidPlayerBoard();


        if(         pb.getClassifiedTiles().containsKey(AlienAddons.class) &&
                pb.getClassifiedTiles().get(AlienAddons.class).contains(new IntegerPair(x,y-1))){
            if (tile.getConnectors().get(0).checkAdjacent(pb.getTile(x,y-1).getConnectors().get(2))){

                if (vb[x][y-1] == 1 && (boolean) pb.getTile(x, y - 1).getComponent().get(new AlienGetter(pb.getTile(x,y-1).getComponent()))){
                    component.setNearPurple(true);
                }
                else {
                    component.setNearBrown(true);
                }
            }
        }

        if(         pb.getClassifiedTiles().containsKey(AlienAddons.class) &&
                pb.getClassifiedTiles().get(AlienAddons.class).contains(new IntegerPair(x-1,y))){
            if (tile.getConnectors().get(1).checkAdjacent((pb.getTile(x-1,y).getConnectors().get(3)))){

                if (vb[x-1][y] == 1 && (boolean) pb.getTile(x-1,y).getComponent().get(new AlienGetter(pb.getTile(x-1,y).getComponent()))){
                    component.setNearPurple(true);
                }
                else {
                    component.setNearBrown(true);
                }
            }
        }

        if(         pb.getClassifiedTiles().containsKey(AlienAddons.class) &&
                pb.getClassifiedTiles().get(AlienAddons.class).contains(new IntegerPair(x,y+1))){
            if (tile.getConnectors().get(2).checkAdjacent(pb.getTile(x,y+1).getConnectors().get(0))){

                if (vb[x][y+1] == 1 && (boolean) pb.getTile(x,y+1).getComponent().get(new AlienGetter(pb.getTile(x,y+1).getComponent()))){
                    component.setNearPurple(true);
                }
                else {
                    component.setNearBrown(true);
                }
            }
        }


        if(         pb.getClassifiedTiles().containsKey(AlienAddons.class) &&
                pb.getClassifiedTiles().get(AlienAddons.class).contains(new IntegerPair(x+1,y))){
            if (tile.getConnectors().get(3).checkAdjacent (pb.getTile(x+1,y).getConnectors().get(1))){


                if (vb[x + 1][y] == 1 && (boolean) pb.getTile(x+1,y).getComponent().get(new AlienGetter(pb.getTile(x+1,y).getComponent()))){
                    component.setNearPurple(true);
                }
                else {
                    component.setNearBrown(true);
                }
            }
        }
        return true;
    }
}
