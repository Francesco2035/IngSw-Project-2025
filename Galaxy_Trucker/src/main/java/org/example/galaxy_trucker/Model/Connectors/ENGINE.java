package org.example.galaxy_trucker.Model.Connectors;

import com.fasterxml.jackson.annotation.JsonTypeName;
import org.example.galaxy_trucker.Model.Tiles.Tile;

@JsonTypeName("ENGINE")
public class ENGINE implements Connectors {

    public ENGINE() {}    
    
    @Override
    public boolean checkAdjacent(Connectors Adjacent){
        return Adjacent instanceof NONE;
    }

    @Override
    public boolean isExposed() {
        return false;
    }

}
