package org.example.galaxy_trucker.Model.Connectors;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.example.galaxy_trucker.Model.Tiles.Tile;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)


@JsonSubTypes({
        @JsonSubTypes.Type(value = SINGLE.class, name = "SINGLE"),
        @JsonSubTypes.Type(value = DOUBLE.class, name = "DOUBLE"),
        @JsonSubTypes.Type(value = UNIVERSAL.class, name = "UNIVERSAL"),
        @JsonSubTypes.Type(value = NONE.class, name = "NONE"),
        @JsonSubTypes.Type(value = ENGINE.class, name = "ENGINE"),
        @JsonSubTypes.Type(value = CANNON.class, name = "CANNON"),

})



public interface Connectors {

    public boolean checkAdjacent(Connectors Adjacent);

}
