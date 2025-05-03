package org.example.galaxy_trucker.Model.Connectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.io.Serializable;

@JsonTypeName("UNIVERSAL")
public class UNIVERSAL implements Connectors, Serializable {

    public static final UNIVERSAL INSTANCE = new UNIVERSAL();

    private UNIVERSAL() {}

    @JsonCreator
    @JsonIgnoreProperties(ignoreUnknown = true)
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

    @JsonIgnore
    @Override
    public boolean isExposed() {
        return true;
    }
}
