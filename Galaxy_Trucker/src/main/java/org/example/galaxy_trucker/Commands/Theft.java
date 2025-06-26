package org.example.galaxy_trucker.Commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.PlayerState;

import java.io.Serializable;

/**
 * Represents the Theft command in the game, allowing a player to steal or lose cargo based
 * on the provided details. This class extends the Command base class and is part of the
 * game's command system.
 *
 * The Theft command specifically handles scenarios where cargo is affected due to theft,
 * targeting a specific position and defined by an integer pair.
 *
 * This class utilizes Jackson annotations to enable serialization and deserialization.
 */
public class Theft extends Command implements Serializable {

    /**
     * Represents a pair of integer values that define a specific configuration
     * or parameter related to the Theft command in the game. The pair is used
     * to denote associated numeric values for cargo manipulation during theft
     * actions.
     *
     * This field is serialized and deserialized using the Jackson library
     * and mapped to the JSON property "pair".
     */
    @JsonProperty("pair")
    IntegerPair pair;
    /**
     * Represents the specific position targeted in a theft action within the game.
     * The position is identified as an integer value, which corresponds to a location
     * or index impacted by the command.
     *
     * This variable is annotated with Jackson's `@JsonProperty` to enable serialization
     * and deserialization within the game's command system.
     */
    @JsonProperty("position")
    int position;


    /**
     * Default constructor for the Theft command.
     * This constructor initializes a new instance of the Theft class with no parameters.
     * Primarily used for instantiation where detailed parameters are not immediately available
     * or needed, including scenarios involving deserialization.
     */
    public Theft(){}

    /**
     * Constructs a Theft command, representing an in-game theft action where a player can
     * target a specific position and defined pair for cargo manipulation.
     *
     * @param position the position targeted by the theft action
     * @param pair     an IntegerPair defining two numeric values related to the theft action
     * @param gameId   the unique identifier of the game
     * @param playerId the unique identifier of the player performing the action
     * @param lv       the level associated with the command
     * @param title    the title or name of the command
     * @param token    the authentication or validation token for the command
     */
    public Theft(int position,IntegerPair pair,String gameId, String playerId, int lv, String title, String token) {
        super(gameId, playerId, lv, title, token,-1);
        this.pair = pair;
        this.position = position;
    }

    /**
     * Determines whether the current Theft command is allowed in the given player state.
     * This method delegates the decision to the `allows` method of the provided
     * playerState instance.
     *
     * @param playerState the current state of the player in the game, which dictates
     *                    if the command can be executed.
     * @return true if the Theft command is allowed in the given player state, false otherwise.
     */
    @Override
    public boolean allowedIn(PlayerState playerState) {
        return playerState.allows(this);
    }

    /**
     * Executes the Theft command, causing the specified player to lose cargo
     * based on the provided pair and position attributes. This method accesses
     * the player's current card and applies the cargo loss logic.
     *
     * @param player the player that the command is being executed on, whose
     *               cargo will be affected.
     */
    @Override
    public void execute(Player player) {
        try {
            player.getCurrentCard().loseCargo(pair,position);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
