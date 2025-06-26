package org.example.galaxy_trucker.ClientServer.Messages.TileSets;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.galaxy_trucker.ClientServer.Messages.Event;
import org.example.galaxy_trucker.ClientServer.Messages.EventVisitor;

public class CoveredTileSetEvent implements Event {

    private int size;

    @JsonCreator
    public CoveredTileSetEvent(@JsonProperty("size") int size) {
        this.size = size;
    }

    public CoveredTileSetEvent() {
    }

    public int getSize() {
        return size;
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
