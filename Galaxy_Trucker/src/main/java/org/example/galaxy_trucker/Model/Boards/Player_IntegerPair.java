package org.example.galaxy_trucker.Model.Boards;

import org.example.galaxy_trucker.Model.Player;

/**
 * The Player_IntegerPair class is a utility class used to represent a pair consisting
 * of a Player object and an Integer value, such as a score associated with the player.
 * This class provides methods to retrieve and modify the Player and the associated Integer value.
 */
public class Player_IntegerPair {
    /**
     * Represents a player in the Player_IntegerPair utility class.
     * This variable holds a reference to a Player instance which forms one part
     * of the key-value pair encapsulated within the Player_IntegerPair class.
     */
    Player player;
    /**
     * Represents the score associated with a player.
     * This variable is used to store an Integer value indicating the player's score
     * in the context of the Player_IntegerPair class.
     */
    Integer score;

    /**
     * Constructs a Player_IntegerPair object with the specified player and associated integer value.
     *
     * @param player the Player object to be paired
     * @param number the Integer value associated with the Player object, such as a score
     */
    Player_IntegerPair(Player player, Integer number) {
        this.player = player;
        this.score = number;
    }

    /**
     * Retrieves the Player object from the Player_IntegerPair instance.
     *
     * @return the Player object associated with this Player_IntegerPair.
     */
    public Player getKey(){return player;}

    /**
     * Retrieves the Integer score associated with the Player in this Player_IntegerPair.
     *
     * @return the score as an Integer associated with the Player.
     */
    public Integer getValue(){return score;}

    /**
     * Updates the score associated with the player in the Player_IntegerPair.
     *
     * @param value the new Integer value to set as the score for the player
     */
    public void setValue(Integer value){this.score=value;}

}
