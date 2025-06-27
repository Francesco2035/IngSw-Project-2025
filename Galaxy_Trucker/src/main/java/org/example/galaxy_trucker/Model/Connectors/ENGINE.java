package org.example.galaxy_trucker.Model.Connectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.example.galaxy_trucker.Model.Tiles.Tile;

import java.io.Serializable;

/**
 * The ENGINE class represents a type of connector for tiles in the game
 * and implements the {@link Connectors} interface. This class is a singleton
 * and provides methods to validate compatibility and adjacency rules
 * specific to the ENGINE connector type.
 *
 * This connector type has the following characteristics:
 * - It is not considered legal for any adjacent connectors.
 * - It does not support adjacency with other connector types.
 * - It is not exposed.
 *
 * The class is annotated with {@code @JsonTypeName} to support JSON
 * serialization and deserialization using the type name "ENGINE".
 *
 * Methods:
 * - {@code getInstance()}: Returns the singleton instance of ENGINE.
 * - {@code checkLegal(Connectors Adjacent)}: Evaluates if the connection is
 *   legal with a given adjacent connector. Always returns {@code false}.
 * - {@code checkAdjacent(Connectors Adjacent)}: Determines adjacency
 *   compatibility with another connector. Always returns {@code false}.
 * - {@code isExposed()}: Indicates whether this connector is exposed.
 *   Always returns {@code false}.
 */
@JsonTypeName("ENGINE")
public class ENGINE implements Connectors , Serializable {

    /**
     * Singleton instance of the {@code ENGINE} class.
     *
     * The {@code INSTANCE} variable provides access to the sole instance of the {@code ENGINE} class.
     * This instance represents a connector of type "ENGINE" within the game and enforces the following behaviors:
     * - Connections with adjacent connectors are always considered illegal.
     * - Adjacency with other connectors is not allowed.
     * - This connector type is not exposed.
     *
     * The {@code INSTANCE} variable ensures thread-safe and global access to the singular instance.
     */
    public static final ENGINE INSTANCE = new ENGINE();

    /**
     * Private constructor for the {@code ENGINE} singleton class.
     *
     * This constructor is marked private to enforce the singleton design pattern,
     * ensuring that only one instance of the {@code ENGINE} class can ever exist.
     * The singleton instance represents the unique behavior and characteristics
     * of the ENGINE connector type, such as its incompatibility with all other
     * connector types, its lack of adjacency support, and its non-exposed state.
     */
    private ENGINE() {}

    /**
     * Returns the singleton instance of the {@code ENGINE} class.
     *
     * This method ensures that only a single instance of the {@code ENGINE} connector type
     * exists throughout the application. The instance follows the singleton design pattern
     * and represents the ENGINE connector type in the game's tile system.
     *
     * @return the singleton instance of {@code ENGINE}
     */
    @JsonCreator
    @JsonIgnoreProperties(ignoreUnknown = true)

    public static ENGINE getInstance() {
        return INSTANCE;
    }

    /**
     * Evaluates whether a connection to the specified adjacent connector is legal.
     * For the ENGINE connector, connections with any adjacent connector are always illegal.
     *
     * @param Adjacent the connector to evaluate legality with
     * @return false always, as ENGINE is not considered legal for any adjacent connectors
     */
    @Override
    public boolean checkLegal(Connectors Adjacent) {
        return false;
    }

    /**
     * Determines whether the current connector is considered adjacent to the given connector.
     * This method evaluates adjacency compatibility rules specific to the ENGINE connector type.
     *
     * @param Adjacent the connector to check adjacency against
     * @return false, as the ENGINE connector type does not support adjacency with any other connector
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
