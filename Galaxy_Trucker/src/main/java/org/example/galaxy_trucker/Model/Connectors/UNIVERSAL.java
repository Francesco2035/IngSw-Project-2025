package org.example.galaxy_trucker.Model.Connectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.io.Serializable;

@JsonTypeName("UNIVERSAL")
public class UNIVERSAL implements Connectors, Serializable {

    public static final UNIVERSAL INSTANCE = new UNIVERSAL();

    private UNIVERSAL() {}

    @JsonCreator
    public static UNIVERSAL getInstance() {
        return INSTANCE;
    }

    @Override
    public boolean checkLegal(Connectors Adjacent) {
        return Adjacent == UNIVERSAL.INSTANCE || Adjacent == SINGLE.INSTANCE || Adjacent == DOUBLE.INSTANCE ;
    }

    @Override
    public boolean checkAdjacent(Connectors Adjacent) {
        return Adjacent == UNIVERSAL.INSTANCE || Adjacent == SINGLE.INSTANCE || Adjacent == DOUBLE.INSTANCE;
    }

    @Override
    public boolean isExposed() {
        return true;
    }
}
