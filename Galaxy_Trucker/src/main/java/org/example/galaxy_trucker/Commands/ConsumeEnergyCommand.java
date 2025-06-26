package org.example.galaxy_trucker.Commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.PlayerState;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Represents a command to consume energy within the context of the game.
 * This command is responsible for performing actions related to energy consumption
 * based on specified coordinates.
 *
 * The command is executed on a specific player, interacting with their current card
 * to consume energy as defined by the provided coordinates. Additionally, this
 * command includes validation to ensure that it is allowed in the player's current state.
 *
 * It is a subclass of the {@code Command} base class and benefits
 * from its shared properties and functional structure.
 */
public class ConsumeEnergyCommand extends Command implements Serializable {

    /**
     * Represents a list of coordinate pairs used to specify locations where energy consumption
     * or other related actions are executed within the game's context.
     *
     * Each coordinate is represented as an {@link IntegerPair}, defining a pair of integers
     * that denote a specific position or target within the game.
     *
     * The coordinate list is utilized by commands such as {@code ConsumeEnergyCommand} to
     * perform game mechanics based on user-defined inputs, ensuring precise targeting and execution.
     */
    @JsonProperty("coordinate")
    private ArrayList<IntegerPair> coordinate;

    /**
     * Default constructor for the ConsumeEnergyCommand class.
     *
     * Initializes a new instance of the ConsumeEnergyCommand with no parameters.
     * Typically used when constructing a command without predefined attributes.
     */
    public ConsumeEnergyCommand() {}

    /**
     * Constructs a ConsumeEnergyCommand object with specified parameters.
     * This command represents an action for consuming energy based on given coordinates
     * within the game's context.
     *
     * @param coordinate the list of coordinate pairs specifying where energy should be consumed
     * @param gameId     the unique identifier of the game
     * @param playerId   the unique identifier of the player issuing the command
     * @param lv         the level or priority associated with the command
     * @param title      the title or name associated with the command
     * @param token      the token used for authentication or verification
     */
    public ConsumeEnergyCommand(ArrayList<IntegerPair> coordinate,String gameId, String playerId, int lv, String title, String token) {
        super(gameId, playerId, lv, title, token,-1);
        this.coordinate = coordinate;
    }

    /**
     * Executes the command to consume energy for the specified player.
     * This method interacts with the player's current card and applies the energy
     * consumption process based on the provided coordinates.
     *
     * @param player the player on whom the command is being executed.
     *               The player's current card is used to apply the energy consumption.
     */
    @Override
    public void execute(Player player) {
        try{
            player.getCurrentCard().consumeEnergy(coordinate);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }


    /**
     * Determines if this command is allowed in the current player's state.
     *
     * @param playerState the current state of the player
     * @return true if the player state allows this command, false otherwise
     */
    @Override
    public boolean allowedIn(PlayerState playerState) {
        return playerState.allows(this);
    }
}
