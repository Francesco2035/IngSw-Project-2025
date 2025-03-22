package org.example.galaxy_trucker.Model.Tiles;

import org.example.galaxy_trucker.Model.Boards.Goods;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;

import java.util.ArrayList;

public class powerCenter extends Component{



    private int privEnergy;

    public powerCenter(int numEnergy) {
        this.privEnergy = numEnergy;
    }


    public powerCenter() {}



    public int getPrivEnergy() {
        return privEnergy;}
    public void setPrivEnergy(int privEnergy) {
        this.privEnergy = privEnergy;}




    @Override
    public int getAbility() {
        return privEnergy;
    }

    @Override
    public ArrayList<Goods> getAbility(Goods good) {
        return null;
    }

    @Override
    public ArrayList<Integer> getAbility(int integer) {
        return null;
    }


    @Override
    public int setAbility() {
        try {
            if(this.getAbility() == 0) throw new IllegalArgumentException("cannot exceed 0");
            else this.privEnergy--;
        } catch (ArithmeticException e) {
            e.printStackTrace();
        }
        return this.privEnergy;
    }

    @Override
    public int setAbility(int numAbility, boolean purpleAlien, boolean brownAlien) {
        return 0;
    }

    @Override
    public int setAbility(Goods good, boolean select) {
        return 0;
    }

    @Override
    public void setAbility(boolean direzione) {

    }

    @Override
    public boolean controlValidity(PlayerBoard pb, int x, int y, Tile tile) {
        return true;
    }


    @Override
    public void initType() {
        if(type.equals("double")) setPrivEnergy(2);
        else if (type.equals("triple")) setPrivEnergy(3);
    }

    @Override
    public void initType(int numHumans, boolean purpleAlien, boolean brownAlien) {

    }
}

