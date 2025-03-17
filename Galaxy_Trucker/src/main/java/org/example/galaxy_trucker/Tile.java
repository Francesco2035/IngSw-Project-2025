package org.example.galaxy_trucker;

import java.util.*;

public class Tile {


    private int id;
    private IntegerPair coords;
    private Component component;
    private String componentType;
    private ArrayList<Connector> connectors;
    private String type;


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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public void setConnectors(ArrayList<Connector> connectors) {
        this.connectors = connectors;
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
