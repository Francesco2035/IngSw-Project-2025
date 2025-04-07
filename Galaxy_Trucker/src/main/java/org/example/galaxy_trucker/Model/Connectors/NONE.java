package org.example.galaxy_trucker.Model.Connectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("NONE")
public class NONE implements Connectors {

    public static final NONE INSTANCE = new NONE();

    private NONE() {}

    @JsonCreator
    public static NONE getInstance() {
        return INSTANCE;
    }

    @Override
    public boolean checkLegal(Connectors Adjacent) {
        return Adjacent == NONE.INSTANCE;
    }

    @Override
    public boolean checkAdjacent(Connectors Adjacent) {
        return false;
    }

    @Override
    public boolean isExposed() {
        return false;
    }
}
