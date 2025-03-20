package org.example.galaxy_trucker.Model.Tiles;


import com.fasterxml.jackson.annotation.JsonProperty;


public enum Connector {
    @JsonProperty("SINGLE")
    SINGLE,
    @JsonProperty("DOUBLE")
    DOUBLE,
    @JsonProperty("UNIVERSAL")
    UNIVERSAL,
    @JsonProperty("NONE")
    NONE,
    @JsonProperty("MOTOR")
    MOTOR,
    @JsonProperty("CANNON")
    CANNON;

    private int direction;

    private Connector() {}
}

