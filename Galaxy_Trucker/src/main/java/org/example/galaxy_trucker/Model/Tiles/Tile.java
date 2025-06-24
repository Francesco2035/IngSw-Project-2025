package org.example.galaxy_trucker.Model.Tiles;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.galaxy_trucker.Messages.PlayerBoardEvents.RemoveTileEvent;
import org.example.galaxy_trucker.Messages.PlayerBoardEvents.TileEvent;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.Connectors.*;
import org.example.galaxy_trucker.Model.Goods.Goods;


import java.io.Serializable;
import java.util.*;

public class Tile implements Serializable {


    private int id;
    private Component component;
    private PlayerBoard playerBoard;
    int x;
    int y;
    int rotation = 0;
    private boolean chosen = false;
    @JsonProperty("connectors")
    private ArrayList<Connectors> connectors;

    private boolean available;


    public Tile() {}

    public Tile(Component component, Connectors... connectors) {
        this.component = component;
        this.connectors = new ArrayList<>();
        this.connectors.addAll(Arrays.asList(connectors));
    }



//    public boolean controlDirections(PlayerBoard pb, int x, int y) {
//        return component.controlValidity(pb, x, y);
//    }


    //spostare la rotazione delle direzioni protette in una chiamata della classe di shield generator

    //metodi rotate per le tiles


    public void RotateSx(){
        Collections.rotate(this.connectors, -1);
        this.getComponent().rotate(false);
    }

    public void RotateDx(){
        Collections.rotate(this.connectors, 1);
        this.getComponent().rotate(true);
    }



    public Tile clone(PlayerBoard clonedPlayerBoard){
        Tile clonedTile = new Tile();
        clonedTile.setId(this.id);
        clonedTile.setRotation(this.rotation);
        clonedTile.setPlayerBoard(clonedPlayerBoard);
        Component clonedComponent = this.component.clone(clonedPlayerBoard);
        clonedComponent.setTile(clonedTile);
        clonedTile.setComponent(clonedComponent);
        clonedTile.setConnectors(new ArrayList<>(this.connectors));
        clonedTile.chosen = this.chosen;
        return clonedTile;

    }




//    public boolean isAvailable() {return available;}
//    public void setAvailable(boolean available) {this.available = available;}


    //  json required
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public Component getComponent() {
        return component;
    }
    public void setComponent(Component component) {
        this.component = component;
    }
    public ArrayList<Connectors> getConnectors() {return connectors;}
    public void setConnectors(ArrayList<Connectors> connectors) {this.connectors = connectors;}



    public boolean controlDirections(PlayerBoard pb, int x, int y) {
        return component.controlValidity(pb, x, y);
    }


    public void sendUpdates(ArrayList<Goods> cargo, int humans,boolean purpleAlien, boolean brownAlien, int batteries)  {
        playerBoard.sendUpdates(new TileEvent(id,x,y,cargo,humans,purpleAlien,brownAlien,batteries,rotation, connectors));
        if (cargo!=null) {
            for (Goods g : cargo) {
                System.out.println(g.getClass());
            }
        }
    }

    public void sendUpdates(RemoveTileEvent event) {
        ArrayList<Connectors> noneConnectors = new ArrayList<>();
        noneConnectors.add(NONE.INSTANCE);
        noneConnectors.add(NONE.INSTANCE);
        noneConnectors.add(NONE.INSTANCE);
        noneConnectors.add(NONE.INSTANCE);
        playerBoard.sendUpdates(new TileEvent(157,x,y,null, 0,false, false, 0, 0,  noneConnectors));
    }


    public void setPlayerBoard(PlayerBoard playerBoard) {
        this.playerBoard = playerBoard;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }


    public void setChosen(){
        chosen = true;
    }
    public boolean getChosen(){
        return chosen;
    }

    public void setRotation(int rotations) {
        rotation += rotations;
        rotation = rotation % 360;
    }
}




