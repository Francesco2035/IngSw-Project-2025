package org.example.galaxy_trucker.Model.Tiles;

import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.IntegerPair;



import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public class ShieldGenerator extends Component{


    private ArrayList<Integer> protectedDirections = new ArrayList<>(Arrays.asList(0, 1, 1, 0));
    public ShieldGenerator() {}

    public ArrayList<Integer> getProtectedDirections() {
        return protectedDirections;
    }

    public void setProtectedDirections(ArrayList<Integer> protectedDirections) {
        this.protectedDirections = protectedDirections;
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
    public void insert(PlayerBoard playerBoard) {
        playerBoard.getShieldGenerators().add(this);
    }

    @Override
    public void remove(PlayerBoard playerBoard) {
        playerBoard.getShieldGenerators().remove(this);
    }


}






//    @Override
//    public int getAbility() {
//        return 0;
//    }
//
//    @Override
//    public ArrayList<Goods> getAbility(Goods good) {
//        return null;
//    }
//
//
//    @Override
//    public ArrayList<Integer> getAbility(int integer) {
//        return this.protectedDirections;
//    }
//
//    @Override
//    public int setAbility() {
//        return 0;
//    }
//
//    @Override
//    public int setAbility(int numAbility, boolean purpleAlien, boolean brownAlien) {
//        return 0;
//    }
//
//    @Override
//    public int setAbility(Goods good, boolean select) {
//        return 0;
//    }
//
//    @Override
//    public void setAbility(boolean direzione){
//        if (direzione){Collections.rotate(this.protectedDirections, 1);}
//        else {Collections.rotate(this.protectedDirections, -1);}
//    }
//