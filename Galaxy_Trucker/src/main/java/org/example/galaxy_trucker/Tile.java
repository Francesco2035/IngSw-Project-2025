package org.example.galaxy_trucker;

import java.util.*;
import org.example.galaxy_trucker.Component;

public class Tile {

    private Component component;
    private List<Connector> connectors = new ArrayList<>();

    public Tile(Component component, Connector... connectors) {
        this.component = component;
        this.connectors.addAll(Arrays.asList(connectors));
    }

    public void RotateSx(){
        Collections.rotate(this.connectors, -1);
    }

    public void RotateDx(){
        Collections.rotate(this.connectors, 1);
    }
}
