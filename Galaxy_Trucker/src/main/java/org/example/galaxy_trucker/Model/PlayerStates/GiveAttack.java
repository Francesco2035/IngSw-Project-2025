package org.example.galaxy_trucker.Model.PlayerStates;

import org.example.galaxy_trucker.Commands.Command;
import org.example.galaxy_trucker.Commands.GiveAttackCommand;
import org.example.galaxy_trucker.ClientServer.Messages.PhaseEvent;
import org.example.galaxy_trucker.Model.Boards.Actions.GetPlasmaDrillPower;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.View.ClientModel.States.GiveAttackClient;

/**
 * The GiveAttack class represents a specific state of a player in the game where
 * the player can initiate an attack action. This state extends the abstract
 * PlayerState class, leveraging its structure to define and control
 * permissible actions and commands specific to this state.
 */
public class GiveAttack extends PlayerState {


    /**
     * Constructs a new instance of the GiveAttack class, representing the state
     * where a player is allowed to initiate an attack action. This constructor
     * initializes the state without requiring additional parameters.
     */
    public GiveAttack() {

    }

//    @Override
//    public Command PlayerAction(String json, Player player){
//
//        JsonNode root = JsonHelper.parseJson(json);
//        String title = JsonHelper.getRequiredText(root, "title");
//        if (!"give_attack".equals(title)) {
//            throw new IllegalArgumentException("Unexpected action type: " + title);
//        }
//        ArrayList<IntegerPair> coordsArray =JsonHelper.readUniqueIntegerPairs(root, "coordinates");
//
//        Card card = player.getCurrentCard();
//        return new GiveAttackCommand(card, coordsArray, player);
//    }

    /**
     * Determines whether the specified GetPlasmaDrillPower action is allowed
     * in the current state of the player.
     *
     * @param action the instance of GetPlasmaDrillPower action to be checked.
     * @return true if the action is permissible in the current state; otherwise, returns false.
     */
    @Override
    public boolean allows(GetPlasmaDrillPower action){
        return true;
    }

    /**
     * Creates a default command to be executed when the player is in the GiveAttack state.
     * This command is specific to the player's current level and game context.
     *
     * @param gameId the unique identifier of the current game
     * @param player the player for whom the default command is being created
     * @return a new instance of the GiveAttackCommand configured with the player's level and game details
     */
    @Override
    public Command createDefaultCommand(String gameId, Player player) {
        int lv= player.getCommonBoard().getLevel();

        return new GiveAttackCommand(null,gameId,player.GetID(),lv,"GiveAttackCommand","placeholder"); /// devo mettere il token
    }

    /**
     * Determines if the provided GiveAttackCommand is allowed in the current player state.
     *
     * @param action the GiveAttackCommand to be verified for allowance in this state.
     * @return true if the GiveAttackCommand is permitted in this state, false otherwise.
     */
    @Override
    public boolean allows(GiveAttackCommand action) {
        return true;
    }

    /**
     * Converts the current player state, represented by the {@code GiveAttack} instance,
     * into a corresponding client-side representation.
     * This conversion is used to encapsulate the player's current state and phase
     * in the game for communication with the client.
     *
     * @return a {@code PhaseEvent} object containing the client-side representation
     *         of the "Give Attack" phase, encapsulated as a {@code GiveAttackClient}.
     */
    @Override
    public PhaseEvent toClientState() {
        return new PhaseEvent(new GiveAttackClient());
    }

}
