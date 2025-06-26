package org.example.galaxy_trucker.Model.PlayerStates;

import org.example.galaxy_trucker.Model.Boards.Actions.KillCrewAction;

/**
 * The WaitingEpidemic class represents a specialized state that extends the Waiting state
 * in a game framework. This state overrides specific behavior of the base Waiting class
 * to tailor interactions during an epidemic scenario.
 *
 * In this state, all actions that involve killing crew members, such as the KillCrewAction,
 * are permitted without restriction. The class modifies the default behavior such that
 * attempts to perform these actions always succeed, irrespective of the current game status
 * or player-specific conditions.
 *
 * This behavior is implemented by overriding the `allows` method, ensuring that any KillCrewAction
 * is granted permission when interacting with this state.
 *
 * Key Behavior:
 * - Allows all KillCrewAction executions.
 * - Operates as a game-specific override of the Waiting state with relaxed restrictions.
 *
 * Constructor Details:
 * - This class relies on the default constructor provided by the Java runtime and does not
 *   include additional initialization beyond what is handled by the Waiting superclass.
 *
 * Method Details:
 * - boolean allows(KillCrewAction action):
 *   Always returns true, indicating that the KillCrewAction is permitted in this state.
 */
public class WaitingEpidemic extends Waiting{

    /**
     * Determines whether the specified KillCrewAction is allowed in the current state.
     * This method always returns true, signifying that any KillCrewAction is permitted
     * without restriction in the context of this state.
     *
     * @param action the KillCrewAction to check for allowance
     * @return true if the action is allowed, which is always the case in this implementation
     */
    @Override
    public boolean allows(KillCrewAction action) {
        return true;
    }
}
