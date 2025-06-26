package org.example.galaxy_trucker.Model.Tiles;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.galaxy_trucker.ClientServer.Messages.PlayerBoardEvents.RemoveTileEvent;
import org.example.galaxy_trucker.ClientServer.Messages.PlayerBoardEvents.TileEvent;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.Connectors.*;
import org.example.galaxy_trucker.Model.Goods.Goods;


import java.io.Serializable;
import java.util.*;

/**
 * Represents a tile object that can be part of a game board. Each tile is associated with a specific
 * component and has a set of connectors, coordinates, and rotation properties. Tile objects can
 * be manipulated, rotated, cloned, and updated as part of the game state.
 *
 * This class supports serialization for game state persistence and includes functionality to
 * interact with the parent player board and events.
 */
public class Tile implements Serializable {


    /**
     * Represents the unique identifier for a Tile instance.
     *
     * This field is used to differentiate between various Tile objects
     * within the game or system and serves as a primary reference key
     * for identification purposes.
     *
     * The ID is expected to be assigned and managed programmatically to ensure
     * uniqueness within the context of the application. It can be accessed
     * or modified through the corresponding getter and setter methods.
     */
    private int id;
    /**
     * Represents the component assigned to the tile.
     * The component determines specific characteristics or functionality of the tile.
     */
    private Component component;
    /**
     * Represents the PlayerBoard instance associated with a particular Tile.
     * This variable links the tile to the specific player's game board,
     * enabling management and interaction within the context of the player's gameplay state.
     */
    private PlayerBoard playerBoard;
    /**
     * Represents the x-coordinate of the tile on the player board.
     *
     * The x variable denotes the horizontal position of the tile,
     * typically used to determine its placement or interactions within the
     * game board. This value is integral to positioning, movement,
     * and other operations involving the tile's spatial attributes.
     */
    int x;
    /**
     * Represents the y-coordinate of this Tile instance on the associated PlayerBoard.
     *
     * This variable is used to determine the vertical position of the Tile in the grid
     * system of a PlayerBoard. It plays a crucial role in the placement, validation,
     * and interaction of the Tile within the game.
     */
    int y;
    /**
     * Represents the rotation state of a tile component in the game.
     * This variable holds the current rotational orientation of the tile, expressed as an integer.
     *
     * The value of this variable determines how the tile is positioned on the game board
     * and changes when rotation methods are invoked. The rotation logic is typically implemented
     * using a cyclic enumeration of possible orientations (e.g., 0, 90, 180, 270 degrees).
     *
     * Modification of this variable should be handled exclusively via the provided methods
     * to ensure consistency and proper handling within the game logic.
     */
    int rotation = 0;
    /**
     * Indicates whether the tile has been selected or chosen for an action
     * or operation during the gameplay.
     *
     * This variable is used to track the selection state of the instance,
     * allowing the game logic to differentiate between tiles that are actively
     * in play versus those that are inactive or not currently selected.
     *
     * The state of this variable can be modified using the appropriate setter
     * method {@code setChosen} and queried with the getter method {@code getChosen}.
     */
    private boolean chosen = false;
    /**
     * Represents the list of connector components associated with the tile.
     *
     * This field defines the connectors attached to a specific tile, which play a
     * crucial role in determining the connections and interactions between tiles
     * on the player board. Each connector in the list follows the Connectors
     * interface, allowing for multiple types such as SINGLE, DOUBLE, UNIVERSAL, and more.
     *
     * The connectors are serialized and deserialized using JSON annotations for
     * proper data exchange. They form part of the tile's configuration and can
     * influence gameplay by managing legality and adjacency of connections.
     *
     * @see Connectors
     * @see #getConnectors()
     * @see #setConnectors(ArrayList)
     */
    @JsonProperty("connectors")
    private ArrayList<Connectors> connectors;

    /**
     * Indicates whether this tile is currently available for use or interaction.
     *
     * This variable serves as a flag to determine the tile's availability status
     * within the game logic. A value of {@code true} means the tile is available,
     * whereas {@code false} indicates it is not available.
     */
    private boolean available;


    /**
     * Represents a Tile object in a system.
     * This class serves as a foundational component for creating and
     * managing tile-based structures, layouts, or entities.
     *
     * The default constructor initializes a new Tile instance with no
     * specific properties defined.
     */
    public Tile() {}

    /**
     * Constructs a Tile instance with the specified component and connectors.
     *
     * @param component the component associated with the Tile
     * @param connectors the connectors to be added to the Tile
     */
    public Tile(Component component, Connectors... connectors) {
        this.component = component;
        this.connectors = new ArrayList<>();
        this.connectors.addAll(Arrays.asList(connectors));
    }



//    public boolean controlDirections(PlayerBoard pb, int x, int y) {
//        return component.controlValidity(pb, x, y);
//    }


    //spostare la rotazione delle direzioni protette in una chiamata della classe di shield generator

    //metodi rotate per le tiles


    /**
     * Rotates the tile counterclockwise by adjusting its connectors and component orientation.
     *
     * This method performs two main operations:
     * 1. Rotates the list of connectors associated with the tile by one position in the counterclockwise direction.
     * 2. Invokes the rotation logic of the tile's component to update its orientation accordingly.
     *
     * The rotation ensures that the tile's connectors and the underlying component are
     * aligned to reflect the new orientation after the operation. This is useful in
     * scenarios where the tile's configuration needs to be adjusted dynamically
     * during gameplay.
     */
    public void RotateSx(){
        Collections.rotate(this.connectors, -1);
        this.getComponent().rotate(false);
    }

    /**
     * Rotates the Tile instance in the clockwise direction by adjusting its connectors
     * and invoking the rotation of its associated component.
     *
     * The method performs two primary actions:
     * 1. Rotates the list of connectors by one position to represent a clockwise rotation.
     * 2. Invokes the `rotate` method on the associated component with a `true` parameter
     *    indicating a clockwise rotation.
     */
    public void RotateDx(){
        Collections.rotate(this.connectors, 1);
        this.getComponent().rotate(true);
    }



    /**
     * Creates and returns a deep copy of this Tile object with updated references
     * for the cloned PlayerBoard and other related components.
     *
     * @param clonedPlayerBoard the PlayerBoard instance to associate with the cloned Tile
     * @return a new Tile object that is a deep copy of this Tile
     */
    public Tile clone(PlayerBoard clonedPlayerBoard){
        Tile clonedTile = new Tile();
        clonedTile.setId(this.id);
        clonedTile.setRotation(this.rotation);
        clonedTile.setPlayerBoard(clonedPlayerBoard);
        Component clonedComponent = this.component.clone(clonedPlayerBoard);
        clonedComponent.setTile(clonedTile);
        clonedTile.setComponent(clonedComponent);
        clonedTile.setConnectors(new ArrayList<>(this.connectors));
        clonedTile.chosen = this.chosen;
        return clonedTile;

    }




//    public boolean isAvailable() {return available;}
//    public void setAvailable(boolean available) {this.available = available;}


    /**
     * Retrieves the unique identifier associated with this object.
     *
     * @return the unique identifier as an integer
     */
    //  json required
    public int getId() {
        return id;
    }
    /**
     * Sets the identifier for this instance.
     *
     * @param id the unique identifier to be assigned
     */
    public void setId(int id) {
        this.id = id;
    }
    /**
     * Retrieves the component associated with the tile.
     *
     * @return the component of the tile
     */
    public Component getComponent() {
        return component;
    }
    /**
     * Sets the component associated with this tile.
     *
     * @param component the component to be assigned to this tile
     */
    public void setComponent(Component component) {
        this.component = component;
    }
    /**
     * Retrieves the list of connectors associated with the tile.
     *
     * The connectors define the connection interfaces of the tile
     * and determine its compatibility with adjacent tiles in the system.
     *
     * @return an ArrayList containing the connectors associated with the tile
     */
    public ArrayList<Connectors> getConnectors() {return connectors;}
    /**
     * Sets the list of connectors for the tile.
     *
     * The connectors define the points on a tile where connections can be made
     * to other tiles, determining adjacency and compatibility rules. This method
     * allows updating the connectors for the tile as needed.
     *
     * @param connectors an ArrayList of {@link Connectors} representing the connectors
     *                   to be assigned to the tile. Must not be null and must contain
     *                   valid connector instances implementing the {@code Connectors} interface.
     */
    public void setConnectors(ArrayList<Connectors> connectors) {this.connectors = connectors;}



    /**
     * Checks the validity of directions based on the provided player board and coordinates.
     *
     * @param pb the PlayerBoard instance for which the directions need to be validated
     * @param x the x-coordinate to be checked
     * @param y the y-coordinate to be checked
     * @return true if the directions are valid, false otherwise
     */
    public boolean controlDirections(PlayerBoard pb, int x, int y) {
        return component.controlValidity(pb, x, y);
    }


    /**
     * Sends updates related to the current tile, including cargo details, human count, alien presence,
     * and battery information, to the associated player board.
     *
     * @param cargo A list of goods being carried (may be null).
     * @param humans The number of humans present on the tile.
     * @param purpleAlien A boolean indicating the presence of a purple alien on the tile.
     * @param brownAlien A boolean indicating the presence of a brown alien on the tile.
     * @param batteries The number of batteries available on the tile.
     */
    public void sendUpdates(ArrayList<Goods> cargo, int humans,boolean purpleAlien, boolean brownAlien, int batteries)  {
        playerBoard.sendUpdates(new TileEvent(id,x,y,cargo,humans,purpleAlien,brownAlien,batteries,rotation, connectors));
        if (cargo!=null) {
            for (Goods g : cargo) {
                System.out.println(g.getClass());
            }
        }
    }

    /**
     * Sends updates related to a tile removal event. This method processes a RemoveTileEvent
     * and sends the corresponding updates using the player board.
     *
     * @param event the RemoveTileEvent that triggers the update; contains information about
     *              the tile being removed and its associated properties
     */
    public void sendUpdates(RemoveTileEvent event) {
        ArrayList<Connectors> noneConnectors = new ArrayList<>();
        noneConnectors.add(NONE.INSTANCE);
        noneConnectors.add(NONE.INSTANCE);
        noneConnectors.add(NONE.INSTANCE);
        noneConnectors.add(NONE.INSTANCE);
        playerBoard.sendUpdates(new TileEvent(157,x,y,null, 0,false, false, 0, 0,  noneConnectors));
    }


    /**
     * Sets the player's board for this tile.
     *
     * @param playerBoard the PlayerBoard object to be associated with this tile
     */
    public void setPlayerBoard(PlayerBoard playerBoard) {
        this.playerBoard = playerBoard;
    }

    /**
     * Sets the x-coordinate of the tile.
     *
     * @param x the x-coordinate to be set for the tile
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Sets the y-coordinate value for the Tile object.
     *
     * @param y the integer value to set as the y-coordinate
     */
    public void setY(int y) {
        this.y = y;
    }


    /**
     * Marks the tile as chosen by setting the `chosen` field to true.
     *
     * This method updates the state of the tile to indicate that it has been selected
     * or marked for a specific purpose. The `chosen` field is a boolean property
     * representing the selection status of the tile.
     */
    public void setChosen(){
        chosen = true;
    }
    /**
     * Retrieves the value of the chosen state for the tile.
     *
     * @return true if the tile is chosen; false otherwise
     */
    public boolean getChosen(){
        return chosen;
    }

    /**
     * Sets the rotation of the object by a specified number of degrees. Updates the current rotation value
     * and ensures that it stays within a range of 0 to 359 degrees.
     *
     * @param rotations the number of degrees to rotate the object. Positive values indicate clockwise
     *                  rotation, while negative values indicate counter-clockwise rotation.
     */
    public void setRotation(int rotations) {
        rotation += rotations;
        rotation = rotation % 360;
    }
}




