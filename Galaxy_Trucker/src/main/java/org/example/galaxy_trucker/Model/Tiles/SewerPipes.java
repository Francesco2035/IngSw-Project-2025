package org.example.galaxy_trucker.Model.Tiles;

import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.IntegerPair;

import java.util.ArrayList;
import java.util.HashMap;

public class SewerPipes extends Component {



    @Override
    public void initType() {}

//    @Override
//    public void initType(int numHumans, boolean purpleAlien, boolean brownAlien) {}

    @Override
    public void rotate(Boolean direction) {

    }

//    @Override
//    public void initType(int numHumans, boolean purpleAlien, boolean brownAlien) {
//
//    }
//
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
    @Override
    public boolean controlValidity(PlayerBoard pb, int x, int y) {
        return true;
    }




}

