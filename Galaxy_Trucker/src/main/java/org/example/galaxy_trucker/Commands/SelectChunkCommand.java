package org.example.galaxy_trucker.Commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.PlayerState;

import java.io.Serializable;

/**
 * Represents a command to select a specific chunk in a game. This command is used
 * to modify the player's game state based on the chosen chunk of the game board.
 *
 * This class extends the base {@code Command} class and implements {@code Serializable}
 * for enabling serialization and deserialization across the system.
 */
public class SelectChunkCommand extends Command implements Serializable {

    @JsonProperty("chunk")
    IntegerPair chunk;

    public SelectChunkCommand(){}

    /**
     * Constructs a SelectChunkCommand with the specified parameters.
     *
     * @param chunk    an {@code IntegerPair} representing the coordinates of the selected chunk
     * @param gameId   the unique identifier of the game
     * @param playerId the unique identifier of the player
     * @param lv       the level associated with the command
     * @param title    the title or name of the command
     * @param token    the token used for authentication or validation
     */
    public SelectChunkCommand(IntegerPair chunk,String gameId, String playerId, int lv, String title, String token) {
        super(gameId, playerId, lv, title, token,-1);
        this.chunk = chunk;
    }

    /**
     * Determines whether the current command is allowed in the provided player state.
     * This method delegates the decision to the provided {@code PlayerState} instance.
     *
     * @param playerState the current {@code PlayerState} of the player, which determines
     *                    if the command is valid in the given context
     * @return {@code true} if the command is allowed in the given {@code PlayerState},
     *         {@code false} otherwise
     */
    @Override
    public boolean allowedIn(PlayerState playerState) {
        return playerState.allows(this);
    }

    /**
     * Executes the {@code SelectChunkCommand} for the given player. It updates the player's
     * board state by modifying it based on the specified chunk selection and triggers
     * the continuation of the current card's operation.
     *
     * @param player the player executing the command, whose board state will be modified
     */
    @Override
    public void execute(Player player) {
        PlayerBoard playerBoard = player.getmyPlayerBoard();
        playerBoard.modifyPlayerBoard(playerBoard.choosePlayerBoard(chunk));

        try {
            player.getCurrentCard().keepGoing();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
