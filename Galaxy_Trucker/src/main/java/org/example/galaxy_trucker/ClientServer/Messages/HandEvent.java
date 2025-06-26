package org.example.galaxy_trucker.ClientServer.Messages;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.galaxy_trucker.Model.Connectors.Connectors;

import java.util.ArrayList;

public class HandEvent implements Event {

    private int id;
    private ArrayList<Connectors> connectors;

    @JsonCreator
    public HandEvent(
            @JsonProperty("id") int id,
            @JsonProperty("connectors") ArrayList<Connectors> connectors
    ) {
        this.id = id;
        this.connectors = connectors;
    }

    public HandEvent() {}

    @Override
    public void accept(EventVisitor visitor) {
        visitor.visit(this);
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
