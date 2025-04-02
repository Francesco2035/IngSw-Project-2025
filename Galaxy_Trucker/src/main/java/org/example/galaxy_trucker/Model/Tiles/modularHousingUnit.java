package org.example.galaxy_trucker.Model.Tiles;

import org.example.galaxy_trucker.Model.Boards.PlayerBoard;

public class ModularHousingUnit extends HousingUnit {


    protected int numHumans;
    protected boolean purpleAlien;
    protected boolean brownAlien;
    protected boolean nearPurpleAddon;
    protected boolean nearBrownAddon;

    @Override
    public int getNumHumans() {
        return this.numHumans;
    }

    @Override
    public void setNumHumans(int numHumans) {
        this.numHumans = numHumans;
    }

    @Override
    public boolean isPurpleAlien() {
        return this.purpleAlien;
    }

    @Override
    public void setPurpleAlien(boolean purpleAlien) {
        this.purpleAlien = purpleAlien;
    }

    @Override
    public boolean isBrownAlien() {
        return this.brownAlien;
    }

    @Override
    public void setBrownAlien(boolean brownAlien) {
        this.brownAlien = brownAlien;
    }

    @Override
    public boolean isNearPurpleAddon() {
        return this.nearPurpleAddon;
    }

    @Override
    public void setNearPurpleAddon(boolean nearPurpleAddon) {
        this.nearPurpleAddon = nearPurpleAddon;
    }

    @Override
    public boolean isNearBrownAddon() {
        return this.nearBrownAddon;
    }

    @Override
    public void setNearBrownAddon(boolean nearBrownAddon) {
        this.nearBrownAddon = nearBrownAddon;
    }

    @Override
    public void rotate(Boolean direction) {}

    @Override
    public boolean controlValidity(PlayerBoard pb, int x, int y) {
        return false;
    }

    @Override
    public void insert(PlayerBoard playerBoard) {

    }

    @Override
    public void remove(PlayerBoard playerBoard) {}


    public void setNearBrown(boolean nearBrown) {
        this.nearBrownAddon = nearBrown;
    }
    public void setNearPurple(boolean nearPurple) {
        this.nearPurpleAddon = nearPurple;
    }

    public boolean getNearBrown(){
        return this.nearBrownAddon;
    }
    public boolean getNearPurple(){
        return this.nearPurpleAddon;
    }

}