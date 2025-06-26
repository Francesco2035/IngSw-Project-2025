package org.example.galaxy_trucker.Commands;

import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.PlayerState;

import java.io.IOException;

/**
 * Represents a command used to handle player quit functionality within the game.
 * The QuitCommand is a specific implementation of the {@link Command} class,
 * designed to process the player's action of quitting the game.
 */
public class QuitCommand extends Command{

    public QuitCommand(){}


    /**
     * Constructs a QuitCommand object with specified parameters.
     *
     * @param gameId   the unique identifier of the game
     * @param playerId the unique identifier of the player who is quitting
     * @param lv       the level associated with the command
     * @param title    the title of the command
     * @param token    the token for authentication or validation
     */
    public QuitCommand(String gameId, String playerId, int lv, String title, String token) {
        super(gameId, playerId, lv, title, token, -1);
    }


    /**
     * Executes the player quit command, handling the player's action of quitting the game.
     *
     * @param player the player instance performing the quit action
     * @throws IOException if an input or output error occurs during execution
     */
    @Override
    public void execute(Player player) throws IOException {

    }

    /**
     * Determines whether the command is allowed to be executed in the given player state.
     *
     * @param playerState the current state of the player
     * @return true if the command is allowed in the specified player state, false otherwise
     */
    @Override
    public boolean allowedIn(PlayerState playerState) {
        return playerState.allows(this);
    }
}
