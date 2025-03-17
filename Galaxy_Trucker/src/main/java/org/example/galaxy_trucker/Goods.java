package org.example.galaxy_trucker;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Goods {
    @JsonProperty("BLUE")
    BLUE,
    @JsonProperty("GREEN")
    GREEN,
    @JsonProperty("YELLOW")
    YELLOW,
    @JsonProperty("RED")
    RED
}
