package org.example.galaxy_trucker.Model.Boards.Actions;

import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.PlayerStates.PlayerState;
import org.example.galaxy_trucker.Model.Tiles.HousingUnit;

/**
 * The KillCrewAction class represents an action aimed at removing crew members from a housing unit
 * as part of the game logic. This action interacts with the provided PlayerBoard and modifies its
 * state depending on the type of crew member removed.
 *
 * This class extends the abstract ComponentAction class and overrides the visit method specific to
 * HousingUnit components. The player state is validated before performing the action to ensure state
 * compliance rules are followed.
 *
 * Key Behavior:
 * - When visiting a HousingUnit, a kill operation is executed to determine which type of crew member
 *   is removed.
 * - Based on the result of the kill operation:
 *   - If a human crew member is killed, the number of humans is decremented on the PlayerBoard.
 *   - If a purple alien is killed, the purple alien status is updated on the PlayerBoard.
 *   - If a brown alien is killed, the brown alien status is updated on the PlayerBoard.
 * - An exception is thrown if the action is not allowed due to the player's current state.
 *
 * This implementation supports the visitor pattern, allowing the KillCrewAction to interact
 * specifically with the HousingUnit component while delegating other visit methods to the default
 * behavior defined in the ComponentAction base class.
 *
 * Constructor Details:
 * - KillCrewAction(PlayerBoard playerBoard): Initializes the KillCrewAction with a reference to
 *   the PlayerBoard, enabling modifications to the player's state during the action.
 *
 * Method Details:
 * - void visit(HousingUnit housing, PlayerState playerState):
 *   1. Validates if the action is allowed in the current player state.
 *   2. Executes the housing unit's kill operation to remove a crew member.
 *   3. Updates the PlayerBoard accordingly:
 *      - Decrease human crew count if a human is killed.
 *      - Update the status of aliens (purple/brown) based on the kill result.
 *   4. Throws an IllegalStateException if the action is not permitted by the current player state.
 */
public class KillCrewAction extends ComponentAction {
    /**
     * The playerBoard variable represents the state and attributes of a player's board in the game.
     *
     * This object is central to tracking and managing the player's progress, including the number
     * of crew members and their status (e.g., humans, purple aliens, brown aliens).
     *
     * Responsibilities:
     * - Stores and updates the player's state based on game actions, such as killing crew members.
     * - Provides methods to modify specific attributes of the player's board, reflecting changes
     *   during gameplay.
     *
     * Usage Context:
     * - Used by the KillCrewAction class to update the player's board when a crew member is killed.
     * - Tracks modifications such as decrementing the number of human crew members or changing the
     *   status of aliens based on game events.
     */
    private PlayerBoard playerBoard;

    /**
     * Constructs a new KillCrewAction instance, which allows modifying the state
     * of a PlayerBoard by executing an action to remove crew members.
     *
     * @param playerBoard the PlayerBoard associated with the player, on which
     *                    the effects of the KillCrewAction will be applied
     */
    public KillCrewAction(PlayerBoard playerBoard) {
        this.playerBoard = playerBoard;
    }

    /**
     * Executes a visit operation on a HousingUnit as part of the visitor pattern.
     * This method validates the current player state for permission before proceeding
     * to interact with the HousingUnit. Depending on the type of entity that is
     * removed during the operation, the player's board state will be updated accordingly.
     *
     * @param housing The HousingUnit being visited and interacted with. The visit triggers
     *                a kill operation on the HousingUnit that determines which type of entity
     *                (human or alien) is affected.
     * @param playerState The current state of the player, used to validate if the visit action
     *                    is permitted. If the player state disallows the action, an
     *                    IllegalStateException is thrown.
     * @throws IllegalStateException If the action is not permitted in the player's current state.
     */
    @Override
    public void visit(HousingUnit housing, PlayerState playerState) {
        if (!playerState.allows(this)){
            throw new IllegalStateException("You are not allowed to perform this action in this state");
        }
        int typeKill = housing.kill();
        if (typeKill == 2){
            playerBoard.setNumHumans(-1);
        }
        else if (typeKill == 1){
            playerBoard.setPurpleAlien(false);
        }
        else{
            playerBoard.setBrownAlien(false);
        }
    }


}
