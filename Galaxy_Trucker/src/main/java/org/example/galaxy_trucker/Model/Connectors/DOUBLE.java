package org.example.galaxy_trucker.Model.Connectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.io.Serializable;

@JsonTypeName("DOUBLE")
public class DOUBLE implements Connectors, Serializable {

    public static final DOUBLE INSTANCE = new DOUBLE();

    private DOUBLE() {}

    @JsonCreator
    @JsonIgnoreProperties(ignoreUnknown = true)

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


    @JsonIgnore
    @Override
    public boolean isExposed() {
        return true;
    }
}
