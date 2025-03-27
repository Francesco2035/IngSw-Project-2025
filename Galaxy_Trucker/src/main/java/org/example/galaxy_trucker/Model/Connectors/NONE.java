package org.example.galaxy_trucker.Model.Connectors;

import com.fasterxml.jackson.annotation.JsonTypeName;
import org.example.galaxy_trucker.Model.Tiles.Tile;

@JsonTypeName("NONE")
public class NONE implements Connectors {

    public NONE() {}
    
    @Override
    public boolean checkAdjacent(Connectors Adjacent){
        return true;
    }

    @Override
    public boolean isExposed() {
        return false;
    }

}
