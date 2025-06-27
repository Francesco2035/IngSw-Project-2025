package org.example.galaxy_trucker.Model.Connectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.io.Serializable;

/**
 * The SINGLE class represents a specific type of connector in the game that enforces
 * rules of connectivity and adjacency. It implements the {@link Connectors} interface
 * and adheres to its requirements for evaluating legal connections, adjacency, and exposure.
 *
 * This class is a singleton, meaning only one instance of the SINGLE type exists,
 * which can be accessed using the {@link #getInstance()} method. It is annotated
 * with {@code @JsonTypeName} to support serialization and deserialization using the type name "SINGLE".
 *
 * Key characteristics of the SINGLE connector type:
 * - Connections with adjacent connectors are legal only if the adjacent connector is
 *   of type SINGLE or UNIVERSAL.
 * - The SINGLE connector type is considered adjacent to connectors of type SINGLE or UNIVERSAL.
 * - The SINGLE connector type is always exposed.
 *
 * Methods:
 * - {@code getInstance()}: Returns the singleton instance of the SINGLE connector type.
 * - {@code checkLegal(Connectors Adjacent)}: Determines if a connection to an adjacent
 *   connector is legal. Legal only if the adjacent connector is of type SINGLE or UNIVERSAL.
 * - {@code checkAdjacent(Connectors Adjacent)}: Evaluates adjacency compatibility. Returns
 *   true for connectors of type SINGLE or UNIVERSAL.
 * - {@code isExposed()}: Indicates whether this connector is exposed. Always returns {@code true}.
 */
@JsonTypeName("SINGLE")
public class SINGLE implements Connectors , Serializable {

    /**
     * Singleton instance of the {@code SINGLE} class.
     *
     * The {@code INSTANCE} variable provides access to the sole instance of the {@code SINGLE} class.
     * This instance represents a connector of type "SINGLE" within the game, following these rules:
     * - Connections with adjacent connectors are legal only if the adjacent connector is of type SINGLE or UNIVERSAL.
     * - The SINGLE connector type is considered adjacent to connectors of type SINGLE or UNIVERSAL.
     * - The SINGLE connector type is always exposed.
     *
     * The {@code INSTANCE} variable ensures thread-safe and global access to the single instance of this class.
     */
    public static final SINGLE INSTANCE = new SINGLE();

    /**
     * Private constructor for the {@code SINGLE} class.
     *
     * This constructor is private to enforce the singleton pattern, ensuring that
     * only one instance of the {@code SINGLE} class can ever exist. The {@code SINGLE}
     * type represents a specific connector in the game with rules for connectivity,
     * adjacency, and exposure. Instances of this class cannot be created directly
     * and must be accessed via the {@link #getInstance()} method.
     */
    private SINGLE() {}

    /**
     * Returns the singleton instance of the {@code SINGLE} class.
     *
     * This method provides access to the unique instance of the {@code SINGLE} connector type,
     * adhering to the singleton design pattern. The instance represents a specific type of
     * connector in the game, enforcing rules of connectivity, adjacency, and exposure.
     *
     * @return the singleton instance of {@code SINGLE}
     */
    @JsonCreator
    @JsonIgnoreProperties(ignoreUnknown = true)

    public static SINGLE getInstance() {
        return INSTANCE;
    }

    /**
     * Determines if a connection to the given adjacent connector is legal.
     * A connection is considered legal if the adjacent connector is of type
     * SINGLE or UNIVERSAL.
     *
     * @param Adjacent the connector to evaluate legality with
     * @return true if the connection to the given adjacent connector is legal;
     *         otherwise, false
     */
    @Override
    public boolean checkLegal(Connectors Adjacent) {
        return Adjacent == SINGLE.INSTANCE || Adjacent == UNIVERSAL.INSTANCE;
    }

    /**
     * Checks adjacency compatibility between the current connector and the given adjacent connector.
     * This method determines if the adjacent connector is compatible for adjacency
     * based on the rules specific to the SINGLE connector type.
     *
     * @param Adjacent the connector to check for adjacency compatibility
     * @return true if the given connector is of type SINGLE or UNIVERSAL; false otherwise
     */
    @Override
    public boolean checkAdjacent(Connectors Adjacent) {
        return Adjacent == SINGLE.INSTANCE || Adjacent == UNIVERSAL.INSTANCE;
    }


    /**
     * Indicates whether this connector is exposed.
     *
     * The implementation for the SINGLE connector type always returns {@code true},
     * indicating that this connector is always exposed.
     *
     * @return {@code true}, as this connector is always exposed.
     */
    @JsonIgnore
    @Override
    public boolean isExposed() {
        return true;
    }
}
