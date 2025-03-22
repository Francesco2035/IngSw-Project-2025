package org.example.galaxy_trucker.Model.Tiles;

import org.example.galaxy_trucker.Model.Boards.Goods;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;

import java.util.ArrayList;

public class MainCockpitComp extends Component {

    private int numHumans;
    private int color;
    private String type;

    public MainCockpitComp(int color) {

        this.color = color;
        this.type="MainCockpit";
    }

    public int getColor() {
        return color;
    }
    public void setNumHumans(int numHumans) {this.numHumans = numHumans;}


    @Override
    public int getAbility() {
        return numHumans;
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
    public void initType() {

    }

    @Override
    public void initType(int numHumans, boolean purpleAlien, boolean brownAlien) {
        this.numHumans = numHumans;
    }



    @Override
    public int setAbility(int numAbility, boolean purpleAlien, boolean brownAlien) {
        if(this.numHumans>0) this.numHumans -= numAbility;
        return numHumans;
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
        return false;
    }

    @Override
    public String getType() {
        return type;
    }


}

