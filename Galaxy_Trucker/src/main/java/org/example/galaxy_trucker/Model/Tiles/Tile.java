package org.example.galaxy_trucker.Model.Tiles;

import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.IntegerPair;

import java.util.*;

public class Tile {


    private int id;
    private Component component;
    private ArrayList<Connector> connectors;


    public Tile() {}

    public Tile(IntegerPair coords, Component component, Connector... connectors) {
        this.component = component;
        this.connectors = new ArrayList<>();
        this.connectors.addAll(Arrays.asList(connectors));
    }



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
    public ArrayList<Connector> getConnectors() {return connectors;}
    public void setConnectors(ArrayList<Connector> connectors) {this.connectors = connectors;}




    public boolean controlDirections(PlayerBoard pb, int x, int y) {
        return component.controlValidity(pb, x, y, this);
    }



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

