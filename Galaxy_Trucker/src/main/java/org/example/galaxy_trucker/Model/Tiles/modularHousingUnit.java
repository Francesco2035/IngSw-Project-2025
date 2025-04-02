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
    public void initType() {
        // if non ci sono alienaddons adiacenti -> numHumans==2
        // if c'è solo un colore -> chiama l'interfaccia per richiedere l'input dal giocatore su human/alien di quel colore
        // if ci sono due colori -> chiama l'interfaccia per richiedere l'input dal giocatore su human/alien di uno o dell'altro colore
        //così facendo non devo passare l'input di initType e posso chiamare su tutti i tasselli initType e sarà poi il gioco a trovare dinamicamente dove è richiesto un input
    }




    @Override
    public void rotate(Boolean direction) {}

//    @Override
//    public int setAbility(int numAbility, boolean purpleAlien, boolean brownAlien){
//        if(this.numHumans>0) this.numHumans -= numAbility;
//        this.purpleAlien = this.purpleAlien && purpleAlien;
//        this.brownAlien = this.brownAlien && brownAlien;
//        return numHumans;
//    }
//
//
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

                if (vb[x][y-1] == 1 && playerBoard.getAlienAddons().get(index).isWhatColor() ){
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

                if (vb[x-1][y] == 1 && playerBoard.getAlienAddons().get(index).isWhatColor() ){
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

                if (vb[x][y+1] == 1 && playerBoard.getAlienAddons().get(index).isWhatColor() ){
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

                if (vb[x+1][y] == 1 && playerBoard.getAlienAddons().get(index).isWhatColor() ){
                    nearPurpleAddon = true;
                }
                else {
                    nearBrownAddon = true;
                }
            }
        }
        return true;
    }
//
//
//    @Override
//    public int setAbility(Goods good, boolean select) {
//        return 0;
//    }
//
//    @Override
//    public void setAbility(boolean direzione) {}
//
//    @Override
//    public ArrayList<Goods> getAbility(Goods good) {
//        return null;
//    }
//
//    @Override
//    public ArrayList<Integer> getAbility(int integer) {
//        return null;
//    }
//
//    @Override
//    public int setAbility() {
//        return 0;
//    }

    @Override
    public void insert(PlayerBoard playerBoard) {
        playerBoard.getHousingUnits().add(this);
    }

    @Override
    public void remove(PlayerBoard playerBoard) {
        playerBoard.getHousingUnits().remove(this);
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

