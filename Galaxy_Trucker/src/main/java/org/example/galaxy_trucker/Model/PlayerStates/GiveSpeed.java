package org.example.galaxy_trucker.Model.PlayerStates;

import org.example.galaxy_trucker.Commands.GiveSpeedCommand;
import org.example.galaxy_trucker.ClientServer.Messages.PhaseEvent;
import org.example.galaxy_trucker.Model.Boards.Actions.GetEnginePower;
import org.example.galaxy_trucker.Commands.Command;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.View.ClientModel.States.GiveSpeedClient;

/**
 * The GiveSpeed class represents a specific player state that allows
 * the execution of the GiveSpeedCommand and related actions. It extends
 * the PlayerState abstract class and overrides its methods to customize
 * behavior for this particular state.
 *
 * In this state, players can perform actions to grant speed enhancements
 * to their cards or interact with specific gameplay components. The class
 * also defines the conditions under which particular commands and actions
 * are allowed, ensuring consistent gameplay logic.
 */
public class GiveSpeed  extends PlayerState{
//    @Override
//    public Command PlayerAction(String json, Player player) {
//        JsonNode root = JsonHelper.parseJson(json);
//        String title = JsonHelper.getRequiredText(root, "title");
//        if (!"give_speed".equals(title)) {
//            throw new IllegalArgumentException("Unexpected action type: " + title);
//        }
//        ArrayList<IntegerPair> coordsArray =JsonHelper.readUniqueIntegerPairs(root, "coordinates");
//
//        Card card = player.getCurrentCard();
//        return new GiveSpeedCommand(card, coordsArray, player);
//    }

    /**
     * Determines whether the specified GetEnginePower action is allowed
     * in the current state.
     *
     * This method evaluates the compatibility of the player state with the
     * GetEnginePower action and enforces gameplay rules, allowing or disallowing
     * the action based on the current state logic.
     *
     * @param action the GetEnginePower action to be evaluated for allowance.
     * @return true if the GetEnginePower action is permitted in the current state; false otherwise.
     */
    @Override
    public boolean allows(GetEnginePower action){
        return true;
    }

    /**
     * Determines whether the given `GiveSpeedCommand` is allowed in the current player state.
     * This method checks if the specified command can be executed based on the rules
     * and logic defined for the `GiveSpeed` player state.
     *
     * @param command the `GiveSpeedCommand` instance to be checked for validity
     * @return {@code true} if the command is allowed in this state; otherwise, {@code false}
     */
    @Override
    public boolean allows(GiveSpeedCommand command){
        return true;
    }

    /**
     * Creates a default GiveSpeedCommand for the provided game and player.
     * The command is initialized with the game's ID, the player's ID, and
     * the level from the player's common board, along with placeholder values.
     *
     * @param gameId the unique identifier of the game for which the command is created
     * @param player the player who will execute the default command
     * @return a new instance of GiveSpeedCommand configured with the provided parameters
     */
    @Override
    public Command createDefaultCommand(String gameId, Player player) {
        int lv= player.getCommonBoard().getLevel();
        return new GiveSpeedCommand(null,gameId,player.GetID(),lv,"GiveSpeedCommand","placeholder"); /// devo mettere il token
    }


    /**
     * Converts the current game state to a client-side representation suitable for rendering
     * or interaction during the "Give Speed" phase. This method creates a new PhaseEvent
     * with a GiveSpeedClient instance, encapsulating the player's state and commands specific
     * to this phase.
     *
     * @return a PhaseEvent object containing a GiveSpeedClient instance, which represents
     *         the client-side state for the "Give Speed" phase of the game. This enables
     *         interaction and visualization of the state within the client application.
     */
    @Override
    public PhaseEvent toClientState() {
        return new PhaseEvent(new GiveSpeedClient());
    }
}
