package org.example.galaxy_trucker.Model.Connectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.io.Serializable;

@JsonTypeName("SINGLE")
public class SINGLE implements Connectors , Serializable {

    public static final SINGLE INSTANCE = new SINGLE();

    private SINGLE() {}

    @JsonCreator
    @JsonIgnoreProperties(ignoreUnknown = true)

    public static SINGLE getInstance() {
        return INSTANCE;
    }

    @Override
    public boolean checkLegal(Connectors Adjacent) {
        return Adjacent == SINGLE.INSTANCE || Adjacent == UNIVERSAL.INSTANCE;
    }

    @Override
    public boolean checkAdjacent(Connectors Adjacent) {
        return Adjacent == SINGLE.INSTANCE || Adjacent == UNIVERSAL.INSTANCE;
    }


    @JsonIgnore
    @Override
    public boolean isExposed() {
        return true;
    }
}
