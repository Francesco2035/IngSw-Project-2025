package org.example.galaxy_trucker.Model.PlayerStates;

import org.example.galaxy_trucker.Commands.Command;
import org.example.galaxy_trucker.ClientServer.Messages.PhaseEvent;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.View.ClientModel.States.WaitingClient;

/**
 * Represents a state in which a player is waiting and has no immediate action to perform.
 * This state implements the necessary methods from the PlayerState class to adjust the player's state
 * and behavior accordingly.
 *
 * When in the waiting state, the player is marked as having acted, and a default no-op command is created.
 * The state also determines the necessary client-side behavior through a corresponding client event.
 */
public class Waiting extends PlayerState{

 /**
  * Creates the default command for the given player in the specified game.
  * Marks the player as having acted in the current game state.
  *
  * @param gameId the identifier of the game in which the player is participating
  * @param player the player for whom the default command is being created
  * @return the default command for the player, or null if no specific command is required
  */
 @Override
    public Command createDefaultCommand(String gameId,Player player) {
        player.SetHasActed(true);
        return null;
    }

    /**
     * Marks the player as having acted in the current state.
     *
     * @param player The player whose state is being modified to indicate that they have acted.
     */
    @Override
    public void shouldAct(Player player) {
        player.SetHasActed(true);
    }

    /**
     * Transitions the player's state to a client-specific representation of the waiting state.
     *
     * This method is an implementation of the `toClientState` behavior, transforming the current
     * player state into a client-facing phase event. It returns a `PhaseEvent` object with a
     * `WaitingClient` instance, representing the "waiting" phase of the game.
     *
     * @return a PhaseEvent instance containing a WaitingClient object, which encapsulates
     *         the player's behavior and available actions during the waiting phase.
     */
    @Override
    public PhaseEvent toClientState() {
        return new PhaseEvent(new WaitingClient());
    }
}
