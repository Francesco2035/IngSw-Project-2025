package org.example.galaxy_trucker.Model.Connectors;

import com.fasterxml.jackson.annotation.JsonTypeName;
import org.example.galaxy_trucker.Model.Tiles.Tile;

@JsonTypeName("CANNON")
public class CANNON implements Connectors {

    public CANNON() {}
    
    @Override
    public boolean checkAdjacent(Connectors Adjacent){
        return Adjacent instanceof NONE;
    }
}
