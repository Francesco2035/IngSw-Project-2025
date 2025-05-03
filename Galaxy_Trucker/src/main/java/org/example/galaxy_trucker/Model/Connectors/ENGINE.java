package org.example.galaxy_trucker.Model.Connectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.example.galaxy_trucker.Model.Tiles.Tile;

import java.io.Serializable;

@JsonTypeName("ENGINE")
public class ENGINE implements Connectors , Serializable {

    public static final ENGINE ISTANCE = new ENGINE();

    private ENGINE() {}

    @JsonCreator
    @JsonIgnoreProperties(ignoreUnknown = true)

    public static ENGINE getInstance() {
        return ISTANCE;
    }

    @Override
    public boolean checkLegal(Connectors Adjacent) {
        return false;
    }

    @Override
    public boolean checkAdjacent(Connectors Adjacent){
        return false;
    }


    @JsonIgnore
    @Override
    public boolean isExposed() {
        return false;
    }

}
