package org.example.galaxy_trucker.Model.Tiles;

import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.example.galaxy_trucker.Exceptions.InvalidInput;
import org.example.galaxy_trucker.Model.Boards.Actions.ComponentAction;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.PlayerStates.PlayerState;

import java.util.ArrayList;

public class ModularHousingUnit extends HousingUnit {

    private boolean connected = false;

    protected int numHumans;
    protected boolean purpleAlien;
    protected boolean brownAlien;
    protected boolean nearPurpleAddon;
    protected boolean nearBrownAddon;
    private PlayerBoard playerBoard;

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
    //@SuppressWarnings("SuspiciousMethodCalls")
    public boolean controlValidity(PlayerBoard pb, int x, int y) {
        this.playerBoard = pb;
        Tile tile = playerBoard.getTile(x,y);
        nearBrownAddon = false;
        nearPurpleAddon = false;
        connected = false;

        int[][] validPlayerBoard = playerBoard.getValidPlayerBoard();
        int index = 0;


        playerBoard.getConnectedHousingUnits().remove(this);
        ArrayList<HousingUnit> nearbyunits = getNearbyHousingUnits();

        if (validPlayerBoard[x-1][y] == 1  && pb.checkConnection(pb.getTile(x,y).getConnectors().get(1), pb.getTile(x-1,y).getConnectors().get(3)) && playerBoard.getHousingUnits().contains(pb.getTile(x-1,y).getComponent())) {
            nearbyunits.add(pb.getHousingUnits().get(pb.getHousingUnits().indexOf((pb.getTile(x-1,y).getComponent()))));
        }
        if (validPlayerBoard[x+1][y] == 1  && pb.checkConnection(pb.getTile(x,y).getConnectors().get(3), pb.getTile(x+1,y).getConnectors().get(1)) && playerBoard.getHousingUnits().contains( pb.getTile(x+1,y).getComponent())) {
            nearbyunits.add(pb.getHousingUnits().get(pb.getHousingUnits().indexOf((pb.getTile(x+1,y).getComponent()))));
        }
        if (validPlayerBoard[x][y-1] == 1  && pb.checkConnection(pb.getTile(x,y).getConnectors().get(0), pb.getTile(x,y-1).getConnectors().get(2)) && playerBoard.getHousingUnits().contains( pb.getTile(x,y-1).getComponent())) {
            nearbyunits.add(pb.getHousingUnits().get(pb.getHousingUnits().indexOf((pb.getTile(x,y-1).getComponent()))));
        }
        if (validPlayerBoard[x][y+1] == 1  && pb.checkConnection(pb.getTile(x,y).getConnectors().get(2), pb.getTile(x,y+1).getConnectors().get(0)) && playerBoard.getHousingUnits().contains( pb.getTile(x,y+1).getComponent())) {
            nearbyunits.add(pb.getHousingUnits().get(pb.getHousingUnits().indexOf((pb.getTile(x,y+1).getComponent()))));
        }


        if(validPlayerBoard[x][y-1] == 1 && playerBoard.getAlienAddons().contains(playerBoard.getTile(x,y-1).getComponent())){
            index = playerBoard.getAlienAddons().indexOf(playerBoard.getTile(x,y-1).getComponent());
            if (tile.getConnectors().get(0).checkAdjacent(playerBoard.getTile(x,y-1).getConnectors().get(2))){

                if (validPlayerBoard[x][y-1] == 1 && playerBoard.getAlienAddons().get(index).type == 1 ){
                    nearPurpleAddon = true;
                }
                else {
                    nearBrownAddon = true;
                }
            }
        }

        if(validPlayerBoard[x-1][y] == 1 && playerBoard.getAlienAddons().contains(playerBoard.getTile(x-1,y).getComponent())){
            index = playerBoard.getAlienAddons().indexOf(playerBoard.getTile(x-1,y).getComponent());
            if (tile.getConnectors().get(1).checkAdjacent(playerBoard.getTile(x-1,y).getConnectors().get(3))){

                if (validPlayerBoard[x-1][y] == 1 && playerBoard.getAlienAddons().get(index).type == 1 ){
                    nearPurpleAddon = true;
                }
                else {
                    nearBrownAddon = true;
                }
            }
        }

        if(validPlayerBoard[x][y+1] == 1 && playerBoard.getAlienAddons().contains(playerBoard.getTile(x,y+1).getComponent())){
            index = playerBoard.getAlienAddons().indexOf(playerBoard.getTile(x,y+1).getComponent());
            if (tile.getConnectors().get(2).checkAdjacent(playerBoard.getTile(x,y+1).getConnectors().get(0))){

                if (validPlayerBoard[x][y+1] == 1 && playerBoard.getAlienAddons().get(index).type == 1 ){
                    nearPurpleAddon = true;
                }
                else {
                    nearBrownAddon = true;
                }
            }
        }
        //System.out.println("salve");
        // Safe: getComponent() returns Component, and list contains Component
        if(validPlayerBoard[x+1][y] == 1 && playerBoard.getAlienAddons().contains(playerBoard.getTile(x +1,y).getComponent())){
            index = playerBoard.getAlienAddons().indexOf(playerBoard.getTile(x+1,y).getComponent());
            //System.out.println("dovrei entrare qui "+index);
            if (tile.getConnectors().get(3).checkAdjacent(playerBoard.getTile(x+1,y).getConnectors().get(1))){

                if (validPlayerBoard[x+1][y] == 1 && playerBoard.getAlienAddons().get(index).type == 1 ){
                    nearPurpleAddon = true;
                }
                else {
                    nearBrownAddon = true;
                }
            }

        }

        if(numHumans > 0 || isBrownAlien() || isPurpleAlien()){
            for (HousingUnit unit : getNearbyHousingUnits()){
                if(unit.getNumHumans() > 0 || unit.isBrownAlien() || unit.isPurpleAlien()){
                    playerBoard.getConnectedHousingUnits().remove(unit);
                    playerBoard.getConnectedHousingUnits().add(unit);
                }
            }
        }

        if((!nearPurpleAddon && purpleAlien) || (brownAlien && !nearBrownAddon)){
            kill();
        }

        return true;
    }



    @Override
    public void accept(ComponentAction visitor, PlayerState state) {
        visitor.visit(this, state);
    }

    @Override
    public int kill(){
        if (numHumans == 0 && !purpleAlien && !brownAlien){
            throw new InvalidInput("This ModularHousingUnit is empty! "+getX()+" "+getY());
        }
        if (numHumans != 0){
            numHumans--;
            System.out.println("killed a human in "+getX()+" "+getY());
            if (numHumans == 0){
                playerBoard.getConnectedHousingUnits().remove(this);
            }
            if(numHumans == 0 && !getNearbyHousingUnits().isEmpty()){
                playerBoard.getConnectedHousingUnits().remove(this);
                for (HousingUnit housingUnit : getNearbyHousingUnits()) {
                    housingUnit.notifyUnit(false, this);
                }
            }
            tile.sendUpdates(null, numHumans,purpleAlien,brownAlien,0);
            return 2;
        }
        else if (purpleAlien){
            purpleAlien = false;
            System.out.println("killed a purple alien in "+getX()+" "+getY());
            playerBoard.getConnectedHousingUnits().remove(this);
            if(!getNearbyHousingUnits().isEmpty()){
                for (HousingUnit housingUnit : getNearbyHousingUnits()) {
                    housingUnit.notifyUnit(false, this);
                }
            }
            tile.sendUpdates(null, numHumans,purpleAlien,brownAlien,0);


            return 1;

        }
        else {
            brownAlien = false;
            System.out.println("killed a brown alien in "+getX()+" "+getY());
            playerBoard.getConnectedHousingUnits().remove(this);
            if(!getNearbyHousingUnits().isEmpty()){
                for (HousingUnit housingUnit : getNearbyHousingUnits()) {
                    housingUnit.notifyUnit(false, this);
                }
            }
            tile.sendUpdates(null, numHumans,purpleAlien,brownAlien,0);

            return 0;
        }



    }

    @Override
    public void addCrew(int humans, boolean purple, boolean brown){

        if ((purple && !nearPurpleAddon ) || (brown && !nearBrownAddon)){
            throw new InvalidInput("There isn't a nearby addon");
        }

        if (humans != 0 && (brownAlien || purpleAlien)){
            throw new InvalidInput("You can't add humans, there is an alien!");
        }

        if (humans > 2){
            throw new InvalidInput("Input Human amount is greater than 2");
        }
        if (humans > 0 && (purple || brown)){
            throw new InvalidInput("Is possible to add only one type of alien or humans");
        }
        if (numHumans + humans > 2){
            throw new InvalidInput("Human amount is greater than 2");
        }
        if ((purple && brownAlien) || (brown && purpleAlien) || (purple && purpleAlien) || (brown && brownAlien)){
            throw new InvalidInput("There is already an alien");
        }
        numHumans += humans;
        purpleAlien = purple;
        brownAlien = brown;
        if(!getNearbyHousingUnits().isEmpty()){
            for (HousingUnit housingUnit : getNearbyHousingUnits()) {
                housingUnit.notifyUnit(true, this);
            }
        }


        tile.sendUpdates(null,numHumans,purpleAlien,brownAlien,0);
    }


    @Override
    public Component clone(PlayerBoard clonedPlayerBoard){
        ModularHousingUnit clone = new ModularHousingUnit();
        clone.setBrownAlien(brownAlien);
        clone.setPurpleAlien(purpleAlien);
        clone.setNearPurpleAddon(nearPurpleAddon);
        clone.setNearBrownAddon(nearBrownAddon);
        clone.setNumHumans(numHumans);
        clone.setConnected(connected);
        clone.setPlayerBoard(clonedPlayerBoard);
        if (connected && this.playerBoard.getConnectedHousingUnits().contains(this)){
            clonedPlayerBoard.getConnectedHousingUnits().add(clone);
        }
        return clone;
    }

    public void setConnected(boolean connected){
        this.connected = connected;
    }

    public void setPlayerBoard(PlayerBoard playerBoard){
        this.playerBoard = playerBoard;
    }


    @Override
    public void notifyUnit(boolean type, HousingUnit unit){
        if (type){
            if (numHumans > 0 || purpleAlien || brownAlien){
                playerBoard.getConnectedHousingUnits().remove(this);
                playerBoard.getConnectedHousingUnits().add(this);
            }
        }
        else {
            getNearbyHousingUnits().remove(unit);
            if(getNearbyHousingUnits().isEmpty()){
                playerBoard.getConnectedHousingUnits().remove(this);
            }

        }
    }

}




