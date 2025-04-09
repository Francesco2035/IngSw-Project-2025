package org.example.galaxy_trucker.Model.Goods;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.io.Serial;
import java.io.Serializable;


@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXTERNAL_PROPERTY,
        property = "type"
)


@JsonSubTypes({
        @JsonSubTypes.Type(value = BLUE.class, name = "BLUE"),
        @JsonSubTypes.Type(value = GREEN.class, name = "GREEN"),
        @JsonSubTypes.Type(value = YELLOW.class, name = "YELLOW"),
        @JsonSubTypes.Type(value = RED.class, name = "RED")
})


public interface Goods extends Serializable {
    public int getValue();
}
