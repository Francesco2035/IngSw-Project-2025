package org.example.galaxy_trucker.Model.Connectors;

import com.fasterxml.jackson.annotation.JsonTypeName;
import org.example.galaxy_trucker.Model.Tiles.Tile;

@JsonTypeName("UNIVERSAL")
public class UNIVERSAL implements Connectors {

    public UNIVERSAL() {}
    
    @Override
    public boolean checkAdjacent(Connectors Adjacent){
        return Adjacent instanceof UNIVERSAL || Adjacent instanceof SINGLE || Adjacent instanceof DOUBLE;
    }

    @Override
    public boolean isExposed() {
        return true;
    }

}
