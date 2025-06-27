package org.example.galaxy_trucker.Model.PlayerStates;

import org.example.galaxy_trucker.Commands.Command;
import org.example.galaxy_trucker.Commands.ReadyCommand;
import org.example.galaxy_trucker.Commands.RemoveTileCommand;
import org.example.galaxy_trucker.ClientServer.Messages.PhaseEvent;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.View.ClientModel.States.CheckValidityClient;

import java.io.IOException;

/**
 * The CheckValidity class represents a specific state in the player's lifecycle,
 * inheriting from the abstract PlayerState class. This state is responsible for validating
 * the player's game board and ensuring readiness for the next phase.
 *
 * The main responsibility of this class includes verifying the validity of the player's
 * board and allowing specific commands permissible in this state. When transitioning to this
 * state, a default command is executed to ensure that any invalid tiles are removed, and
 * the player is marked as ready.
 *
 * Methods:
 * - allows(RemoveTileCommand): Permits the removal of tiles.
 * - allows(ReadyCommand): Permits readiness for the next phase.
 * - createDefaultCommand(String gameId, Player player): Generates a default command
 *   that validates the player's board and ensures readiness.
 * - toClientState(): Converts the server-side state to a client-side representation
 *   using the CheckValidityClient class.
 *
 * This state is crucial for ensuring the integrity of the player's board and enforcing the
 * rules of the game during transitions between phases.
 */
public class CheckValidity extends PlayerState{


    /**
     * Determines whether the given RemoveTileCommand is allowed in the current player state.
     *
     * @param command the RemoveTileCommand to check for permission
     * @return {@code true} if the command is allowed, otherwise {@code false}
     */
    @Override
    public boolean allows(RemoveTileCommand command) {
        return true;
    }

    /**
     * Determines whether the specified {@code ReadyCommand} is allowed in the current state.
     *
     * @param command the {@code ReadyCommand} to be evaluated for permission in the current state
     * @return {@code true} if the {@code ReadyCommand} is allowed in this state, {@code false} otherwise
     */
    @Override
    public boolean allows(ReadyCommand command) {
        return true;
    }

    /**
     * Creates a default command for the specified player and game.
     * The generated command validates the player's board by removing invalid tiles
     * iteratively until the board becomes valid, and then sets the player as ready.
     *
     * @param gameId the unique identifier for the game
     * @param player the player for whom the default command is created
     * @return a Command object that performs the default actions for the CheckValidity state
     */
    @Override
    public Command createDefaultCommand(String gameId,Player player) {

        return new Command() {
            @Override
            public void execute(Player player) throws IOException {
                while (!player.getmyPlayerBoard().checkValidity()){
                    for (int i = 0; i < 10 ; i ++){
                        for (int j = 0; j < 10 ; j ++){
                            if (player.getmyPlayerBoard().getToRemovePB()[i][j] == -2){

                                player.getmyPlayerBoard().removeTile(i, j);
                            }
                        }
                    }
                }
                player.SetReady(true);
            }
        };
    }

    /**
     * Converts the current server-side state to its corresponding client-side representation.
     * This method is responsible for creating and returning a {@code PhaseEvent} object,
     * encapsulating the specific client-side state represented by {@code CheckValidityClient}.
     *
     * @return a {@code PhaseEvent} object that encapsulates the {@code CheckValidityClient}
     *         state, which represents the "Check Validity" phase in the game for the client-side.
     */
    @Override
    public PhaseEvent toClientState() {
        return new PhaseEvent(new CheckValidityClient());
    }

}
