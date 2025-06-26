package org.example.galaxy_trucker.Model.PlayerStates;

import org.example.galaxy_trucker.Commands.AcceptCommand;
import org.example.galaxy_trucker.ClientServer.Messages.PhaseEvent;
import org.example.galaxy_trucker.Commands.Command;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.View.ClientModel.States.AcceptClient;

/**
 * The Accepting class represents a specific state of a player in the game
 * where the player has the ability to accept or decline certain game-related
 * actions. It extends the PlayerState abstract class and customizes behavior
 * related to the "accepting" phase of the game.
 *
 * Key behaviors in this state include:
 * - Allowing the execution of AcceptCommand objects.
 * - Creating a default command for this state to define expected behavior.
 * - Transitioning to a client-side representation of this state.
 *
 * Responsibilities:
 * - Define acceptance logic for commands during the "accepting" phase.
 * - Facilitate communication between the game logic and the client by converting
 *   the state into a PhaseEvent that encapsulates its client-side representation.
 */
public class Accepting extends PlayerState{
/**
 * Determines whether the specified AcceptCommand is allowed in the current "accepting" phase.
 *
 * This method evaluates the given AcceptCommand object and returns a boolean value indicating
 * whether the command can be executed during this state. In the "accepting" phase, the default
 * behavior is to permit all AcceptCommand executions, which is reflected in the constant true
 * return value.
 *
 * @param command the AcceptCommand to be evaluated for allowance within the current state
 * @return true if the command is allowed in the "accepting" phase, otherwise false
 */
//    @Override
//    public Command PlayerAction(String json, Player player) {
//        ObjectMapper mapper = new ObjectMapper();
//        boolean accepting;
//        JsonNode root = JsonHelper.parseJson(json);
//        String title = JsonHelper.getRequiredText(root, "title");
//        if (!"Accepting".equals(title)) {
//            throw new IllegalArgumentException("Unexpected action type: " + title);
//        }
//        accepting = JsonHelper.readBoolean(root, "accepting");
//
//        Card card = player.getCurrentCard();
//        return new AcceptCommand(card, accepting);
//
//
//    }
    @Override
    public boolean allows(AcceptCommand command) {
        return true;
    }


    /**
     * Creates a default command for the "accepting" phase of the game. The command
     * generated is an {@link AcceptCommand} tailored to the provided game and player
     * context. This method defines the behavior to be performed by default when the
     * player is in the accepting state.
     *
     * @param gameId the unique identifier of the game for which the command is being created
     * @param player the player for whom the default command is being constructed
     * @return a default {@link AcceptCommand} configured with the game's ID, the player's ID,
     *         the current level from the player's common board, a predefined command title, and default values
     */
    @Override
    public Command createDefaultCommand(String gameId,Player player) {
        int lv= player.getCommonBoard().getLevel();
        return new AcceptCommand(gameId,player.GetID(),lv,"AcceptCommand",false,"placeholder"); /// devo mettere il token
    }

    /**
     * Converts the current state of the player into a client-side representation.
     * This method encapsulates the current "accepting" phase of the game into
     * a {@link PhaseEvent} object that wraps an {@link AcceptClient} instance,
     * which represents the client-side behavior for this phase.
     *
     * @return a PhaseEvent object containing an AcceptClient instance that represents
     *         the player's current state in the "accepting" phase of the game. This
     *         object facilitates phase-specific communication between the game logic
     *         and the client.
     */
    @Override
    public PhaseEvent toClientState() {
        return new PhaseEvent(new AcceptClient());
    }
}


