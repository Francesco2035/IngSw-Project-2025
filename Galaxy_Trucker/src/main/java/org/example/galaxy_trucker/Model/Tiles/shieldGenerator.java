package org.example.galaxy_trucker.Model.Tiles;

import org.example.galaxy_trucker.Model.Boards.Goods;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;

import java.util.ArrayList;
import java.util.Collections;

public class shieldGenerator extends Component{


    private ArrayList<Integer> protectedDirections;

    public shieldGenerator() {
    }

    @Override
    public void initType() {
        if (type.equals("nord-est")) {
            protectedDirections = new ArrayList<Integer>();
            protectedDirections.add(0);
            protectedDirections.add(1);
            protectedDirections.add(1);
            protectedDirections.add(0);
        }
    }

    @Override
    public void initType(int numHumans, boolean purpleAlien, boolean brownAlien) {

    }

    @Override
    public int getAbility() {
        return 0;
    }

    @Override
    public ArrayList<Goods> getAbility(Goods good) {
        return null;
    }


    @Override
    public ArrayList<Integer> getAbility(int integer) {
        return this.protectedDirections;
    }

    @Override
    public int setAbility() {
        return 0;
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
    public void setAbility(boolean direzione){
        if (direzione){Collections.rotate(this.protectedDirections, 1);}
        else {Collections.rotate(this.protectedDirections, -1);}
    }

    @Override
    public boolean controlValidity(PlayerBoard pb, int x, int y, Tile tile) {
        return true;
    }

    @Override
    public boolean getNearbyAddons(boolean type){
        return false;
    }



}


