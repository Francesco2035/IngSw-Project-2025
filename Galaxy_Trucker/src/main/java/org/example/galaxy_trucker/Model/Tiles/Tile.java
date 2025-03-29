package org.example.galaxy_trucker.Model.Tiles;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.Connectors.*;
import org.example.galaxy_trucker.Model.IntegerPair;

import java.util.*;

public class Tile {


    private int id;
    private Component component;
    @JsonProperty("connectors")
    private ArrayList<Connectors> connectors;


    public Tile() {}

    public Tile(IntegerPair coords, Component component, Connectors... connectors) {
        this.component = component;
        this.connectors = new ArrayList<>();
        this.connectors.addAll(Arrays.asList(connectors));
    }


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



// questo fa cagare
//    public boolean checkAdjacent(Tile Adjacent, int direction) {
//        return this.getConnectors().get(direction % connectors.size()) instanceof UNIVERSAL ||
//                Adjacent.getConnectors().get((direction + 2) % connectors.size()) instanceof UNIVERSAL ||
//                (this.getConnectors().get(direction % connectors.size()).equals(Adjacent.getConnectors().get((direction + 2) % connectors.size())) &&
//                        this.getConnectors().get(direction % connectors.size()) instanceof ENGINE &&
//                        Adjacent.getConnectors().get((direction + 2) % connectors.size()) instanceof ENGINE &&
//                        this.getConnectors().get(direction % connectors.size()) instanceof CANNON &&
//                        Adjacent.getConnectors().get((direction + 2) % connectors.size()) instanceof CANNON &&
//                        this.getConnectors().get(direction % connectors.size()) instanceof NONE &&
//                        Adjacent.getConnectors().get((direction + 2) % connectors.size()) instanceof NONE);
//    }

    //metodi rotate per le tiles
    public void RotateSx(){
        Collections.rotate(this.connectors, -1);
        this.component.setAbility(false);
    }

    public void RotateDx(){
        Collections.rotate(this.connectors, 1);
        this.component.setAbility(true);
    }


}

