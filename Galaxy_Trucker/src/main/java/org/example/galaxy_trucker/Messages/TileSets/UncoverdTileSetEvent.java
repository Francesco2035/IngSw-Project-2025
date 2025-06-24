package org.example.galaxy_trucker.Messages.TileSets;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.galaxy_trucker.Messages.Event;
import org.example.galaxy_trucker.Messages.EventVisitor;
import org.example.galaxy_trucker.Model.Connectors.Connectors;

import java.util.ArrayList;

public class UncoverdTileSetEvent implements Event {

    private Integer id;
    private ArrayList<Connectors> connectors;

    @JsonCreator
    public UncoverdTileSetEvent(
            @JsonProperty("id") Integer id,
            @JsonProperty("connectors") ArrayList<Connectors> connectors
    ) {
        this.id = id;
        this.connectors = connectors;
    }

    public UncoverdTileSetEvent() {
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

    @Override
    public void accept(EventVisitor visitor) {
        visitor.visit(this);
    }

}
