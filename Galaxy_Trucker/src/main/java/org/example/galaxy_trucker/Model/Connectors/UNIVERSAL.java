package org.example.galaxy_trucker.Model.Connectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.io.Serializable;

/**
 * The UNIVERSAL class represents a special type of connector in the game that is
 * universally compatible with other connector types. It adheres to the rules and
 * behaviors defined by the {@link Connectors} interface.
 *
 * This class is implemented as a singleton, meaning that only one instance of the
 * UNIVERSAL type exists. The singleton instance can be accessed using the
 * {@link #getInstance()} method. The UNIVERSAL connector type is compatible with
 * all other connector types, making it a universal choice for connectivity in the system.
 *
 * Key characteristics of the UNIVERSAL connector type:
 * - Connections with adjacent connectors are always legal if they are of type SINGLE, DOUBLE, or UNIVERSAL.
 * - The UNIVERSAL connector type is considered adjacent to connectors of type SINGLE, DOUBLE, or UNIVERSAL.
 * - The UNIVERSAL connector type is always exposed.
 *
 * Methods:
 * - {@code getInstance()}: Provides access to the singleton instance of the UNIVERSAL connector type.
 * - {@code checkLegal(Connectors Adjacent)}: Determines if a connection to an adjacent
 *   connector is legal. Legal only if the adjacent connector*/
@JsonTypeName("UNIVERSAL")
public class UNIVERSAL implements Connectors, Serializable{

    /**
     * Singleton instance of the {@code UNIVERSAL} class.
     *
     * The {@code INSTANCE} variable represents the single, globally accessible instance
     * of the {@code UNIVERSAL} connector type. This connector type is unique in that it is
     * universally compatible with all other connector types in the game, including SINGLE,
     * DOUBLE, and UNIVERSAL connectors. This universal compatibility allows it to act as a
     * key enabler in the game for establishing flexible and legal connections.
     *
     * Characteristics of the {@code INSTANCE} variable:
     * - It ensures that only one instance of the UNIVERSAL connector exists (singleton pattern).
     * - It allows for thread-safe and global access to this unique instance.
     * - Connections with adjacent connectors are always legal if they are of type SINGLE,
     *   DOUBLE, or UNIVERSAL.
     * - This connector type is always exposed and is never restricted under the game's
     *   rules of connectivity and adjacency.
     *
     * This instance is created upon class loading and can be accessed via the
     * {@code UNIVERSAL.INSTANCE} field or through the {@link UNIVERSAL#getInstance()} method.
     */
    public static final UNIVERSAL INSTANCE = new UNIVERSAL();

    /**
     * Private constructor for the {@code UNIVERSAL} class.
     *
     * This constructor is private to enforce the singleton pattern, ensuring that
     * only one instance of the {@code UNIVERSAL} class exists throughout the application.
     * Instances of this class cannot be created directly and must be accessed via
     * the {@link #getInstance()} method.
     *
     * The {@code UNIVERSAL} type represents a unique connector in the game, which is
     * universally compatible with other connector types. It facilitates game logic
     * by always allowing legal connections and adjacency with other types such as
     * SINGLE, DOUBLE, and UNIVERSAL connectors.
     */
    private UNIVERSAL() {}

    /**
     * Returns the singleton instance of the {@code UNIVERSAL} class.
     *
     * This method ensures that the class adheres to the singleton design pattern by
     * providing access to the single instance of the {@code UNIVERSAL} connector type.
     *
     * @return the singleton instance of {@code UNIVERSAL}
     */
    @JsonCreator
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static UNIVERSAL getInstance() {
        return INSTANCE;
    }

    /**
     * Determines whether a connection to the specified adjacent connector
     * is legal based on the rules defined for the connector types.
     *
     * A connection is considered legal if the adjacent connector is of type
     * UNIVERSAL, SINGLE, or DOUBLE.
     *
     * @param Adjacent the connector to evaluate legality with
     * @return true if the connection to the given adjacent connector is legal;
     *         otherwise, false
     */
    @Override
    public boolean checkLegal(Connectors Adjacent) {
        return Adjacent == UNIVERSAL.INSTANCE || Adjacent == SINGLE.INSTANCE || Adjacent == DOUBLE.INSTANCE ;
    }

    /**
     * Checks adjacency compatibility between the current connector and the given adjacent connector.
     * This method evaluates adjacency based on specific compatibility rules defined for the connector types.
     *
     * @param Adjacent the connector to check for adjacency compatibility
     * @return true if the given connector is of type UNIVERSAL, SINGLE, or DOUBLE; false otherwise
     */
    @Override
    public boolean checkAdjacent(Connectors Adjacent) {
        return Adjacent == UNIVERSAL.INSTANCE || Adjacent == SINGLE.INSTANCE || Adjacent == DOUBLE.INSTANCE;
    }

    /**
     * Indicates whether the current connector is exposed.
     *
     * This implementation always returns {@code true}, signifying that the connector
     * is universally exposed irrespective of its state or context.
     *
     * @return {@code true}, as this connector is always exposed.
     */
    @JsonIgnore
    @Override
    public boolean isExposed() {
        return true;
    }
}
