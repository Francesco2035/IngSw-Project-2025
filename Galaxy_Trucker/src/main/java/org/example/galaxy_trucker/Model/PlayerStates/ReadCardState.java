package org.example.galaxy_trucker.Model.PlayerStates;

import org.example.galaxy_trucker.Commands.Command;
import org.example.galaxy_trucker.ClientServer.Messages.PhaseEvent;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.View.ClientModel.States.ReadCardClient;

/**
 * Represents the state of a player where they are in the "read card" phase of the game.
 * This class is a concrete implementation of the {@link PlayerState} abstract class
 * and provides behavior specific to when a player is interacting with a game phase
 * where they read or interact with cards.
 */
public class ReadCardState extends PlayerState {


    /**
     * Creates a default command for the current game phase and updates the player's state
     * to indicate they have acted.
     *
     * @param gameId the unique identifier for the game in which the command is being created
     * @param player the player for whom the command is being created
     * @return a {@code Command} object representing the default action for the player, or
     *         {@code null} if there is no applicable default action
     */
    @Override
    public Command createDefaultCommand(String gameId,Player player) {
        player.SetHasActed(true);
        return null;
    }

    /**
     * Sets the player to a state where they have acted during their turn.
     *
     * @param player the player whose state is updated to indicate they have acted
     */
    @Override
    public void shouldAct(Player player) {
        player.SetHasActed(true);
    }

    /**
     * Converts the current state to a client-readable representation of the phase event.
     *
     * This method creates and returns a PhaseEvent object that encapsulates the
     * current game phase as represented by a ReadCardClient instance. It is used
     * to represent the "read card" phase of the game in a format suitable for
     * client-side consumption and interaction.
     *
     * @return a PhaseEvent object containing a ReadCardClient instance, which
     *         represents the player's current state in the "read card" phase of the game.
     */
    @Override
    public PhaseEvent toClientState() {
        return new PhaseEvent(new ReadCardClient());
    }
}
