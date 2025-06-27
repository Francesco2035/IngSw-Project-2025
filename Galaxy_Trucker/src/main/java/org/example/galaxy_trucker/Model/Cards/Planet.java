package org.example.galaxy_trucker.Model.Cards;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.galaxy_trucker.Model.Goods.Goods;
import org.example.galaxy_trucker.Model.Player;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * The Planet class represents a planet with associated goods and an occupancy status
 * indicating whether a player has chosen the planet.
 * This class is serializable and utilizes JSON annotations for data exchange.
 */
// list of the cargo contained in a planet and the bool signaling if a player chose it already
public class Planet implements Serializable {
    /**
     * Represents the player occupying the planet. If the value is null,
     * the planet is unoccupied. Otherwise, it indicates the player who
     * has chosen the planet.
     */
    private Player Occupied;
    /**
     * A collection of Goods associated with a planet.
     * This list represents various items of trade or resources
     * that can exist on the planet. Each item in the list conforms
     * to the Goods interface, ensuring a standard structure for all
     * goods, regardless of their specific type.
     *
     * The property is annotated with @JsonProperty to support seamless
     * serialization and deserialization in JSON data exchange.
     */
    @JsonProperty("Goods")
    ArrayList<Goods> Goods;

    /**
     * Constructs a Planet with the specified list of goods.
     * Initializes the planet as unoccupied.
     *
     * @param Goods the list of goods available on this planet
     */
    Planet(ArrayList<Goods> Goods) {
        Goods = Goods;
        this.Occupied=null;
    }


    /**
     * Retrieves the list of goods associated with the planet.
     * The goods represent resources or items available on the planet,
     * which conform to the {@code Goods} interface.
     *
     * @return an {@code ArrayList} containing all the goods present on the planet
     */
    public ArrayList<Goods> getGoods() {
        return Goods;
    }

    /**
     * Checks whether the planet is currently occupied by a player.
     *
     * @return true if the planet is occupied by a player, false otherwise.
     */
    public boolean isOccupied() {
        if(Occupied != null){
            return true;
        }
        else{
            return false;
        }
    }




    // json file


    /**
     * Constructs a default instance of the Planet class.
     * Initializes the planet with no goods and as unoccupied.
     */
    public Planet() {}

    /**
     * Updates the list of goods associated with the planet.
     * The goods represent resources or items available, which conform
     * to the {@code Goods} interface.
     *
     * @param goods the new list of goods to be associated with the planet
     */
    public void setGoods(ArrayList<Goods> goods) {
        Goods = goods;
    }

    /**
     * Sets the player occupying the planet. If a player is specified,
     * the planet is marked as occupied by that player. Passing null indicates
     * that the planet is no longer occupied.
     *
     * @param occupied the player to occupy the planet, or null to mark it as unoccupied
     */
    public void setOccupied(Player occupied) {
        Occupied = occupied;
    }

    /**
     * Retrieves the player occupying the planet.
     * If the planet is unoccupied, this method returns null.
     *
     * @return the {@code Player} object representing the player occupying the planet, or null if the planet is unoccupied
     */
    public Player getOccupied() {
        return Occupied;
    }


}
