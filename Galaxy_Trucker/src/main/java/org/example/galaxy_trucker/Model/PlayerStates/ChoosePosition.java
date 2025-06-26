package org.example.galaxy_trucker.Model.PlayerStates;

import org.example.galaxy_trucker.Commands.Command;
import org.example.galaxy_trucker.Commands.FinishBuildingCommand;
import org.example.galaxy_trucker.ClientServer.Messages.PhaseEvent;
import org.example.galaxy_trucker.Model.Boards.GameBoard;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.View.ClientModel.States.ChoosePositionClient;

import java.io.IOException;

/**
 * The ChoosePosition class is a specific state implementation extending the PlayerState abstract class.
 * It represents a game state where a player must choose their starting position. This state is responsible
 * for defining the behavior of a player during the position selection phase of the game.
 *
 * In this state:
 * - Specific commands, such as FinishBuildingCommand, are permitted.
 * - A default command to allow the player to choose their starting position is created.
 * - The state representation is converted into a corresponding client-side event.
 */
public class ChoosePosition extends PlayerState {

    /**
     * Determines whether the given FinishBuildingCommand is allowed in the current player state.
     *
     * @param command the FinishBuildingCommand to be checked for validity in the current state
     * @return true, indicating that the FinishBuildingCommand is allowed in this state
     */
    @Override
    public boolean allows(FinishBuildingCommand command){return true;}

    /**
     * Creates the default command for the current state, which enforces the player to choose
     * their starting position on the game board.
     *
     * @param gameId the unique identifier of the game.
     * @param player the player who is executing the command.
     * @return a Command instance that sets the player's starting position and marks them as ready.
     */
    @Override
    public Command createDefaultCommand(String gameId, Player player) {
        System.out.println("CHOOSEPOSITION");
        return new Command() {
            @Override
            public void execute(Player player) throws IOException {
                GameBoard board = player.getCommonBoard();
                board.SetStartingPosition(player);
                player.SetReady(true);

            }
        };
    }

    /**
     * Converts the current game state into a client-side representation.
     * This method encapsulates the current state, in this case, the "Choose Position" phase,
     * into a PhaseEvent object that is used to communicate the state to the client.
     *
     * @return a PhaseEvent object representing the current state as a client-side event.
     */
    @Override
    public PhaseEvent toClientState() {
        return new PhaseEvent(new ChoosePositionClient());
    }

}
