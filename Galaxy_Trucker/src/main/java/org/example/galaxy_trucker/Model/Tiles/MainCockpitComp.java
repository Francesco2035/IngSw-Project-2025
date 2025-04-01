package org.example.galaxy_trucker.Model.Tiles;

import org.example.galaxy_trucker.Model.Goods.*;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;

import java.util.ArrayList;

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
}

