package org.example.galaxy_trucker;

import java.util.*;

import javafx.util.Pair;
import org.example.galaxy_trucker.Component;

public class Tile {


    private int id;
    private IntegerPair coords;
    private Component component;
    private String componentType;
    private ArrayList<Connector> connectors;
    private String ability;


    public Tile() {}

    public Tile(IntegerPair coords, Component component, Connector... connectors) {
        this.coords = coords;
        this.component = component;
        this.connectors = new ArrayList<>();
        this.connectors.addAll(Arrays.asList(connectors));
    }





    public IntegerPair getCoords() {
        return coords;
    }

    public void setCoords(IntegerPair coords) {
        this.coords = coords;
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

    public String getComponentType() {
        return componentType;
    }

    public void setComponentType(String componentType) {
        this.componentType = componentType;
    }

    public String getAbility() {
        return ability;
    }

    public void setAbility(String ability) {
        this.ability = ability;
    }


    public void setConnectors(ArrayList<Connector> connectors) {
        this.connectors = connectors;
    }






    //metodi rotate per le tiles
    public void RotateSx(){
        Collections.rotate(this.connectors, -1);
    }

    public void RotateDx(){
        Collections.rotate(this.connectors, 1);
    }

}
