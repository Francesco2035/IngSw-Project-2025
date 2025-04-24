package org.example.galaxy_trucker.Controller.Messages.TileSets;

import org.example.galaxy_trucker.Controller.Messages.Event;
import org.example.galaxy_trucker.Model.Connectors.Connectors;

import java.util.ArrayList;
import java.util.HashMap;

public class UncoverdTileSetEvent implements Event {

    private Integer id;
    private ArrayList<Connectors> connectors;
    public UncoverdTileSetEvent(Integer id, ArrayList<Connectors> connectors) {
        this.id = id;
        this.connectors = connectors;
    }

    public Integer getId() {
        return id;
    }
    public ArrayList<Connectors> getConnectors() {
        return connectors;
    }

    @Override
    public String message() {
        return "";
    }
}
