package org.example.galaxy_trucker.Model.Tiles;

import org.example.galaxy_trucker.Exceptions.InvalidInput;
import org.example.galaxy_trucker.Model.Boards.Actions.ComponentAction;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.PlayerStates.PlayerState;
import org.example.galaxy_trucker.Model.PlayerStatesss;

public class MainCockpitComp extends HousingUnit {

    private int numHumans;


    @Override
    public int getNumHumans() {
        return this.numHumans;
    }

    public void setNumHumans(int numHumans) {
        this.numHumans = numHumans;
    }

    @Override
    public boolean isPurpleAlien() {
        return false;
    }

    @Override
    public void setPurpleAlien(boolean purpleAlien) throws IllegalArgumentException {
        if (purpleAlien) {
            throw new IllegalArgumentException("MainCockpit can't have aliens");
        }

    }

    @Override
    public boolean isBrownAlien() {
        return false;
    }

    @Override
    public void setBrownAlien(boolean brownAlien) {
        if (brownAlien) {
            throw new IllegalArgumentException("MainCockpit can't have aliens");
        }
    }

    @Override
    public boolean isNearPurpleAddon() {
        return false;
    }

    @Override
    public void setNearPurpleAddon(boolean nearPurpleAddon) {
        throw new IllegalArgumentException("can't have aliens");
    }

    @Override
    public boolean isNearBrownAddon() {
        return false;
    }

    @Override
    public void setNearBrownAddon(boolean nearBrownAddon) {
        throw new IllegalArgumentException("can't have aliens");
    }


    @Override
    public boolean controlValidity(PlayerBoard pb, int x, int y) {
        return true;
    }



    @Override
    public void accept(ComponentAction visitor, PlayerState State) {

        visitor.visit(this, State);
    }


    @Override
    public int kill(){
        if (numHumans == 0){
            throw new InvalidInput("MainCockPit is empty!");
        }
            numHumans--;
            return 2;
    }



    @Override
    public void addCrew(int humans, boolean purple, boolean brown){
        if (purple || brown){
            throw new InvalidInput("This is the mainCockPit you can't add aliens");
        }

        if (numHumans + humans > 2){
            throw new InvalidInput("Human amount is greater than 2");
        }

        numHumans += humans;

    }

    @Override
    public Component clone(){
        ModularHousingUnit clone = new ModularHousingUnit();
        clone.setNumHumans(numHumans);
        return clone;
    }

}

