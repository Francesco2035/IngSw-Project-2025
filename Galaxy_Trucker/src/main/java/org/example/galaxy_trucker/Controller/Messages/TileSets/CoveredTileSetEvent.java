package org.example.galaxy_trucker.Controller.Messages.TileSets;

import org.example.galaxy_trucker.Controller.Messages.Event;

public class CoveredTileSetEvent implements Event {

    int size;
    public CoveredTileSetEvent(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    @Override
    public String message() {
        return "";
    }
}
