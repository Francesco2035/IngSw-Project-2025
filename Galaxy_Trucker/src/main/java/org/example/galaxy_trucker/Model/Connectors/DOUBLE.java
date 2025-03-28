package org.example.galaxy_trucker.Model.Connectors;

import com.fasterxml.jackson.annotation.JsonTypeName;
import org.example.galaxy_trucker.Model.Tiles.Tile;

@JsonTypeName("DOUBLE")
public class DOUBLE implements Connectors {

    public DOUBLE() {}
    
    @Override
    public boolean checkAdjacent(Connectors Adjacent){
        return Adjacent instanceof DOUBLE || Adjacent instanceof UNIVERSAL;
    }

    @Override
    public boolean isExposed() {
        return true;
    }
}
