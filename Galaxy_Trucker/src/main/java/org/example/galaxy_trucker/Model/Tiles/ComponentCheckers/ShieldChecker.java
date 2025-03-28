package org.example.galaxy_trucker.Model.Tiles.ComponentCheckers;

import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.Tiles.Component;

import java.util.ArrayList;
import java.util.Objects;

public class ShieldChecker implements ComponentChecker{

    int[] pb_shield;
    ArrayList<Integer> c_shield;
    public ShieldChecker(PlayerBoard playerBoard, Component component) {
        pb_shield = playerBoard.getShield();
        c_shield = component.getAbility(0);

    }

    @Override
    public boolean Check(){
        for (int i = 0; i < pb_shield.length; i++){
            pb_shield[i] += (int)c_shield.get(i);
        }
        return true;
    }

}
