package org.example.galaxy_trucker.Model.Connectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("CANNON")
public class CANNON implements Connectors {

    public static final CANNON INSTANCE = new CANNON();

    private CANNON() {}

    @JsonCreator
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

    @Override
    public boolean isExposed() {
        return false;
    }
}
