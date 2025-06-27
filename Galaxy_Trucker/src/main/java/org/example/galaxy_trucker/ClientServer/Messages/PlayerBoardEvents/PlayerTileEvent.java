package org.example.galaxy_trucker.ClientServer.Messages.PlayerBoardEvents;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.galaxy_trucker.ClientServer.Messages.Event;
import org.example.galaxy_trucker.ClientServer.Messages.EventVisitor;
import org.example.galaxy_trucker.Model.Connectors.Connectors;
import org.example.galaxy_trucker.Model.Goods.Goods;

import java.util.ArrayList;

/**
 * The PlayerTileEvent class represents an event that stores information about a player's tile
 * state during a game event. This class extends the TileEvent class and implements the Event
 * interface. It provides specific details about a player's tile and supports the visitor
 * pattern for event handling.
 *
 * This class is designed to encapsulate data associated with the player, such as player name,
 * tile id, position (x and y coordinates), cargo, humans, alien occupancy, battery count,
 * rotation, and connectors. It is intended to be serialized and deserialized using JSON for seamless
 * communication and state management.
 *
 * Constructors and methods are provided to initialize and retrieve property values.
 */
public class PlayerTileEvent extends TileEvent implements Event  {

    /**
     * Represents the name of the player associated with the tile event.
     * This field specifies the player involved in the event and is used
     * for identifying the relevant player in the context of the event.
     */
    private String playerName;
    /**
     * Represents the unique identifier of the tile event on the player board.
     * This ID is used to differentiate between different tile events.
     */
    private int id;
    /**
     * Represents the number of humans present on a specific tile in the player's board.
     * This value indicates the count of human characters involved in the tile event.
     */
    private int humans;
    /**
     * Represents the x-coordinate of a tile on the player board in the context of a PlayerTileEvent.
     * This value indicates the horizontal position of the tile within the board's grid.
     */
    private int x;
    /**
     * Represents the y-coordinate of the tile on the player's board.
     * It is used to determine the vertical position of a tile within the game.
     */
    private int y;
    /**
     * Indicates whether a purple alien is present on the tile associated
     * with this event.
     *
     * This boolean flag represents the presence or absence of a purple alien
     * on the tile in the player's board during a specific event. It is used
     * within the context of the PlayerTileEvent class to determine the state
     * of the tile regarding purple alien occupants.
     */
    private boolean purpleAlien;
    /**
     * Represents the presence of a brown alien on a tile.
     * This variable is a boolean flag used to indicate whether a brown alien
     * is present in a specific tile associated with a player's event.
     *
     * It is a key attribute relevant to the game's tile event logic,
     * especially when determining the entities or effects on a given tile.
     */
    private boolean brownAlien;
    /**
     * Represents the number of batteries present on the player's tile.
     * Batteries can be used to power various components or systems in the game.
     */
    private int batteries;
    /**
     * Represents the collection of goods stored as cargo in the context of a player's tile event.
     * The type of goods is defined by the {@code Goods} interface, which can represent multiple
     * specific types such as BLUE, GREEN, YELLOW, or RED. Each item in the cargo list corresponds
     * to an instance of {@code Goods}.
     */
    private ArrayList<Goods> cargo;
    /**
     * Represents the rotation angle of a tile in a PlayerTileEvent.
     * This value denotes the orientation of the tile, typically measured
     * in degrees or as a standardized internal representation.
     */
    private int rotation;
    /**
     * Represents a collection of connectors associated with a tile in the player's event.
     * Each connector defines a specific type of connection mechanism and can be used
     * to determine compatibility and adjacency between tiles.
     */
    private ArrayList<Connectors> connectors;

    /**
     * Default constructor for the PlayerTileEvent class.
     * Creates an instance of PlayerTileEvent with default or uninitialized values.
     * This constructor is primarily used for deserialization or initialization purposes.
     */
    public PlayerTileEvent() {

    }

    /**
     * Constructs a PlayerTileEvent object that represents a tile event on the player board with specific attributes.
     *
     * @param playerName the name of the player associated with the tile event
     * @param id the unique identifier of the tile
     * @param x the x-coordinate of the tile on the player board
     * @param y the y-coordinate of the tile on the player board
     * @param cargo the list of goods present on the tile
     * @param humans the number of humans present on the tile
     * @param purpleAlien a boolean indicating if a purple alien is present on the tile
     * @param brownAlien a boolean indicating if a brown alien is present on the tile
     * @param batteries the number of batteries present on the tile
     * @param rotation the rotation angle of the tile
     * @param connectors the list of connectors associated with the tile
     */
    @JsonCreator
    public PlayerTileEvent(
            @JsonProperty("player") String playerName,
            @JsonProperty("id") int id,
            @JsonProperty("x") int x,
            @JsonProperty("y") int y,
            @JsonProperty("cargo") ArrayList<Goods> cargo,
            @JsonProperty("humans") int humans,
            @JsonProperty("purpleAlien") boolean purpleAlien,
            @JsonProperty("brownAlien") boolean brownAlien,
            @JsonProperty("batteries") int batteries,
            @JsonProperty("rotation") int rotation,
            @JsonProperty("connectors") ArrayList<Connectors> connectors
    ) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.cargo = cargo;
        this.humans = humans;
        this.purpleAlien = purpleAlien;
        this.brownAlien = brownAlien;
        this.batteries = batteries;
        this.rotation = rotation;
        this.connectors = connectors;
        this.playerName = playerName;
    }

    /**
     * Returns a message associated with the event.
     *
     * @return an empty string as the default message.
     */
    @JsonIgnore
    @Override
    public String message() {
        return "";
    }

    /**
     * Retrieves the unique identifier of the event.
     *
     * @return the ID of the event as an integer.
     */
    public int getId() {
        return id;
    }

    /**
     * Retrieves the number of humans associated with this event.
     *
     * @return the number of humans present
     */
    public int getHumans() {
        return humans;
    }

    /**
     * Retrieves the x-coordinate associated with this event.
     *
     * @return the x-coordinate of the event.
     */
    public int getX() {
        return x;
    }

    /**
     * Retrieves the y-coordinate of the tile event.
     *
     * @return the y-coordinate as an integer.
     */
    public int getY() {
        return y;
    }

    /**
     * Checks if the tile contains a purple alien.
     *
     * @return true if the tile has a purple alien, otherwise false.
     */
    public boolean isPurpleAlien() {
        return purpleAlien;
    }

    /**
     * Determines if a brown alien is present.
     *
     * @return true if there is a brown alien, otherwise false.
     */
    public boolean isBrownAlien() {
        return brownAlien;
    }

    /**
     * Retrieves the number of batteries associated with the event.
     *
     * @return the number of batteries
     */
    public int getBatteries() {
        return batteries;
    }

    /**
     * Retrieves the list of goods currently stored as cargo.
     *
     * @return an ArrayList containing the current cargo, represented as Goods objects.
     */
    public ArrayList<Goods> getCargo() {
        return cargo;
    }

    /**
     * Retrieves the rotation of the tile.
     *
     * @return the rotation value as an integer
     */
    public int getRotation() {
        return rotation;
    }

    /**
     * Retrieves the list of connectors associated with this object.
     *
     * @return an ArrayList containing the connectors. Each connector
     *         represents a specific type of connection mechanism for a tile.
     */
    public ArrayList<Connectors> getConnectors() {
        return connectors;
    }

    /**
     * Retrieves the name of the player associated with this event.
     *
     * @return the name of the player as a String
     */
    public String getPlayerName(){
        return playerName;
    }

    /**
     * Accepts a visitor object that processes this PlayerTileEvent instance.
     *
     * @param visitor the visitor instance handling the logic for this event
     */
    @Override
    public void accept(EventVisitor visitor) {
        visitor.visit(this);
    }

}
