package org.example.galaxy_trucker.Model.PlayerStates;

import org.example.galaxy_trucker.Commands.DefendFromLargeCommand;
import org.example.galaxy_trucker.ClientServer.Messages.PhaseEvent;
import org.example.galaxy_trucker.Model.Boards.Actions.UseEnergyAction;
import org.example.galaxy_trucker.Commands.Command;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.View.ClientModel.States.DefendingFromLargeClient;

/**
 * Represents the "Defending From Large" state of a player in the game.
 * This state allows players to perform specific defensive actions against large-scale threats
 * using appropriate commands, such as the {@code DefendFromLargeCommand} and the
 * energy-related actions*/
public class DefendingFromLarge extends PlayerState{
/**
 * Checks whether the given {@code DefendFromLargeCommand} is allowed in this state.
 *
 * @param command the {@code DefendFromLargeCommand} to be checked against the current state.
 * @return {@code true} if the command is allowed in the current state, otherwise {@code false}.
 */

    @Override
    public boolean allows(DefendFromLargeCommand command) {
        return true;
    }


    /**
     * Determines if a given UseEnergyAction is allowed in the current player state.
     *
     * @param action the UseEnergyAction to be validated
     * @return true if the action is allowed in this state, false otherwise
     */
    @Override
    public boolean allows(UseEnergyAction action) {
        return true;
    }

    /**
     * Creates a default command for the "Defending From Large" player state.
     * The command represents a defensive action to counter large-scale threats
     * within the game environment.
     *
     * @param gameId the unique identifier for the current game instance
     * @param player the player for whom the default command is being created
     * @return a {@code DefendFromLargeCommand} representing the default defensive action
     */
    @Override
    public Command createDefaultCommand(String gameId, Player player) {
        int lv = player.getCommonBoard().getLevel();
        return new DefendFromLargeCommand(null,null,gameId,player.GetID(),lv,"DefendingFromLargeCommand","placeholder");
    }

    /**
     * Converts the current player state to a corresponding {@code PhaseEvent} object,
     * representing the "Defending From Large" state in the game.
     * This is used to encapsulate the client's view of this specific game phase.
     *
     * @return a {@code PhaseEvent} object containing a {@code DefendingFromLargeClient}
     *         instance, which represents the player's state during the "Defending From Large" phase.
     */
    @Override
    public PhaseEvent toClientState() {
        return new PhaseEvent(new DefendingFromLargeClient());
    }
}
