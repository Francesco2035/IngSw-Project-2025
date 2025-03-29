package org.example.galaxy_trucker.Model.Connectors;

import com.fasterxml.jackson.annotation.JsonTypeName;
import org.example.galaxy_trucker.Model.Tiles.Tile;

@JsonTypeName("SINGLE")
public class SINGLE implements Connectors {

    public SINGLE() {}

    @Override
    public boolean checkAdjacent(Connectors Adjacent){
        return Adjacent instanceof SINGLE || Adjacent instanceof UNIVERSAL;
    }

    @Override
    public boolean isExposed() {
        return true;
    }

}
