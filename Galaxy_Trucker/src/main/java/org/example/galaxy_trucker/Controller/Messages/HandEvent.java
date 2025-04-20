package org.example.galaxy_trucker.Controller.Messages;

import org.example.galaxy_trucker.Model.Connectors.Connectors;

import java.util.ArrayList;

public class HandEvent implements Event{

    private int id;
    private ArrayList<Connectors> connectors;


    public HandEvent(int id, ArrayList<Connectors> connectors) {
        this.id = id;
        this.connectors = connectors;
    }

    @Override
    public String message() {
        return "";
    }

    public int getId() {
        return id;
    }
    public ArrayList<Connectors> getConnectors() {
        return connectors;
    }



}
