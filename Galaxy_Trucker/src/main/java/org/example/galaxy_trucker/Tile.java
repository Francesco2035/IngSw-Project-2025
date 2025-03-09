package org.example.galaxy_trucker;

import java.util.*;

import javafx.util.Pair;
import org.example.galaxy_trucker.Component;

public class Tile {


    private Pair<Integer, Integer> coords;


    private Component component;
    private List<Connector> connectors = new ArrayList<>();

    public Tile(Pair<Integer, Integer > coords, Component component, Connector... connectors) {
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


    public Pair<Integer, Integer> getCoords() {
        return coords;
    }

    public Component getComponent() {
        return component;
    }

}
