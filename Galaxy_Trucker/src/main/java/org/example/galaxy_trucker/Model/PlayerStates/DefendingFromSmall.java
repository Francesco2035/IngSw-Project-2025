package org.example.galaxy_trucker.Model.PlayerStates;

import org.example.galaxy_trucker.Commands.DefendFromSmallCommand;
import org.example.galaxy_trucker.ClientServer.Messages.PhaseEvent;
import org.example.galaxy_trucker.Commands.Command;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.View.ClientModel.States.DefendingFromSmallClient;

/**
 *  * Represents the "DefendingFromSmall" state of a player in the game.
 * This state is a concrete implementation of the abstract {@code PlayerState} class
 * and defines the behavior and constraints when a player is defending from small-scale threats.
 *
 * The goal of this class is to manage and handle actions and commands specific to
 * the "DefendingFromSmall" state, as well as provide the client with the appropriate
 * representation of this state.
 */
public class DefendingFromSmall extends PlayerState{
//    @Override
//    public Command PlayerAction(String json, Player player) {
//        IntegerPair batteryComp;
//        JsonNode root = JsonHelper.parseJson(json);
//        String title = JsonHelper.getRequiredText(root, "title");
//        if (!"Defending From Small".equals(title)) {
//            throw new IllegalArgumentException("Unexpected action type: " + title);
//        }
//        root = JsonHelper.getNode(root, "BatteryComp");
//        int x = JsonHelper.readInt(root, "x");
//        int y = JsonHelper.readInt(root, "y");
//        batteryComp = new IntegerPair(x, y);
//
//
//        Card card = player.getCurrentCard();
//        return new DefendFromSmallCommand(card, batteryComp);
//    }

    /**
     * Determines whether the specified {@code DefendFromSmallCommand} is allowed
     * to execute in the current "DefendingFromSmall" player state.
     *
     * @param command the {@code DefendFromSmallCommand} to be evaluated
     *                against the constraints of the current state.
     * @return true if the {@code DefendFromSmallCommand} is permitted in this state,
     *         false otherwise.
     */
    @Override
    public boolean allows(DefendFromSmallCommand command) {
        return true;
    }
    /**
     * Creates the default command for the "DefendingFromSmall" state of a player.
     * This method generates a command representing a player's defensive action in the current game state.
     *
     * @param gameId the unique identifier of the game.
     * @param player the player for whom the command is being created.
     * @return a {@code DefendFromSmallCommand} representing the default action in this state.
     */
    @Override
    public Command createDefaultCommand(String gameId, Player player) {
        int lv= player.getCommonBoard().getLevel();
        return new DefendFromSmallCommand(null,gameId,player.GetID(),lv,"DefendingFromSmallCommand","placeholder"); /// devo mettere il token
    }

    /**
     * Converts the current player state to a client-consumable {@code PhaseEvent}.
     *
     * This method wraps the current state of the player (indicating
     * they are in the "Defending From Small" phase) into a {@code PhaseEvent}
     * object that can be sent to the client. The encapsulated {@code DefendingFromSmallClient}
     * is a client-side representation of this specific game state, enabling
     * rendering and interaction for the player during this phase.
     *
     * @return a {@code PhaseEvent} object containing a {@code DefendingFromSmallClient},
     *         which represents the player's current state in the "Defending From Small" phase.
     */
    @Override
    public PhaseEvent toClientState() {
        return new PhaseEvent(new DefendingFromSmallClient());
    }
}
