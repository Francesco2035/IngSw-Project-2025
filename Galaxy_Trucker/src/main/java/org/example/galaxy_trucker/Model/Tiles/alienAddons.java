package org.example.galaxy_trucker.Model.Tiles;

import org.example.galaxy_trucker.Model.Goods.Goods;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;

import java.util.ArrayList;

public class alienAddons extends Component{


//purple=true, brown=false
    private boolean whatColor;

    public alienAddons() {}


    public boolean isWhatColor() {
        return whatColor;
    }

    public void setWhatColor(boolean whatColor) {
        this.whatColor = whatColor;
    }


    @Override
    public int getAbility() {
        if (whatColor) {
            return 1;
        } else return 0;
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
    public void setAbility(boolean direzione) {

    }

    @Override
    public boolean controlValidity(PlayerBoard pb, int x, int y) {
        return true;
    }

    @Override
    public void initType(){
        if(type.equals("purple")) whatColor = true;
        else if(type.equals("brown")) whatColor = false;
    }

    @Override
    public void initType(int numHumans, boolean purpleAlien, boolean brownAlien) {

    }

    @Override
    public String toString(){
        return "alienAddons";
    }




}
