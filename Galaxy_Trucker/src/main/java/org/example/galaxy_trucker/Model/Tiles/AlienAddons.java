package org.example.galaxy_trucker.Model.Tiles;

import org.example.galaxy_trucker.Model.Boards.Actions.ComponentActionVisitor;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.PlayerStates;

import java.util.ArrayList;
import java.util.HashMap;

public class AlienAddons extends Component{


//purple=true, brown=false
    private boolean whatColor;

    public AlienAddons() {}

    public boolean isWhatColor() {
        return whatColor;
    }

    public void setWhatColor(boolean whatColor) {
        this.whatColor = whatColor;
    }

    @Override
    public boolean controlValidity(PlayerBoard pb, int x, int y) {
        return true;
    }

    @Override
    public void accept(ComponentActionVisitor visitor, PlayerStates State) {

    }

    @Override
    public void initType(){
        if(type==1) whatColor = true;
        else if(type==2) whatColor = false;
    }

    @Override
    public void rotate(Boolean direction) {}

    @Override
    public void insert(PlayerBoard playerBoard) {
        playerBoard.getAlienAddons().add(this);
    }

    @Override
    public void remove(PlayerBoard playerBoard) {
        playerBoard.getAlienAddons().remove(this);
    }




}
