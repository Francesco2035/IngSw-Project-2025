package org.example.galaxy_trucker.Model.Tiles.ComponentCheckers;

import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Tiles.Component;
import org.example.galaxy_trucker.Model.Tiles.Tile;
import org.example.galaxy_trucker.Model.Tiles.alienAddons;
import org.example.galaxy_trucker.Model.Tiles.modularHousingUnit;

public class ModularHousingUnitChecker implements ComponentChecker{


    PlayerBoard pb;
    int x;
    int y;
    modularHousingUnit component;

    public ModularHousingUnitChecker(PlayerBoard pb, int x, int y, modularHousingUnit component){
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


        if(         pb.getClassifiedTiles().containsKey(alienAddons.class) &&
                pb.getClassifiedTiles().get(alienAddons.class).contains(new IntegerPair(x,y-1))){
            if (tile.getConnectors().get(0).checkAdjacent(pb.getTile(x,y-1).getConnectors().get(2))){
                if (vb[x][y-1] == 1 && pb.getTile(x,y-1).getComponent().getAbility() == 1){
                    component.setNearPurple(true);
                }
                else {
                    component.setNearBrown(true);
                }
            }
        }

        if(         pb.getClassifiedTiles().containsKey(alienAddons.class) &&
                pb.getClassifiedTiles().get(alienAddons.class).contains(new IntegerPair(x-1,y))){
            if (tile.getConnectors().get(1).checkAdjacent((pb.getTile(x-1,y).getConnectors().get(3)))){
                if (vb[x-1][y] == 1 && pb.getTile(x-1,y).getComponent().getAbility() == 1){
                    component.setNearPurple(true);
                }
                else {
                    component.setNearBrown(true);
                }
            }
        }

        if(         pb.getClassifiedTiles().containsKey(alienAddons.class) &&
                pb.getClassifiedTiles().get(alienAddons.class).contains(new IntegerPair(x,y+1))){
            if (tile.getConnectors().get(2).checkAdjacent(pb.getTile(x,y+1).getConnectors().get(0))){
                if (vb[x][y+1] == 1 && pb.getTile(x,y+1).getComponent().getAbility() == 1){
                    component.setNearPurple(true);
                }
                else {
                    component.setNearBrown(true);
                }
            }
        }


        if(         pb.getClassifiedTiles().containsKey(alienAddons.class) &&
                pb.getClassifiedTiles().get(alienAddons.class).contains(new IntegerPair(x+1,y))){
            if (tile.getConnectors().get(3).checkAdjacent (pb.getTile(x+1,y).getConnectors().get(1))){
                if (vb[x + 1][y] == 1 && pb.getTile(x+1,y).getComponent().getAbility() == 1){
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
