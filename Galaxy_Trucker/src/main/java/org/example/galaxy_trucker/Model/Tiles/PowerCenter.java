package org.example.galaxy_trucker.Model.Tiles;

import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.IntegerPair;

import java.util.ArrayList;
import java.util.HashMap;

public class PowerCenter extends Component{



    private int Energy;

    public PowerCenter(int numEnergy) {
        this.Energy = numEnergy;
    }


    public PowerCenter() {}


    public int getEnergy() {
        return Energy;
    }
    
    public void setEnergy(int Energy) {
        this.Energy = Energy;
    }




    @Override
    public void initType() {

    }

//    @Override
//    public void initType(int numHumans, boolean purpleAlien, boolean brownAlien) {
//
//    }

    @Override
    public void rotate(Boolean direction) {}

    @Override
    public boolean controlValidity(PlayerBoard pb, int x, int y) {
        return true;
    }

    @Override
    public void insert(PlayerBoard playerBoard) {
        playerBoard.getPowerCenters().add(this);
    }

    @Override
    public void remove(PlayerBoard playerBoard) {
        playerBoard.getPowerCenters().remove(this);
    }

}





//
//    @Override
//    public int getAbility() {
//        return Energy;
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
//
//    @Override
//    public int setAbility() {
//        try {
//            if(this.getAbility() == 0) throw new IllegalArgumentException("cannot exceed 0");
//            else this.Energy--;
//        } catch (ArithmeticException e) {
//            e.printStackTrace();
//        }
//        return this.Energy;
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
//    public void setAbility(boolean direzione) {}
//
//    @Override
//    public boolean controlValidity(PlayerBoard pb, int x, int y) {
//        return true;
//    }
//
//
//    @Override
//    public void initType() {
//        if(type.equals("double")) setEnergy(2);
//        else if (type.equals("triple")) setEnergy(3);
//    }
//
//    @Override
//    public void initType(int numHumans, boolean purpleAlien, boolean brownAlien) {}

