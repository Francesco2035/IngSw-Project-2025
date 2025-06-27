package org.example.galaxy_trucker.Model.Tiles;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.galaxy_trucker.Exceptions.InvalidInput;
import org.example.galaxy_trucker.Model.Boards.Actions.*;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.PlayerStates.PlayerState;


import java.io.Serializable;

/**
 * Abstract base class representing a component in the game.
 * Components are designed to be extended by specific component types and can
 * represent various functionalities in the game such as utilities, housing,
 * storage, and other elements.
 *
 * This class provides common methods and properties that all components
 * inherit, while allowing extending classes to define specific functionality.
 *
 * Serialization with JSON is supported, and different subtypes can be identified
 * through the "componentType" property for proper deserialization.
 *
 * Subclasses must implement methods for rotation, validation, insertion,
 * removal, and cloning. Additionally, components can accept specific actions
 * via the visitor pattern.
 *
 * The class integrates with `PlayerBoard` and uses a `Tile` object to represent
 * the position of the component.
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXTERNAL_PROPERTY,
        property = "componentType"
)

@JsonSubTypes({
        @JsonSubTypes.Type(value = PlasmaDrill.class, name = "plasmaDrill"),
        @JsonSubTypes.Type(value = HotWaterHeater.class, name = "hotWaterHeater"),
        @JsonSubTypes.Type(value = PowerCenter.class, name = "powerCenter"),
        @JsonSubTypes.Type(value = StorageCompartment.class, name = "storageCompartment"),
        @JsonSubTypes.Type(value = ModularHousingUnit.class, name = "modularHousingUnit"),
        @JsonSubTypes.Type(value = SewerPipes.class, name = "sewerPipes"),
        @JsonSubTypes.Type(value = SpecialStorageCompartment.class, name = "specialStorageCompartment"),
        @JsonSubTypes.Type(value = AlienAddons.class, name = "alienAddons"),
        @JsonSubTypes.Type(value = ShieldGenerator.class, name = "shieldGenerator"),
        @JsonSubTypes.Type(value = SpaceVoid.class, name = "spaceVoid")
})

public abstract class Component implements Serializable {


    /**
     * Represents the type identifier for a component within the game.
     * This variable is used to distinguish between different types of components
     * and is an integral part of the serialization and deserialization process
     * when interacting with system properties or external data structures.
     *
     * The value of this variable is expected to correspond to a specific component
     * type, as referenced in the broader game context, ensuring proper linkage and
     * functionality between components and their behaviors.
     */
    int type;

    /**
     * Represents the position and configuration of a component within the game.
     * The `tile` variable associates a specific `Tile` object with the component,
     * indicating where the component is placed on the player board.
     *
     * The `Tile` object contains attributes such as coordinates, rotation, and
     * connectivity information, which are used to determine the component's state
     * and interactions within the game board.
     *
     * This variable is essential for linking components to their respective spatial
     * locations and for performing operations like placement, removal, and rotation
     * on the board.
     */
    Tile tile;

    /**
     * Default constructor for the Component class.
     *
     * This initializes a new Component object. As an abstract class, Component is
     * intended to be extended by various specific implementations to represent
     * distinct elements within the game. This constructor provides base setup
     * needed for component instances.
     *
     * The Component class integrates with a tile-based system and can interact
     * with a PlayerBoard. Subclasses are required to implement and extend
     * specific functionalities.
     */
    public Component() {}

    /**
     * Rotates the component in the specified direction.
     * The rotation may alter the orientation of the component within the game.
     *
     * @param direction The direction of the rotation.
     *                  If true, the component rotates clockwise;
     *                  if false, it rotates counterclockwise.
     */
    public abstract void rotate(Boolean direction);

    /**
     * Validates whether a specific tile position on the player's board is valid for the
     * component to be placed. The method determines if the provided coordinates are
     * appropriate and adhere to game rules or constraints.
     *
     * @param pb The player's board where the validity of the position will be checked.
     * @param x The x-coordinate of the tile to validate.
     * @param y The y-coordinate of the tile to validate.
     * @return A boolean value indicating whether the position at (x, y) on the player's
     *         board is valid (true) or not (false).
     */
    public abstract boolean controlValidity(PlayerBoard pb, int x, int y);

    /**
     * Inserts the component into the player's board at the specified coordinates.
     * Subclasses should implement the specific behavior for insertion, ensuring
     * that the component is placed and associated correctly within the player board
     * system.
     *
     * @param playerBoard the player's board where the component is being inserted
     * @param x the x-coordinate on the player's board where the component will be placed
     * @param y the y-coordinate on the player's board where the component will be placed
     */
    public abstract void insert(PlayerBoard playerBoard, int x, int y);
    /**
     * Removes the current component from the specified player board.
     * This method must be implemented by subclasses to define the behavior of
     * removal, such as updating the game state or removing references to the component.
     *
     * @param playerBoard the player board from which the component should be removed
     */
    public abstract void remove(PlayerBoard playerBoard) ;

/**
 * Retrieves the type of the component.
 *
 * The type is represented as an integer and provides identification
 * for different component subtypes in the game.
 *
 * @return the integer value representing the component's type
 */
//  metodi per json
    public int getType() {return type;}
    /**
     * Sets the type of the component.
     *
     * @param type an integer representing the type of the component
     */
    public void setType(int type) {this.type = type;}




    /**
     * Accepts a visitor implementing the ComponentAction interface and performs actions or
     * validations based on the provided game state. The visitor pattern allows dynamic
     * dispatch of specific actions based on the type of the component and its interaction
     * logic. This method throws an InvalidInput exception by default to indicate that the
     * specific action is not valid or supported for the current component.
     *
     * @param visitor the visitor implementing the ComponentAction interface, representing
     *                an action or operation to be performed on the component
     * @param state the game state of the player, providing context for validating the action
     *              or determining its impact
     * @throws InvalidInput when the action represented by the visitor is not valid
     *                      for the specific component or player state
     */
    public void accept(ComponentAction visitor, PlayerState state){
        throw new InvalidInput("Invalid input for the specific action");
    };

    /**
     * Creates and returns a deep copy of the current component associated with
     * the given player board. The cloned component maintains its state while
     * being independent of the original instance.
     *
     * @param playerBoard the player's board associated with the cloned component
     * @return a deep copy of the current component
     */
    public abstract Component clone(PlayerBoard playerBoard);

    /**
     * Sets the {@code Tile} object associated with this component.
     *
     * This method assigns a specific {@code Tile} to the component, defining
     * its positional and contextual relationship within the game. The {@code Tile}
     * represents the location and configuration where the component resides on the board.
     *
     * @param tile the {@code Tile} to associate with this component
     */
    public void setTile(Tile tile) {
        this.tile = tile;
    }



}
