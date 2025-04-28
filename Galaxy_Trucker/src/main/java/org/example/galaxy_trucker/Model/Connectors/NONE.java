package org.example.galaxy_trucker.Model.Connectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.io.Serializable;

@JsonTypeName("NONE")
@JsonIgnoreProperties(ignoreUnknown = true)
public class NONE implements Connectors , Serializable {

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


    @JsonIgnore
    @Override
    public boolean isExposed() {
        return false;
    }
}
