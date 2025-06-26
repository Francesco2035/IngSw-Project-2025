package org.example.galaxy_trucker.Model.Connectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.io.Serializable;

/**
 * The NONE class represents a special type of connector in the game that enforces
 * strict rules of non-connectivity. It implements the {@link Connectors} interface
 * and adheres to its requirements for evaluating legal connections, adjacency, and exposure.
 *
 * This class is a singleton, meaning only one instance of the NONE type exists,
 * which can be accessed using the {@link #getInstance()} method.
 *
 * Key characteristics of the NONE connector type:
 * - Connections with adjacent connectors are only legal if the adjacent connector
 *   is also of the NONE type.
 * - The NONE connector type is never considered adjacent to any other connector type.
 * - The NONE connector type is not exposed under any circumstances.
 *
 * The class is annotated with {@code @JsonTypeName} to enable proper serialization
 * and deserialization in JSON using the type name "NONE".
 *
 * Methods:
 * - {@code getInstance()}: Returns the singleton instance of the NONE connector type.
 * - {@code checkLegal(Connectors Adjacent)}: Determines if a connection to an adjacent
 *   connector is legal. Legal only if the adjacent connector is also of type NONE.
 * - {@code checkAdjacent(Connectors Adjacent)}: Evaluates adjacency compatibility. Always returns {@code false}.
 * - {@code isExposed()}: Indicates whether this connector is exposed. Always returns {@code false}.
 */
@JsonTypeName("NONE")
@JsonIgnoreProperties(ignoreUnknown = true)
public class NONE implements Connectors , Serializable {

    /**
     * Singleton instance of the {@code NONE} class.
     *
     * The {@code INSTANCE} variable provides access to the sole instance of the {@code NONE} class.
     * This instance represents a connector of type "NONE" within the game, following these rules:
     * - Connections with adjacent connectors are only permitted if the adjacent connector is also of type NONE.
     * - The NONE connector type is never considered adjacent to any other connector type.
     * - The NONE connector type is not exposed under any circumstances.
     *
     * The {@code INSTANCE} variable ensures thread-safe and global access to the single instance of this class.
     */
    public static final NONE INSTANCE = new NONE();

    /**
     * Private constructor for the {@code NONE} singleton class.
     *
     * This constructor is private to enforce the singleton pattern, ensuring that
     * only one instance of the {@code NONE} class can ever exist. The {@code NONE}
     * type represents a specialized connector in the game, adhering to strict rules
     * of non-connectivity. Instances of this class cannot be created directly and
     * must be accessed via the {@link #getInstance()} method.
     */
    private NONE() {}

    /**
     * Returns the singleton instance of the {@code NONE} class.
     *
     * This method provides access to the unique instance of the {@code NONE} connector type,
     * ensuring that the class adheres to the singleton design pattern.
     *
     * @return the singleton instance of {@code NONE}
     */
    @JsonCreator
    public static NONE getInstance() {
        return INSTANCE;
    }

    /**
     * Determines if a connection to the given adjacent connector is legal.
     * A connection is considered legal only if the adjacent connector is of type {@code NONE}.
     *
     * @param Adjacent the connector to evaluate legality with
     * @return true if the adjacent connector is of type {@code NONE}, false otherwise
     */
    @Override
    public boolean checkLegal(Connectors Adjacent) {
        return Adjacent == NONE.INSTANCE;
    }

    /**
     * Determines whether the current connector is considered adjacent to the given connector.
     * This method evaluates adjacency compatibility rules based on the specific implementation
     * of the Connectors interface. For the NONE connector type, it always returns {@code false},
     * as NONE is never adjacent to any other connector type.
     *
     * @param Adjacent the connector to evaluate adjacency with
     * @return {@code false}, since the NONE connector type does not support adjacency with any other connector
     */
    @Override
    public boolean checkAdjacent(Connectors Adjacent) {
        return false;
    }


    /**
     * Determines whether this connector is exposed.
     *
     * This implementation always returns {@code false}, indicating that the connector
     * is never exposed under any circumstances.
     *
     * @return {@code false}, as this connector is not exposed.
     */
    @JsonIgnore
    @Override
    public boolean isExposed() {
        return false;
    }
}
