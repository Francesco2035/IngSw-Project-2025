package org.example.galaxy_trucker.Model.Tiles;

import org.example.galaxy_trucker.Model.Boards.Actions.ComponentActionVisitor;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.PlayerStates;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class HousingUnit extends Component{



    public abstract int getNumHumans();


    public abstract void setNumHumans(int numHumans);

    public abstract boolean isPurpleAlien();

    public abstract void setPurpleAlien(boolean purpleAlien);

    public abstract boolean isBrownAlien();

    public abstract void setBrownAlien(boolean brownAlien);

    public abstract boolean isNearPurpleAddon();

    public abstract void setNearPurpleAddon(boolean nearPurpleAddon);

    public abstract boolean isNearBrownAddon();

    public abstract void setNearBrownAddon(boolean nearBrownAddon);


    @Override
    public void initType(){

    }

    @Override
    public void rotate(Boolean direction){

    }

    public abstract int kill();

    public abstract void accept(ComponentActionVisitor visitor, PlayerStates State);

    public abstract void addCrew(int humans, boolean purple, boolean brown);
}
