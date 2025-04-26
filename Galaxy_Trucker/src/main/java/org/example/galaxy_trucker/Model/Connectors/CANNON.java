package org.example.galaxy_trucker.Model.Connectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.io.Serializable;

@JsonTypeName("CANNON")
public class CANNON implements Connectors, Serializable {

    public static final CANNON INSTANCE = new CANNON();

    private CANNON() {}

    @JsonCreator
    @JsonIgnoreProperties(ignoreUnknown = true)

    public static CANNON getInstance() {
        return INSTANCE;
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
