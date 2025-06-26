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

    private int id;
    private int humans;
    private int x;
    private int y;
    private boolean purpleAlien;
    private boolean brownAlien;
    private int batteries;
    private ArrayList<Goods> cargo;
    private int rotation;
    private ArrayList<Connectors> connectors;

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
