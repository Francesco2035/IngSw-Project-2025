package org.example.galaxy_trucker.ClientServer.Messages.PlayerBoardEvents;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.galaxy_trucker.ClientServer.Messages.Event;
import org.example.galaxy_trucker.ClientServer.Messages.EventVisitor;
import org.example.galaxy_trucker.Model.Connectors.Connectors;
import org.example.galaxy_trucker.Model.Goods.Goods;

import java.util.ArrayList;

/**
 * Represents an event associated with a tile in a game. The TileEvent class
 * contains information about the tile's position, its attributes, and related entities,
 * such as humans, aliens, cargo, and connectors.
 */
public class TileEvent implements Event {

    /**
     * Represents the unique identifier for a TileEvent.
     *
     * This field is used to uniquely distinguish each TileEvent instance
     * within the system. It is assigned during the construction of the
     * TileEvent object and serves as a key attribute for identifying and
     * managing specific tile-related events.
     */
    private int id;
    /**
     * Represents the number of humans associated with the tile.
     * This field holds the count of humans present on the tile
     * and is used as part of the TileEvent's state.
     */
    private int humans;
    /**
     * Represents the x-coordinate of the tile's position in a grid or space.
     * This field is an integer that specifies the horizontal location
     * of the tile within its respective context.
     */
    private int x;
    /**
     * Represents the y-coordinate of the tile's position within the TileEvent.
     * This variable holds the integer value indicating the vertical position
     * of the tile in a grid system or map.
     */
    private int y;
    /**
     * Represents the presence of a purple alien in a tile event.
     * This variable is used to determine whether a purple alien
     * is associated with the tile involved in the event.
     *
     * The value of this variable is {@code true} if a purple alien
     * is present on the tile, and {@code false} otherwise.
     */
    private boolean purpleAlien;
    /**
     * Indicates whether a brown alien is present on the tile associated with this event.
     * This field represents the presence of a brown alien, which may influence
     * the state or behavior of the tile in the event context.
     *
     * Possible values:
     * - {@code true} if a brown alien is present.
     * - {@code false} if no brown alien is present.
     */
    private boolean brownAlien;
    /**
     * Represents the number of batteries associated with the tile event.
     * This variable stores an integer value indicating the quantity of batteries
     * linked to the specific tile.
     */
    private int batteries;
    /**
     * Represents the cargo associated with a tile event.
     * The cargo is stored as a list of {@code Goods} objects, where each {@code Goods}
     * instance represents a specific type of resource or item (e.g., BLUE, GREEN,
     * YELLOW, RED).
     *
     * This list encapsulates all the cargo items available for the tile event and
     * may be used to track resources related to gameplay or system state.
     *
     * The cargo list is initialized during the creation of the {@code TileEvent}
     * and is accessible through the relevant getter methods. It may contain zero or
     * more items, depending on the specific context of the tile.
     */
    private ArrayList<Goods> cargo;
    /**
     * Represents the rotation of a tile in the context of a tile-based event.
     * Rotation is an integer value used to define the orientation of the tile within
     * the system, likely in degrees or specific unit increments (e.g., 0, 90, 180, 270).
     *
     * This variable is integral in determining how the tile is aligned or positioned
     * relative to other elements within the system. It may influence or be influenced
     * by the overall layout and connections of surrounding entities in the system.
     */
    private int rotation;
    /**
     * Represents the connectors associated with a tile in the game.
     * Connectors define possible connection points or attachment mechanisms
     * on the tile and are used to establish logical and structural relationships
     * between adjacent tiles.
     *
     * Each connector is represented as an instance of the {@code Connectors} interface,
     * which encapsulates the connection type (e.g., SINGLE, DOUBLE, UNIVERSAL, ENGINE, CANNON, NONE).
     * These objects also provide methods for validating connections and checking adjacency rules.
     *
     * This field stores a collection of connectors, allowing multiple connection types
     * to be associated with the same tile. The list may be empty if the tile has no connectors.
     */
    private ArrayList<Connectors> connectors;

    /**
     * Default constructor for the TileEvent class.
     *
     * This constructor initializes a new instance of the TileEvent class
     * without setting any attributes. The attributes can be set later
     * through the appropriate methods or by using the parameterized constructor.
     */
    public TileEvent() {

    }


    /**
     * Constructs a new TileEvent with the specified attributes and associated data.
     *
     * @param id           the unique identifier for the tile
     * @param x            the x-coordinate of the tile's position
     * @param y            the y-coordinate of the tile's position
     * @param cargo        a list of Goods objects representing the cargo associated with the tile
     * @param humans       the number of humans associated with the tile
     * @param purpleAlien  a boolean indicating whether a purple alien is present on the tile
     * @param brownAlien   a boolean indicating whether a brown alien is present on the tile
     * @param batteries    the number of batteries associated with the tile
     * @param rotation     an integer representing the rotation of the tile
     * @param connectors   a list of Connectors objects representing the connectors of the tile
     */
    @JsonCreator
    public TileEvent(
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
    }

    /**
     * Returns a message associated with this event.
     * This method currently returns an empty string, representing
     * no specific message is assigned to the event.
     *
     * @return an empty string as the message associated with the event
     */
    @Override
    public String message() {
        return "";
    }

    /**
     * Retrieves the unique identifier associated with this tile event.
     *
     * @return the unique identifier of the tile event
     */
    public int getId() {
        return id;
    }

    /**
     * Retrieves the number of humans associated with the tile event.
     *
     * @return the number of humans, represented as an integer.
     */
    public int getHumans() {
        return humans;
    }

    /**
     * Retrieves the x-coordinate associated with this TileEvent.
     *
     * @return the x-coordinate as an integer.
     */
    public int getX() {
        return x;
    }

    /**
     * Retrieves the y-coordinate of the tile associated with the event.
     *
     * @return the integer value representing the y-coordinate of the tile.
     */
    public int getY() {
        return y;
    }

    /**
     * Determines if the tile event involves a purple alien.
     *
     * @return {@code true} if the tile is associated with a purple alien,
     *         {@code false} otherwise.
     */
    public boolean isPurpleAlien() {
        return purpleAlien;
    }

    /**
     * Checks whether the tile event is associated with a brown alien.
     *
     * @return true if the tile event is related to a brown alien, false otherwise
     */
    public boolean isBrownAlien() {
        return brownAlien;
    }

    /**
     * Retrieves the number of batteries associated with the tile event.
     *
     * @return the number of batteries as an integer.
     */
    public int getBatteries() {
        return batteries;
    }

    /**
     * Retrieves the list of cargo items associated with the tile event.
     *
     * @return an ArrayList of Goods objects, representing the cargo associated
     *         with the tile event. Each Goods object corresponds to a specific
     *         type of item. The list may be empty if no cargo is present.
     */
    public ArrayList<Goods> getCargo() {
        return cargo;
    }

    /**
     * Retrieves the rotation value associated with the tile event.
     *
     * @return an integer representing the rotation of the tile.
     */
    public int getRotation() {
        return rotation;
    }

    /**
     * Retrieves the list of connectors associated with the tile event.
     *
     * @return an ArrayList of Connectors objects, representing the connectors
     *         encapsulated within the tile event. Each Connector object defines
     *         a specific type of connection or attachment point. The list may
     *         be empty if no connectors are associated with the tile.
     */
    public ArrayList<Connectors> getConnectors() {
        return connectors;
    }

    /**
     * Accepts a visitor that performs operations on this TileEvent instance.
     *
     * @param visitor the visitor instance that processes this event
     */
    @Override
    public void accept(EventVisitor visitor) {
        visitor.visit(this);
    }


}
