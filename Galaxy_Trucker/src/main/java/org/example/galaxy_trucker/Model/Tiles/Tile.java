package org.example.galaxy_trucker.Model.Tiles;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.Connectors.*;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Tiles.ComponentSetters.ComponentSetter;
import org.example.galaxy_trucker.Model.Tiles.ComponentSetters.ShieldSetter;

import java.util.*;

public class Tile {


    private int id;
    private Component component;
    @JsonProperty("connectors")
    private ArrayList<Connectors> connectors;


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
        this.getComponent().rotate(false);
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


    }




