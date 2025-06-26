package org.example.galaxy_trucker.Model.Connectors;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.example.galaxy_trucker.Model.Tiles.Tile;

import java.io.Serializable;

/**
 * Represents a generic connector interface for the game's tile system.
 *
 * This interface defines the core behaviors and properties of connectors
 * in the system, allowing for various implementations representing different
 * types of connectors. Connectors are fundamental components that determine
 * the legality and adjacency of connections between tiles.
 *
 * Implementing classes are serialized and deserialized using Jackson annotations
 * to support polymorphic behavior, enabling different connector types.
 */
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



public interface Connectors extends Serializable {

    /**
     * Evaluates whether a connection to the given adjacent connector is legal based
     * on specific rules defined for the connector types.
     *
     * @param Adjacent the connector to evaluate legality with
     * @return true if the connection to the given adjacent connector is legal;
     *         otherwise false
     */
    boolean checkLegal(Connectors Adjacent);


    /**
     * Determines whether the current connector is considered adjacent to the given connector.
     * This method evaluates adjacency compatibility rules based on the specific implementation
     * of the Connectors interface.
     *
     * @param Adjacent the connector to check adjacency against
     */
    boolean checkAdjacent(Connectors Adjacent);


    /**
     * Checks whether this connector is exposed.
     *
     * This method provides a boolean value indicating the exposure status
     * of the connector. The specific implementation may determine the
     * conditions under which a connector is considered exposed.
     *
     * @return true if the connector is exposed, false otherwise.
     */
    @JsonIgnore
    boolean isExposed();

}
