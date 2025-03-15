package org.example.galaxy_trucker;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;


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
