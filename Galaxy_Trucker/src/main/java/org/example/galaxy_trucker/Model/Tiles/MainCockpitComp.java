package org.example.galaxy_trucker.Model.Tiles;

import org.example.galaxy_trucker.Exceptions.InvalidInput;
import org.example.galaxy_trucker.Model.Boards.Actions.ComponentAction;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.PlayerStates.PlayerState;

import java.util.ArrayList;

public class MainCockpitComp extends HousingUnit {

    private int numHumans;
    private boolean connected = false;
    private PlayerBoard playerBoard;


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
    public void notifyUnit(boolean type, HousingUnit unit) {}


    @Override
    public boolean controlValidity(PlayerBoard pb, int x, int y) {
        this.playerBoard = pb;
        this.connected = false;
        //playerBoard.getConnectedHousingUnits().remove(this);
        ArrayList<HousingUnit> nearbyunits = getNearbyHousingUnits();

        int[][] validPlayerBoard = pb.getValidPlayerBoard();
        if (validPlayerBoard[x-1][y] == 1  && pb.checkConnection(pb.getTile(x,y).getConnectors().get(1), pb.getTile(x-1,y).getConnectors().get(3)) && playerBoard.getHousingUnits().contains(pb.getTile(x-1,y).getComponent())) {
            HousingUnit u = pb.getHousingUnits().get(pb.getHousingUnits().indexOf((pb.getTile(x-1,y).getComponent())));
            nearbyunits.remove(u);
            nearbyunits.add(u);
        }
        if (validPlayerBoard[x+1][y] == 1  && pb.checkConnection(pb.getTile(x,y).getConnectors().get(3), pb.getTile(x+1,y).getConnectors().get(1))&& playerBoard.getHousingUnits().contains(pb.getTile(x+1,y).getComponent())) {
            HousingUnit u = pb.getHousingUnits().get(pb.getHousingUnits().indexOf((pb.getTile(x+1,y).getComponent())));
            nearbyunits.remove(u);
            nearbyunits.add(u);
        }
        if (validPlayerBoard[x][y-1] == 1  && pb.checkConnection(pb.getTile(x,y).getConnectors().get(0), pb.getTile(x,y-1).getConnectors().get(2))&& playerBoard.getHousingUnits().contains(pb.getTile(x,y-1).getComponent())) {
            HousingUnit u = pb.getHousingUnits().get(pb.getHousingUnits().indexOf((pb.getTile(x,y-1).getComponent())));
            nearbyunits.remove(u);
            nearbyunits.add(u);
        }
        if (y < 9 && validPlayerBoard[x][y+1] == 1  && pb.checkConnection(pb.getTile(x,y).getConnectors().get(2), pb.getTile(x,y+1).getConnectors().get(0))&& playerBoard.getHousingUnits().contains(pb.getTile(x,y+1).getComponent())) {
            HousingUnit u = pb.getHousingUnits().get(pb.getHousingUnits().indexOf((pb.getTile(x,y+1).getComponent())));
            nearbyunits.remove(u);
            nearbyunits.add(u);
        }

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
            if (numHumans == 0){
                playerBoard.getConnectedHousingUnits().remove(this);
            }
            if(numHumans == 0 &&!getNearbyHousingUnits().isEmpty()){
                for (HousingUnit unit : getNearbyHousingUnits()){
                    unit.notifyUnit(false, this);
                }
            }
            tile.sendUpdates(null, numHumans, false, false, 0);

            return 2;


    }



    @Override
    public void addCrew(int humans, boolean purple, boolean brown){
        if (purple || brown){
            throw new InvalidInput("This is the mainCockpit you can't add aliens");
        }

        if (numHumans + humans > 2){
            throw new InvalidInput("Human amount is greater than 2");
        }

        numHumans += humans;
        if(!getNearbyHousingUnits().isEmpty()){
            for (HousingUnit unit : getNearbyHousingUnits()){
                unit.notifyUnit(true, this);
            }
        }

        tile.sendUpdates(null, numHumans, false, false, 0);

    }

    public void setConnected(boolean connected){
        this.connected = connected;
    }


    @Override
    public Component clone(PlayerBoard clonedPlayerBoard){
        MainCockpitComp clone = new MainCockpitComp();
        clone.setConnected(connected);
        clone.setNumHumans(numHumans);
        clone.setPlayerBoard(clonedPlayerBoard);
        clone.setX(6);
        clone.setY(6);
        if (connected && this.playerBoard.getConnectedHousingUnits().contains(this)){
            clonedPlayerBoard.getConnectedHousingUnits().add(clone);
        }

        return clone;
    }

    public void setPlayerBoard(PlayerBoard playerBoard){
        this.playerBoard = playerBoard;
    }





}

