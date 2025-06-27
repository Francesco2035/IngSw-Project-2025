package org.example.galaxy_trucker.Model.Boards.Actions;

import org.example.galaxy_trucker.Model.PlayerStates.PlayerState;
import org.example.galaxy_trucker.Model.Tiles.HotWaterHeater;


/**
 * GetEnginePower is an implementation of the ComponentAction class and is used to calculate
 * the total engine power from visited components while maintaining a count of specific
 * engine types.
 *
 * This action is designed to interact with HotWaterHeater components in the game. It retrieves
 * and accumulates the engine power contributed by HotWaterHeater components and tracks the
 * number of double-powered engines encountered.
 *
 * Game rules are enforced by validating the action via the PlayerState provided during
 * interaction with components.
 */
public class GetEnginePower extends ComponentAction {
    /**
     * Represents the total engine power accumulated by the GetEnginePower action.
     *
     * This variable keeps track of the sum of engine power retrieved from visited components,
     * such as HotWaterHeater. The power value is calculated and updated whenever the action
     * interacts with a valid component per game rules.
     *
     * The value of this variable increases during successive visits to components that contribute
     * engine power, ensuring the total power reflects the aggregated contributions of all visited
     * components.
     */
    private int power;
    /**
     * Tracks the count of double-powered engines encountered and contributes to overall engine power calculation.
     *
     * This variable is incremented each time a component with non-zero engine power is visited during the execution
     * of the `GetEnginePower` action. Primarily used within the `visit` method when interacting with a
     * `HotWaterHeater` component, it reflects how many such engines have been found. The count is used as part
     * of the game's logic to maintain statistics on specific engine types.
     *
     * Context:
     * - Belongs to the `GetEnginePower` class which calculates and accumulates engine power.
     * - Incremented only when valid components are encountered based on the player's allowed state.
     */
    private int countDoubleEngine = 0;
    /**
     * Constructs a GetEnginePower action that initializes the power of a single engine.
     * This class is designed to calculate the total power contributed by engine components
     * and tracks specific engine configurations, such as double-powered engines.
     *
     * @param singlePower the initial power value of a single engine, which serves as the base value
     *                    for accumulating total power during the execution of this action.
     */
    public GetEnginePower(int singlePower) {
        power = singlePower;
    }

    /**
     * Visits the specified HotWaterHeater component and updates the state
     * with the engine power information extracted from the component.
     * Ensures the action is allowed based on the player's current state
     * before proceeding.
     *
     * @param hotWaterHeater the HotWaterHeater component to be visited. Used to retrieve
     *                       its engine power and update relevant counters.
     * @param playerState the current state of the player. This is used to
     *                    verify if the action is permissible.
     * @throws IllegalStateException if the player's state does not allow this action.
     */
    @Override
    public void visit(HotWaterHeater hotWaterHeater, PlayerState playerState) {

        if (!playerState.allows(this)){
            throw new IllegalStateException("You are not allowed to perform this action in this state");
        }
        int temp = hotWaterHeater.getEnginePower();
        if (temp != 0) {
            countDoubleEngine++;
        }
        power+= temp;
    }

    /**
     * Retrieves the accumulated engine power.
     *
     * @return the total power accumulated from the engine components.
     */
    public  int getPower(){
        return power;
    }

    /**
     * Retrieves the count of double-powered engine components encountered.
     *
     * This method returns the total count of engine components with non-zero power values
     * that have been visited and registered during the execution of related actions.
     *
     * @return the count of double-powered engine components.
     */
    public int getCountDoubleEngine(){
        return countDoubleEngine;
    }

}
