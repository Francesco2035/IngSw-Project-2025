package org.example.galaxy_trucker.Model.Connectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.io.Serializable;

@JsonTypeName("DOUBLE")
public class DOUBLE implements Connectors, Serializable {

    public static final DOUBLE INSTANCE = new DOUBLE();

    private DOUBLE() {}

    @JsonCreator
    public static DOUBLE getInstance() {
        return INSTANCE;
    }

    @Override
    public boolean checkLegal(Connectors Adjacent) {
        return Adjacent == DOUBLE.INSTANCE || Adjacent == UNIVERSAL.INSTANCE;
    }

    @Override
    public boolean checkAdjacent(Connectors Adjacent) {
        return Adjacent == DOUBLE.INSTANCE || Adjacent == UNIVERSAL.INSTANCE;
    }

    @Override
    public boolean isExposed() {
        return true;
    }
}
