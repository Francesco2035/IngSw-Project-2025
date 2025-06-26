package org.example.galaxy_trucker.Commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.galaxy_trucker.Model.Cards.Card;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.PlayerState;

import java.io.Serializable;

/**
 * Represents a command to defend a player from small-scale threats in the game.
 * This class extends the abstract Command class and implements the specific
 * behavior for executing a defense action targeting small-scale attacks.
 *
 * The `DefendFromSmallCommand` is associated with a `batteryComp` parameter
 * that defines the battery-specific configuration used during the defense.
 * The command is designed to interact with the player and modify the player's
 * status based on small-scale defensive actions.
 *
 * This class also implements serialization and uses Jackson annotations
 * to enable proper serialization and deserialization of the command type.
 */
public class DefendFromSmallCommand extends Command implements Serializable {

    @JsonProperty("commandType")
    private final String commandType = "DefendFromSmallCommand";

    @JsonProperty("batteryComp")
    private IntegerPair batteryComp;

    public DefendFromSmallCommand() {}

    /**
     * Constructs a new DefendFromSmallCommand object with the specified parameters.
     *
     * @param batteryComp the battery configuration represented as an IntegerPair
     * @param gameId      the unique identifier of the game
     * @param playerId    the unique identifier of the player
     * @param lv          the level associated with the command
     * @param title       the title or name of the command
     * @param token       the token used for authentication or validation
     */
    public DefendFromSmallCommand(IntegerPair batteryComp,String gameId, String playerId, int lv, String title, String token) {
        super(gameId, playerId, lv, title, token,-1);
        this.batteryComp = batteryComp;
    }

    /**
     * Determines whether the `DefendFromSmallCommand` is allowed to execute
     * in the given player state.
     *
     * @param playerState the current state of the player in the game, which dictates
     *                    whether the command can be executed.
     * @return true if the command is permitted in the specified player state, false otherwise.
     */
    @Override
    public boolean allowedIn(PlayerState playerState) {
        return playerState.allows(this);
    }

    /**
     * Executes the defense command to protect the given player from small-scale threats.
     * This method interacts with the player's current card to perform the defense
     * operation based on the battery configuration.
     *
     * @param player the player who is executing the defense against small-scale threats
     */
    @Override
    public void execute(Player player) {
        try {
            player.getCurrentCard().DefendFromSmall(batteryComp,player);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
