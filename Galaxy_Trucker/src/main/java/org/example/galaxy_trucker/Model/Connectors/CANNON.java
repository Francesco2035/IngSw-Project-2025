package org.example.galaxy_trucker.Model.Connectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.io.Serializable;

/**
 * The CANNON class represents a type of connector for tiles in the game
 * and implements the {@link Connectors} interface. This class is a singleton
 * and provides methods to validate compatibility and adjacency rules
 * specific to the CANNON connector type.
 *
 * This connector type has the following characteristics:
 * - It is not considered legal for any adjacent connectors.
 * - It does not support adjacency with other connector types.
 * - It is not exposed.
 *
 * The class is annotated with {@code @JsonTypeName} to support JSON
 * serialization and deserialization using the type name "CANNON".
 *
 * Methods:
 * - {@code getInstance()}: Returns the singleton instance of CANNON.
 * - {@code checkLegal(Connectors Adjacent)}: Evaluates if the connection is
 *   legal with a given adjacent connector. Always returns {@code false}.
 * - {@code checkAdjacent(Connectors Adjacent)}: Determines adjacency
 *   compatibility with another connector. Always returns {@code false}.
 * - {@code isExposed()}: Indicates whether this connector is exposed.
 *   Always returns {@code false}.
 */
@JsonTypeName("CANNON")
public class CANNON implements Connectors, Serializable {

    /**
     * Singleton instance of the {@code CANNON} class.
     *
     * The {@code INSTANCE} variable provides access to the sole instance of the {@code CANNON} class.
     * This instance represents a connector of type "CANNON" in the game and enforces the following behaviors:
     * - Connections with adjacent connectors are always considered illegal.
     * - Adjacency with other connectors is not allowed.
     * - This connector type is not exposed.
     *
     * The {@code INSTANCE} variable ensures thread-safe and global access to the singular instance.
     */
    public static final CANNON INSTANCE = new CANNON();

    /**
     * Private constructor for the {@code CANNON} singleton class.
     *
     * The constructor is marked private to enforce the singleton pattern,
     * ensuring that only one instance of the class can exist. Instances
     * of this class are used as a specific type of connector within the game's
     * tile system, which adheres to the rules of the {@link Connectors} interface.
     */
    private CANNON() {}

    /**
     * Returns the singleton instance of the {@code CANNON} class.
     *
     * This method provides access to the unique instance of the {@code CANNON} connector type,
     * ensuring that the class adheres to the singleton design pattern.
     *
     * @return the singleton instance of {@code CANNON}
     */
    @JsonCreator
    @JsonIgnoreProperties(ignoreUnknown = true)

    public static CANNON getInstance() {
        return INSTANCE;
    }

    /**
     * Evaluates whether a connection to the given adjacent connector is legal.
     * The method always returns false as the CANNON connector type is not legal
     * for any adjacent connectors.
     *
     * @param Adjacent the connector to evaluate adjacency legality with
     * @return false always, as CANNON is not considered legal for any adjacent connectors
     */
    @Override
    public boolean checkLegal(Connectors Adjacent) {
        return false;
    }

    /**
     * Determines whether the current connector is considered adjacent to the given connector.
     * This method evaluates adjacency compatibility rules specific to the CANNON connector type.
     *
     * @param Adjacent the connector to check adjacency against
     * @return false, as the CANNON connector type does not support adjacency with any other connector
     */
    @Override
    public boolean checkAdjacent(Connectors Adjacent){
        return false;
    }


    /**
     * Determines whether this connector is exposed.
     *
     * The "exposed" status typically indicates whether the connector is in an
     * active or visible state. This implementation always returns {@code false},
     * indicating that the connector is never exposed.
     *
     * @return {@code false}, as this connector is not exposed.
     */
    @JsonIgnore
    @Override
    public boolean isExposed() {
        return false;
    }
}
