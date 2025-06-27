package org.example.galaxy_trucker.Model.Boards.Actions;

import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.PlayerStates.PlayerState;
import org.example.galaxy_trucker.Model.Tiles.PowerCenter;

/**
 * The UseEnergyAction class represents an action where a player interacts with a
 * PowerCenter to consume energy and subsequently updates their energy level on
 * the PlayerBoard. This action ensures the execution of game logic rules when
 * a player attempts to use energy resources.
 *
 * This class is a specific implementation of the abstract ComponentAction class
 * and overrides the visit method for the PowerCenter component type.
 *
 * Key Responsibilities:
 * - Validates if the action is allowed based on the current PlayerState.
 * - Consumes energy from the PowerCenter component.
 * - Updates the PlayerBoard to reflect the reduced energy level.
 *
 * Behavior:
 * - If the action is not permitted by the PlayerState, an IllegalStateException
 *   is thrown to enforce the game's state rules.
 * - The PowerCenter's energy is reduced by invoking its useEnergy method.
 * - The PlayerBoard's energy count is decremented by 1.
 */
public class UseEnergyAction extends ComponentAction {
    /**
     * Represents the player's board in the game, responsible for tracking and managing
     * various aspects of the player's progress and resources, including energy levels.
     *
     * The playerBoard variable is a core component in handling player-related state
     * updates, particularly when energy resources are modified as a result of game actions.
     *
     * Responsibilities:
     * - Tracks the player's energy levels and other game-related statistics.
     * - Provides methods to update player-related data, such as energy consumption.
     *
     * Usage:
     * This variable is utilized by the UseEnergyAction class to reflect changes
     * in the player's energy levels during interactions with specific game components.
     */
    private PlayerBoard playerBoard;
    /**
     * Constructs a UseEnergyAction with the specified PlayerBoard. This action
     * facilitates interaction with energy-related components during the game, enabling
     * players to consume energy and reflect updates on their PlayerBoard.
     *
     * @param playerBoard the PlayerBoard associated with the player, which tracks
     *                    the energy level and is updated as the action is performed
     */
    public UseEnergyAction(PlayerBoard playerBoard) {
        this.playerBoard = playerBoard;
    }

    /**
     * Initiates the process where a player interacts with a PowerCenter, consumes energy,
     * and updates the PlayerBoard accordingly. The method enforces game logic rules based
     * on the player's current state and the energy availability in the PowerCenter.
     *
     * @param powerCenter the PowerCenter component with which the player interacts to consume energy
     * @param playerState the current state of the player, used to validate if the action can be executed
     * @throws IllegalStateException if the player's current state does not allow the action to be performed
     */
    @Override
    public void visit(PowerCenter powerCenter, PlayerState playerState) {
        if (!playerState.allows(this)){
            throw new IllegalStateException("You are not allowed to perform this action in this state");
        }
        powerCenter.useEnergy();
        playerBoard.setEnergy(-1);
    }

}
