package org.example.galaxy_trucker.Model.Boards.Actions;

import org.example.galaxy_trucker.Exceptions.InvalidInput;
import org.example.galaxy_trucker.Model.PlayerStates.PlayerState;
import org.example.galaxy_trucker.Model.Tiles.*;

/**
 * ComponentAction is an abstract class that provides a framework for implementing actions
 * performed on various components within the game. It defines specific visit methods
 * for each type of component but, by default, throws an InvalidInput exception to signal
 * that the action cannot be performed for the given component type.
 *
 * Subclasses of ComponentAction are expected to override the appropriate visit method(s)
 * to implement specific behavior for the corresponding component types.
 *
 * The design uses a visitor pattern, where each component is visited by the action,
 * and the specific action logic can be executed if compatible with the component type.
 *
 * The visit methods incorporate game logic validation and ensure compliance with the
 * game's state rules while interacting with components.
 *
 * The visit methods include:
 * - HotWaterHeater: Represents an interaction with a hot water heater component.
 * - Storage: Represents an interaction with a storage component.
 * - HousingUnit: Represents an interaction with a housing unit component, such as adding or removing crew.
 **/
public abstract class ComponentAction {

    /**
     * Handles the visit action for a HotWaterHeater component in the game.
     * This method provides an interaction interface between the action and
     * the HotWaterHeater component, incorporating game logic validation
     * through the provided PlayerState. By default, this implementation
     * throws an InvalidInput exception to indicate that the action is
     * invalid for this specific component type.
     *
     * @param hotWaterHeater the HotWaterHeater component being visited
     * @param state the current state of the player, used for validating
     *              the action or determining its effects
     * @throws InvalidInput when the interaction with the HotWaterHeater
     *                      component is not supported by this action
     */
    public void visit(HotWaterHeater hotWaterHeater, PlayerState state) {
        throw new InvalidInput("Invalid input for the specific action");
    }

    /**
     * Attempts to perform an action on a Storage component based on the current player state.
     * If the action is not supported or valid, an InvalidInput exception is thrown.
     *
     * @param storage the Storage component on which the action is being attempted
     * @param state the current state of the player, which determines if the action is allowed
     * @throws InvalidInput if the action is deemed invalid for the given component and state
     */
    public void visit(Storage storage, PlayerState state) {
        throw new InvalidInput("Invalid input for the specific action");
    }

    /**
     * Handles a visit action for a HousingUnit component within the game.
     * By default, this method throws an InvalidInput exception to indicate
     * that no specific action is defined for visiting a HousingUnit in the current state.
     *
     * Subclasses can override this method to implement specific behaviors
     * for a HousingUnit when visited.
     *
     * @param housing the HousingUnit being visited; represents a component
     *        within the game on which the action is to be performed.
     * @param state the current PlayerState, representing the state
     *        of the player interacting with the component.
     * @throws InvalidInput if the visit action is invalid or unsupported for this component type.
     */
    public void visit(HousingUnit housing, PlayerState state) {
        throw new InvalidInput("Invalid input for the specific action");
    }

    /**
     * Handles the visit action for a PowerCenter component within the game.
     * This implementation throws an InvalidInput exception by default, indicating
     * that the specific action is not supported for the given component type.
     *
     * @param powerCenter the PowerCenter component being interacted with
     * @param state the current state of the player invoking the action
     * @throws InvalidInput if the action is not valid for the provided inputs
     */
    public void visit(PowerCenter powerCenter, PlayerState state) {
        throw new InvalidInput("Invalid input for the specific action");
    }

    /**
     * Visits a PlasmaDrill component as part of an action. This specific implementation of the visit method
     * throws an InvalidInput exception, indicating that the action is invalid for the PlasmaDrill type.
     *
     * @param plasmaDrill the PlasmaDrill component being visited
     * @param state the current state of the player interacting with the PlasmaDrill
     * @throws InvalidInput always thrown to indicate the action is not valid for PlasmaDrill
     */
    public void visit(PlasmaDrill plasmaDrill, PlayerState state) {
        throw new InvalidInput("Invalid input for the specific action");
    }

}
