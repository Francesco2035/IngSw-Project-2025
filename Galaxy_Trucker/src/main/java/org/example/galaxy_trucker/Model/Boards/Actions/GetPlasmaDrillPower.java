package org.example.galaxy_trucker.Model.Boards.Actions;

import org.example.galaxy_trucker.Model.PlayerStates.PlayerState;
import org.example.galaxy_trucker.Model.Tiles.PlasmaDrill;

/**
 * GetPlasmaDrillPower is a subclass of ComponentAction that calculates
 * the total power of plasma drills by visiting PlasmaDrill components
 * in a player state. It keeps track of the accumulated power and the
 * number of PlasmaDrills with double power in the system.
 *
 * This class uses the visitor pattern to interact with PlasmaDrill
 * components and validates actions against the current PlayerState.
 */
public class GetPlasmaDrillPower extends ComponentAction {
    /**
     * Represents the accumulated power of plasma drills in the context of the player's state.
     * This variable is updated as PlasmaDrill components are visited during the execution of
     * the action and reflects the total power, including contributions from all visited drills.
     *
     * The power value tracks the cumulative effectiveness of plasma drills and may factor
     * into game mechanics or player strategy decisions. It is initialized with a starting
     * value and modified during the action's lifecycle.
     */
    private double power;
    /**
     * Represents the count of PlasmaDrill components that have double power.
     * The variable is incremented each time a PlasmaDrill with non-zero power
     * is encountered during the execution of the visitor pattern in the
     * context of the GetPlasmaDrillPower class.
     *
     * This variable is primarily used to track the number of PlasmaDrill
     * components with enhanced properties and is essential for determining
     * specific configurations or statuses of the player's setup during the game.
     *
     * Default value is initialized to 0.
     */
    private int countDoublePlasmaDrills = 0;
    /**
     * Constructs a new GetPlasmaDrillPower action to calculate the total power
     * contributed by PlasmaDrill components in a player's current state.
     * The initial power value can be set during instantiation and will be updated
     * as PlasmaDrill components are visited.
     *
     * @param SinglePower the initial power value to set for this action, representing
     *                    the starting plasma drill power before visiting components.
     */
    public GetPlasmaDrillPower(double SinglePower) {
        power = SinglePower;
    }

    /**
     * Visits a PlasmaDrill component and updates the power and count of double-powered PlasmaDrills
     * in the current player state. The method ensures that the action is allowed in the provided
     * player state before proceeding with the calculations.
     *
     * @param plasmaDrill the PlasmaDrill component being visited and analyzed
     * @param playerState the current state of the player, used to validate if the action is allowed
     * @throws IllegalStateException if the action is not permitted in the given player state
     */
    @Override
    public void visit(PlasmaDrill plasmaDrill, PlayerState playerState) {
        if (!playerState.allows(this)){
            throw new IllegalStateException("You are not allowed to perform this action in this state");
        }
        double temp = plasmaDrill.getCannonPower();
        if (temp != 0){
            countDoublePlasmaDrills++;
        }
        power += plasmaDrill.getCannonPower();
    }

    /**
     * Retrieves the accumulated power value.
     *
     * @return the total power as a double.
     */
    public double getPower(){
        return power;
    }

    /**
     * Retrieves the count of PlasmaDrill components with double power in the system.
     *
     * @return the number of PlasmaDrills with double power.
     */
    public int getCountDoublePlasmaDrills(){
        return countDoublePlasmaDrills;
    }

}
