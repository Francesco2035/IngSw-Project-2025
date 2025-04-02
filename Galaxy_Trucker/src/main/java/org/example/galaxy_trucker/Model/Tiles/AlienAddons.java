package org.example.galaxy_trucker.Model.Tiles;

import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.IntegerPair;

import java.util.ArrayList;
import java.util.HashMap;

public class AlienAddons extends Component{


//purple=true, brown=false

    public AlienAddons() {}

    @Override
    public boolean controlValidity(PlayerBoard pb, int x, int y) {
        return true;
    }

    @Override
    public void rotate(Boolean direction){}

    @Override
    public void insert(PlayerBoard playerBoard) {
        playerBoard.getAlienAddons().add(this);
    }

    @Override
    public void remove(PlayerBoard playerBoard) {
        playerBoard.getAlienAddons().remove(this);
    }


    @Override
    public void setType(int type){
    }
}





//
//    @Override
//    public int getAbility() {
//        if (whatColor) {
//            return 1;
//        } else return 0;
//    }
//
//    @Override
//    public ArrayList<Goods> getAbility(Goods good) {
//        return null;
//    }
//
//    @Override
//    public ArrayList<Integer> getAbility(int integer) {
//        return null;
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
//    public void setAbility(boolean direzione) {
//
//    }
//