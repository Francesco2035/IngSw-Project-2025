package org.example.galaxy_trucker.Commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.galaxy_trucker.Model.Cards.Card;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.PlayerState;

import java.io.Serializable;

/**
 * Represents the DefendFromLargeCommand, a specific type of command that allows
 * players to defend against large scale threats in the game. This command processes
 * defensive actions tied to specific components such as the plasma drill and
 * battery compartment of the player's resources.
 *
 * This class extends the abstract {@code Command} class and implements functionality
 * for checking its allowance in a player's current state and executing the defined behavior.
 *
 * DefendFromLargeCommand is designed for scenarios where threats to the player's progress
 * or resources must be mitigated, utilizing resources specified via {@code IntegerPair}.
 */
public class DefendFromLargeCommand extends Command implements Serializable {

    @JsonProperty("plasmaDrill")
    private IntegerPair plasmaDrill;
    @JsonProperty("batteryComp")
    private IntegerPair batteryComp;

    public DefendFromLargeCommand() {}

    /**
     * Constructs a DefendFromLargeCommand object with the specified parameters.
     *
     * @param plasmaDrill an IntegerPair representing the coordinates or properties
     *                    of the plasma drill resource used in the command.
     * @param batteryComp an IntegerPair representing the coordinates or properties
     *                    of the battery component resource used in the command.
     * @param gameId      the unique identifier of the game.
     * @param playerId    the unique identifier of the player executing this command.
     * @param lv          the level associated with the command.
     * @param title       the title or descriptive name of the command.
     * @param token       the token used for authentication or validation.
     */
    public DefendFromLargeCommand(IntegerPair plasmaDrill, IntegerPair batteryComp,String gameId, String playerId, int lv, String title, String token) {
        super(gameId, playerId, lv, title, token,-1);
        this.plasmaDrill = plasmaDrill;
        this.batteryComp = batteryComp;
    }


    /**
     * Determines whether this command is allowed in the provided {@code PlayerState}.
     *
     * @param playerState the current state of the player, used to check if the command
     *                    is permissible in the given context.
     * @return {@code true} if the command is allowed in the given {@code PlayerState},
     *         otherwise {@code false}.
     */
    @Override
    public boolean allowedIn(PlayerState playerState) {
        return playerState.allows(this);
    }

    /**
     * Executes the command logic specific to defending against large-scale threats.
     * This method utilizes the player's current card to perform a defense action
     * using the specified plasma drill and battery compartment components.
     *
     * @param player The player that is executing the command. The behavior is
     *               applied to this player's current card and resources.
     */
    @Override
    public void execute(Player player) {
        try {
            player.getCurrentCard().DefendFromLarge(plasmaDrill, batteryComp,player);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
