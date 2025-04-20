package org.example.galaxy_trucker.Model.Tiles;

import org.example.galaxy_trucker.Controller.Messages.PlayerBoardEvents.RemoveTileEvent;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class ShieldGenerator extends Component{


    private ArrayList<Integer> protectedDirections = new ArrayList<>(Arrays.asList(0, 1, 1, 0));
    public ShieldGenerator() {}

    public ArrayList<Integer> getProtectedDirections() {
        return protectedDirections;
    }


    @Override
    public void rotate(Boolean direction) {
        if (direction){
            Collections.rotate(getProtectedDirections(), 1);}
        else {Collections.rotate(getProtectedDirections(), -1);}
    }


    @Override
    public boolean controlValidity(PlayerBoard pb, int x, int y) {
        int[] pb_shield = pb.getShield();
        for (int i = 0; i < pb_shield.length; i++){
            pb_shield[i] += protectedDirections.get(i);
        }

        return true;
    }


    @Override
    public void insert(PlayerBoard playerBoard, int x, int y) {
        playerBoard.getShieldGenerators().add(this);
        tile.sendUpdates(null,0, false, false, 0, "ShieldGenerator");

    }

    @Override
    public void remove(PlayerBoard playerBoard) {
        playerBoard.getShieldGenerators().remove(this);
        tile.sendUpdates(new RemoveTileEvent());

    }


    @Override
    public Component clone(PlayerBoard clonedPlayerBoard){
        ShieldGenerator clone = new ShieldGenerator();
        clone.protectedDirections = new ArrayList<>(this.protectedDirections);
        return clone;
    }



}


