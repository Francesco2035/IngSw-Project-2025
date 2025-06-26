package org.example.galaxy_trucker.Model.PlayerStates;

import org.example.galaxy_trucker.Commands.ChoosingPlanetsCommand;
import org.example.galaxy_trucker.Commands.Command;
import org.example.galaxy_trucker.ClientServer.Messages.PhaseEvent;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.View.ClientModel.States.ChoosingPlanetClient;

/**
 * The ChoosingPlanet class represents the player state where a player is in the process
 * of selecting a planet. It extends the PlayerState class and overrides relevant methods
 * to define the specific behavior and rules associated with this state.
 */
public class ChoosingPlanet extends PlayerState{
/**
 * Determines whether the provided ChoosingPlanetsCommand is allowed in
 * the current player state.
 *
 * @param command the ChoosingPlanetsCommand to be checked for allowance
 * @return true if the command is allowed in the current state, otherwise false
 */
    @Override
    public boolean allows(ChoosingPlanetsCommand command) {
        return true;
    }

    /**
     * Creates a default command for the "Choosing Planets" phase of the game. This command
     * represents the initial state or action associated with a player during this phase.
     *
     * @param gameId the unique identifier of the game session
     * @param player the player for whom the default command is being created
     * @return a new instance of the ChoosingPlanetsCommand with default parameters
     */
    @Override
    public Command createDefaultCommand(String gameId, Player player) {
        int lv= player.getCommonBoard().getLevel();
        return new ChoosingPlanetsCommand(-1,gameId,player.GetID(),lv,"ChoosingPlanetsCommand","placeholder"); /// devo mettere il token
    }

    /**
     * Converts the current player state to a client-specific state representation.
     * This method encapsulates the current state in a PhaseEvent object,
     * which wraps a ChoosingPlanetClient instance to represent the "Choosing Planet" phase.
     *
     * @return a PhaseEvent object containing a ChoosingPlanetClient instance, which represents
     *         the client's state during the "Choosing Planet" phase of the game.
     */
    @Override
    public PhaseEvent toClientState() {
        return new PhaseEvent(new ChoosingPlanetClient());
    }
}

