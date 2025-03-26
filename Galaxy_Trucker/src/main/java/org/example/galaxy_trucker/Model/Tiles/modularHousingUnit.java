package org.example.galaxy_trucker.Model.Tiles;

import org.example.galaxy_trucker.Model.Goods.Goods;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.IntegerPair;

import java.util.ArrayList;
import java.util.HashMap;

public class modularHousingUnit extends Component {

    private int numHumans;
    private boolean purpleAlien;
    private boolean brownAlien;

    private boolean nearPurpleAddon;
    private boolean nearBrownAddon;


    public modularHousingUnit() {
        this.numHumans = 0;
        this.purpleAlien = false;
        this.brownAlien = false;
    }


    public void setNumHumans(int numHumans) {
        this.numHumans = numHumans;
    }
    public boolean isPurpleAlien() {
        return purpleAlien;
    }
    public void setPurpleAlien(boolean purpleAlien) {this.purpleAlien = purpleAlien;}
    public boolean isBrownAlien() {
        return brownAlien;
    }
    public void setBrownAlien(boolean brownAlien) {
        this.brownAlien = brownAlien;
    }


    @Override
    public int getAbility() {
        return numHumans;
    }

    @Override
    public boolean getNearbyAddons(boolean type) { //true -> purple, false -> brown
        if (type){
            return this.nearPurpleAddon;
        }
        return this.nearBrownAddon;
    }


    @Override
    public void initType() {
        // if non ci sono alienaddons adiacenti -> numHumans==2
        // if c'è solo un colore -> chiama l'interfaccia per richiedere l'input dal giocatore su human/alien di quel colore
        // if ci sono due colori -> chiama l'interfaccia per richiedere l'input dal giocatore su human/alien di uno o dell'altro colore
        //così facendo non devo passare l'input di initType e posso chiamare su tutti i tasselli initType e sarà poi il gioco a trovare dinamicamente dove è richiesto un input
    }

    @Override
    public void initType(int numHumans, boolean purpleAlien, boolean brownAlien) {
        this.numHumans = numHumans;
        this.purpleAlien = purpleAlien;
        this.brownAlien = brownAlien;
    }

    @Override
    public int setAbility(int numAbility, boolean purpleAlien, boolean brownAlien){
        if(this.numHumans>0) this.numHumans -= numAbility;
        this.purpleAlien = this.purpleAlien && purpleAlien;
        this.brownAlien = this.brownAlien && brownAlien;
        return numHumans;
    }


    @Override
    public boolean controlValidity(PlayerBoard pb, int x, int y, Tile tile) {

        this.nearBrownAddon = false;
        this.nearPurpleAddon = false;
        int[][] vb = pb.getValidPlayerBoard();
        HashMap<Connector, ArrayList<Connector>> vc = pb.getValidConnection();

    if(         pb.getClassifiedTiles().containsKey(alienAddons.class) &&
                pb.getClassifiedTiles().get(alienAddons.class).contains(new IntegerPair(x,y-1))){
        if (vc.get(tile.getConnectors().get(0)).contains(pb.getTile(x,y-1).getConnectors().get(2))){
            if (vb[x][y-1] == 1 && pb.getTile(x,y-1).getComponent().getAbility() == 1){
                this.nearPurpleAddon = true;
                }
            else {
                this.nearBrownAddon = true;
                }
            }
        }

        if(         pb.getClassifiedTiles().containsKey(alienAddons.class) &&
                pb.getClassifiedTiles().get(alienAddons.class).contains(new IntegerPair(x-1,y))){
            if (vc.get(tile.getConnectors().get(1)).contains(pb.getTile(x-1,y).getConnectors().get(3))){
                if (vb[x-1][y] == 1 && pb.getTile(x-1,y).getComponent().getAbility() == 1){
                    this.nearPurpleAddon = true;
                }
                else {
                    this.nearBrownAddon = true;
                }
            }
        }

        if(         pb.getClassifiedTiles().containsKey(alienAddons.class) &&
                pb.getClassifiedTiles().get(alienAddons.class).contains(new IntegerPair(x,y+1))){
            if (vc.get(tile.getConnectors().get(2)).contains(pb.getTile(x,y+1).getConnectors().get(0))){
                if (vb[x][y+1] == 1 && pb.getTile(x,y+1).getComponent().getAbility() == 1){
                    this.nearPurpleAddon = true;
                }
                else {
                    this.nearBrownAddon = true;
                }
            }
        }


        if(         pb.getClassifiedTiles().containsKey(alienAddons.class) &&
                pb.getClassifiedTiles().get(alienAddons.class).contains(new IntegerPair(x+1,y))){
            if (vc.get(tile.getConnectors().get(3)).contains(pb.getTile(x+1,y).getConnectors().get(1))){
                if (vb[x + 1][y] == 1 && pb.getTile(x+1,y).getComponent().getAbility() == 1){
                    this.nearPurpleAddon = true;
                }
                else {
                    this.nearBrownAddon = true;
                }
            }
        }
        return true;
    }


    @Override
    public int setAbility(Goods good, boolean select) {
        return 0;
    }

    @Override
    public void setAbility(boolean direzione) {}

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

}

