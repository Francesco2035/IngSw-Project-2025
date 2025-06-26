package org.example.galaxy_trucker.Model.PlayerStates;

import org.example.galaxy_trucker.Commands.Command;
import org.example.galaxy_trucker.Commands.ConsumeEnergyCommand;
import org.example.galaxy_trucker.ClientServer.Messages.PhaseEvent;
import org.example.galaxy_trucker.Model.Boards.Actions.UseEnergyAction;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.Cards.Card;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.Tiles.PowerCenter;
import org.example.galaxy_trucker.View.ClientModel.States.ConsumingEnergyClient;

import java.util.ArrayList;

/**
 * The ConsumingEnergy class represents a state in which the player is performing
 * actions related to energy consumption within the game. This state controls the
 * player's ability to execute specific commands and actions involving energy usage.
 *
 * It is a subclass of PlayerState and defines the behavior for energy-related commands
 * and actions, including validations and default command creation.
 *
 * The class provides implementations for determining allowed commands, creating default
 * commands, and transitioning to the corresponding client state.
 */
public class ConsumingEnergy extends PlayerState {

    /**
     * Determines whether the specified ConsumeEnergyCommand is allowed in the current state.
     *
     * @param command the ConsumeEnergyCommand to be evaluated for allowance
     * @return true if the command is allowed, false otherwise
     */
    @Override
    public boolean allows(ConsumeEnergyCommand command){
        return true;
    }
//    @Override
//    public Command PlayerAction(String json, Player player) {
//        JsonNode root = JsonHelper.parseJson(json);
//
//        String title = JsonHelper.getRequiredText(root, "title");
//        if (!"Consume Energy".equals(title)) {
//            throw new InvalidInput("Unexpected action type: " + title);
//        }
//
//        ArrayList<IntegerPair> coordinates = JsonHelper.readIntegerPairs(root, "coordinates");
//
//        Card card = player.getCurrentCard();
//        return new ConsumeEnergyCommand(card, coordinates);
//    }

    /**
     * Determines if the specified UseEnergyAction is allowed in the current player state.
     *
     * @param action the UseEnergyAction to evaluate
     * @return true if the action is permitted, otherwise false
     */
    @Override
    public boolean allows(UseEnergyAction action) {
        return true;
    }

    /**
     * Creates a default command for consuming energy during the player's turn.
     *
     * @param gameId the unique identifier of the current game.
     * @param player the player for whom the default command is created.
     * @return a new {@code ConsumeEnergyCommand} containing the player's chosen coordinates
     *         or empty coordinates if there is insufficient energy.
     */
    @Override
    public Command createDefaultCommand(String gameId, Player player) { // questo stato dovrebbe accadere se e solo se non hai cargo da farti rubare quind ti ruibano le energie
        int lv= player.getCommonBoard().getLevel();

        Card card = player.getCurrentCard();
        PlayerBoard board= player.getmyPlayerBoard();
        int p= card.getDefaultPunishment();
        ArrayList<IntegerPair> coords = new ArrayList<>();

        if(board.getEnergy()<p){
            return new ConsumeEnergyCommand(coords,gameId,player.GetID(),lv,"ConsumeEnrgyCommand","placeholder");
        }
        else{
            int i=0;
            int j=0;
            ArrayList<PowerCenter> powerCenters= board.getPowerCenters();
            while (i<p){
                for (int z=0; (z<powerCenters.get(j).getType() )&&i<p;z++){
                    coords.add(new IntegerPair(powerCenters.get(j).getX(),powerCenters.get(j).getY()));
                    i++;
                }
                j++;
            }
        }

        return new ConsumeEnergyCommand(coords,gameId,player.GetID(),lv,"ConsumeEnrgyCommand","placeholder");
    }

    /**
     * Converts the current server-side state representation of the "Consuming Energy" phase
     * into a client-side representation suitable for serialization and rendering.
     *
     * This method creates and returns a new PhaseEvent object,
     * which encapsulates a ConsumingEnergyClient instance. The ConsumingEnergyClient represents
     * a specific state in the game where energy consumption-related events are being processed.
     *
     * @return a PhaseEvent instance containing a ConsumingEnergyClient object. This object can
     *         be used to communicate the current player state during the "Consuming Energy" phase
     *         to the client.
     */
    @Override
    public PhaseEvent toClientState() {
        return new PhaseEvent(new ConsumingEnergyClient());
    }
}
