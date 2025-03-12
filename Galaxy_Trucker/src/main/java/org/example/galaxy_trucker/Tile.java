package org.example.galaxy_trucker;

import java.util.*;

import javafx.util.Pair;
import org.example.galaxy_trucker.Component;

public class Tile {


    private IntegerPair coords;


    private Component component;
    private List<Connector> connectors = new ArrayList<>();

    public Tile( IntegerPair coords, Component component, Connector... connectors) {
        this.coords = coords;
        this.component = component;
        this.connectors.addAll(Arrays.asList(connectors));
    }

    public void RotateSx(){
        Collections.rotate(this.connectors, -1);
    }

    public void RotateDx(){
        Collections.rotate(this.connectors, 1);
    }


    public IntegerPair getCoords() {
        return coords;
    }

    public Component getComponent() {
        return component;
    }

    public List<Connector> getConnectors() {
        return connectors;
    }

}
