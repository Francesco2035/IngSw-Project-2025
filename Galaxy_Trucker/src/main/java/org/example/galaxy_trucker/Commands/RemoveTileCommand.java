package org.example.galaxy_trucker.Commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.PlayerState;

import java.io.Serializable;

/**
 * Represents a command to remove a tile from the player board. This command
 * specifies the x and y coordinates of the tile to be removed and uses the specified
 * game and player context to execute its operation.
 *
 * This class is a concrete implementation of the abstract {@code Command} class.
 * It inputs specific tile coordinates (x, y) to identify the tile to be removed.
 *
 * Fields:
 * - `x`: The x-coordinate of the tile to be removed.
 * - `y`: The y-coordinate of the tile to be removed.
 * - `commandType`: A string representing the type of the command, used for serialization purposes.
 *
 * Constructor:
 * - Accepts coordinates (x, y), game information, and player context necessary to perform the operation.
 * - Calls the parent `Command` constructor to initialize shared fields.
 *
 * Methods:
 * - `allowedIn(PlayerState playerState)`: Checks whether the command is allowed in the provided player state.
 * - `execute(Player player)`: Executes the removal of a tile on the player's board at the specified x-y coordinates.
 */
public class RemoveTileCommand extends Command implements Serializable {

    /**
     * Represents the type of command being executed in the context of a game.
     * This variable is used to specify the command type for the associated action,
     * in this case, identifying it as a "RemoveTileCommand".
     *
     * It is a constant, final field and is serialized using Jackson annotations
     * to ensure it can be included in the serialized representation of the command.
     */
    @JsonProperty("commandType")
    private final String commandType = "RemoveTileCommand";


    /**
     * The x-coordinate of the tile to be removed.
     *
     * This field is a required parameter representing the horizontal position
     * of a tile in a game grid. It is used as part of a command to remove
     * a specific tile from the board. This value is serialized or
     * deserialized using the Jackson annotation.
     */
    //IntegerPair tile;
    @JsonProperty("x")
    int x;
    /**
     * Represents the y-coordinate of the tile to be removed in the game board.
     * This field is used to specify the vertical position of the tile that
     * will be targeted by the RemoveTileCommand during execution.
     *
     * The value of this field is set when creating an instance of the
     * RemoveTileCommand class and is utilized by the execute method to
     * locate and remove the corresponding tile on the player's board.
     */
    @JsonProperty("y")
    int y;

    /**
     * Represents a command to remove a tile in the game. The RemoveTileCommand class
     * is a part of the game's command system, allowing the removal of a tile from
     * a player's board at specific coordinates.
     *
     * This command includes logic to check if it is allowed in a player's current state
     * and implements the actual removal action when executed.
     */
    public RemoveTileCommand(){}

    /**
     * Constructs a new RemoveTileCommand with the specified parameters.
     *
     * @param x         the x-coordinate of the tile to be removed
     * @param y         the y-coordinate of the tile to be removed
     * @param gameId    the unique identifier of the game
     * @param playerId  the unique identifier of the player issuing the command
     * @param lv        the level associated with the command
     * @param title     the title or name of the command
     * @param token     the token used for authentication or validation
     */
    public RemoveTileCommand(int x, int y,String gameId, String playerId, int lv, String title, String token) {
        super(gameId, playerId, lv, title, token,-1);
        this.x = x;
        this.y = y;
        //this.tile = new IntegerPair(x,y);
    }

    /**
     * Checks whether this command is allowed in the specified player state.
     *
     * @param playerState the current state of the player in which to check if the command is allowed
     * @return {@code true} if the command is permitted in the provided player state, {@code false} otherwise
     */
    @Override
    public boolean allowedIn(PlayerState playerState) {
        return playerState.allows(this);
    }

    /**
     * Executes the command to remove a tile from the player's board at the specified
     * x and y coordinates. This method interacts with the player's board to remove
     * the designated tile.
     *
     * @param player the player whose board the tile will be removed from
     */
    @Override
    public void execute(Player player) {
        player.getmyPlayerBoard().removeTile(x, y);
    }
}
