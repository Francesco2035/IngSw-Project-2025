package org.example.galaxy_trucker.Model.Connectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.io.Serializable;

/**
 * The DOUBLE class represents a type of connector for tiles in the game
 * and implements the {@link Connectors} interface. This class is a singleton
 * and provides methods to validate compatibility and adjacency rules
 * specific to the DOUBLE connector type.
 *
 * This connector type has the following characteristics:
 * - It is considered legal for connections with DOUBLE and UNIVERSAL connectors.
 * - It supports adjacency with DOUBLE and UNIVERSAL connectors.
 * - It is exposed.
 *
 * The class is annotated with {@code @JsonTypeName} to support JSON
 * serialization and deserialization using the type name "DOUBLE".
 *
 * Methods:
 * - {@code getInstance()}: Returns the singleton instance of DOUBLE.
 * - {@code checkLegal(Connectors Adjacent)}: Evaluates if the connection is
 *   legal with a given adjacent connector.
 * - {@code checkAdjacent(Connectors Adjacent)}: Determines adjacency
 *   compatibility with another connector.
 * - {@code isExposed()}: Indicates whether this connector is exposed.
 */
@JsonTypeName("DOUBLE")
public class DOUBLE implements Connectors, Serializable {

    /**
     * Singleton instance of the {@code DOUBLE} class.
     *
     * The {@code INSTANCE} variable provides access to the sole instance of the {@code DOUBLE} class.
     * This instance represents a connector of type "DOUBLE" in the game and enforces the following behaviors:
     * - Connections with adjacent connectors are considered legal for DOUBLE and UNIVERSAL connector types.
     * - Adjacency is supported with DOUBLE and UNIVERSAL connectors.
     * - This connector type is exposed.
     *
     * The {@code INSTANCE} variable ensures thread-safe and global access to the singular instance.
     */
    public static final DOUBLE INSTANCE = new DOUBLE();

    /**
     * Private constructor for the {@code DOUBLE} class.
     *
     * This constructor is marked as private to enforce the singleton pattern,
     * ensuring that only one instance of the {@code DOUBLE} class exists.
     * The singleton instance represents a specific type of connector in the game,
     * adhering to rules defined by the {@link Connectors} interface.
     */
    private DOUBLE() {}

    /**
     * Returns the singleton instance of the {@code DOUBLE} class.
     *
     * This method provides access to the unique instance of the {@code DOUBLE} connector type,
     * ensuring that the class adheres to the singleton design pattern. This instance represents
     * the predefined rules and states specific to the DOUBLE connector type, as defined in the game's logic.
     *
     * @return the singleton instance of {@code DOUBLE}
     */
    @JsonCreator
    @JsonIgnoreProperties(ignoreUnknown = true)

    public static DOUBLE getInstance() {
        return INSTANCE;
    }

    /**
     * Evaluates whether the connection to the specified adjacent connector
     * is legal for the current connector type.
     *
     * @param Adjacent the connector to evaluate legality with
     * @return true if the connection to the given adjacent connector is legal,
     *         otherwise false
     */
    @Override
    public boolean checkLegal(Connectors Adjacent) {
        return Adjacent == DOUBLE.INSTANCE || Adjacent == UNIVERSAL.INSTANCE;
    }

    /**
     * Determines whether the current connector is adjacent to the given connector.
     * Adjacency is evaluated based on the compatibility rules of the DOUBLE and UNIVERSAL
     * connector types.
     *
     * @param Adjacent the connector to check adjacency against
     * @return true if the given connector is either DOUBLE or UNIVERSAL; false otherwise
     */
    @Override
    public boolean checkAdjacent(Connectors Adjacent) {
        return Adjacent == DOUBLE.INSTANCE || Adjacent == UNIVERSAL.INSTANCE;
    }


    /**
     * Determines whether this connector is exposed.
     *
     * This method defines the "exposed" status of the connector,
     * where being exposed typically indicates whether the connector
     * is in an active or visible state. For this implementation,
     * the method always returns {@code true}, signifying that
     * the connector is exposed.
     *
     * @return {@code true}, indicating that this connector is exposed.
     */
    @JsonIgnore
    @Override
    public boolean isExposed() {
        return true;
    }
}
