package org.example.galaxy_trucker.Model.Tiles;

import org.example.galaxy_trucker.Exceptions.InvalidInput;
import org.example.galaxy_trucker.Model.Boards.Actions.ComponentActionVisitor;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.PlayerStates;

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
    public boolean controlValidity(PlayerBoard playerBoard, int x, int y) {
        Tile tile = playerBoard.getTile(x,y);
        nearBrownAddon = false;
        nearPurpleAddon = false;
        int[][] vb = playerBoard.getValidPlayerBoard();
        int index = 0;

        if(vb[x][y-1] == 1 && playerBoard.getAlienAddons().contains(playerBoard.getTile(x,y-1).getComponent())){
            index = playerBoard.getAlienAddons().indexOf(playerBoard.getTile(x,y-1).getComponent());
            if (tile.getConnectors().get(0).checkAdjacent(playerBoard.getTile(x,y-1).getConnectors().get(2))){

                if (vb[x][y-1] == 1 && playerBoard.getAlienAddons().get(index).type == 1 ){
                    nearPurpleAddon = true;
                }
                else {
                    nearBrownAddon = true;
                }
            }
        }

        if(vb[x-1][y] == 1 && playerBoard.getAlienAddons().contains(playerBoard.getTile(x-1,y).getComponent())){
            index = playerBoard.getAlienAddons().indexOf(playerBoard.getTile(x-1,y).getComponent());
            if (tile.getConnectors().get(1).checkAdjacent(playerBoard.getTile(x-1,y).getConnectors().get(3))){

                if (vb[x-1][y] == 1 && playerBoard.getAlienAddons().get(index).type == 1 ){
                    nearPurpleAddon = true;
                }
                else {
                    nearBrownAddon = true;
                }
            }
        }

        if(vb[x][y+1] == 1 && playerBoard.getAlienAddons().contains(playerBoard.getTile(x,y+1).getComponent())){
            index = playerBoard.getAlienAddons().indexOf(playerBoard.getTile(x,y+1).getComponent());
            if (tile.getConnectors().get(2).checkAdjacent(playerBoard.getTile(x,y+1).getConnectors().get(0))){

                if (vb[x][y+1] == 1 && playerBoard.getAlienAddons().get(index).type == 1 ){
                    nearPurpleAddon = true;
                }
                else {
                    nearBrownAddon = true;
                }
            }
        }
        //System.out.println("salve");
        if(vb[x+1][y] == 1 && playerBoard.getAlienAddons().contains(playerBoard.getTile(x +1,y).getComponent())){
            index = playerBoard.getAlienAddons().indexOf(playerBoard.getTile(x+1,y).getComponent());
            //System.out.println("dovrei entrare qui "+index);
            if (tile.getConnectors().get(3).checkAdjacent(playerBoard.getTile(x+1,y).getConnectors().get(1))){

                if (vb[x+1][y] == 1 && playerBoard.getAlienAddons().get(index).type == 1 ){
                    nearPurpleAddon = true;
                }
                else {
                    nearBrownAddon = true;
                }
            }
        }
        return true;
    }



    @Override
    public void accept(ComponentActionVisitor visitor, PlayerStates State) {
        if (!(State.equals(PlayerStates.Killing) || State.equals(PlayerStates.PopulateHousingUnits))){
            throw new IllegalStateException("Player state is not Killing state or PopulateHousingUnits");
        }
        visitor.visit(this, State);
    }

    @Override
    public int kill(){
        if (numHumans == 0 && !purpleAlien && !brownAlien){
            throw new InvalidInput("This ModularHousingUnit is empty!");
        }
        if (numHumans != 0){
            numHumans--;
            return 2;
        }
        else if (purpleAlien){
            purpleAlien = false;
            return 1;
        }
        else {
            brownAlien = false;
            return 0;
        }


    }

    @Override
    public void addCrew(int humans, boolean purple, boolean brown){
        if (purple && brown){
            throw new InvalidInput("Is possible to add only one type of alien");
        }
        if ((purple && !nearPurpleAddon ) || (brown && !nearBrownAddon)){
            throw new InvalidInput("There isn't a nearby addon");
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
    }

}

