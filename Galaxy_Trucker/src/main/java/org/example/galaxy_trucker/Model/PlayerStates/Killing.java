package org.example.galaxy_trucker.Model.PlayerStates;

import org.example.galaxy_trucker.Commands.KillCommand;
import org.example.galaxy_trucker.ClientServer.Messages.PhaseEvent;
import org.example.galaxy_trucker.Model.Boards.Actions.KillCrewAction;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Commands.Command;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.Tiles.HousingUnit;
import org.example.galaxy_trucker.View.ClientModel.States.KillingClient;

import java.util.ArrayList;

/**
 * The Killing class represents a specific state of the player within the game,
 * extending the PlayerState class. This state is associated with the ability
 * to perform actions related to "killing" in the game.
 */
public class Killing extends PlayerState {

//    @Override
//    public Command PlayerAction(String json, Player player) {
//        JsonNode root = JsonHelper.parseJson(json);
//
//        String title = JsonHelper.getRequiredText(root, "title");
//        if (!"kill".equals(title)) {
//            throw new InvalidInput("Unexpected action type: " + title);
//        }
//
//        ArrayList<IntegerPair> coordinates = new ArrayList<>();
//        coordinates = JsonHelper.readIntegerPairs(root, "coordinates");
//
//        Card card = player.getCurrentCard();
//        return new KillCommand(card, coordinates);
//    }


    /**
     * Determines whether the specified KillCommand is allowed in the current player state.
     *
     * @param command the KillCommand instance to check.
     * @return true if the KillCommand is allowed, false otherwise.
     */
    @Override
    public boolean allows(KillCommand command){
        return true;
    }

    /**
     * Determines if the current state allows the KillCrewAction to be performed.
     *
     * @param action the KillCrewAction instance representing the action
     *               that checks whether it is permissible in this state
     * @return true if the action is allowed in the current state, false otherwise
     */
    @Override
    public boolean allows(KillCrewAction action) {
        return true;
    }

    /**
     * Creates a default command for the current player in the Killing state.
     * The command is based on the player's current context, including punishment details,
     * board status, and alien or human availability.
     *
     * @param gameId the ID of the current game.
     * @param player the player for whom the default command is being created.
     * @return a new instance of the {@link KillCommand}, pre-configured with
     *         automatically determined parameters such as coordinates and other attributes.
     */
    @Override
    public Command createDefaultCommand(String gameId, Player player) {


        int lv= player.getCommonBoard().getLevel();
        PlayerBoard board=player.getmyPlayerBoard();
        int punishment = player.getCurrentCard().getDefaultPunishment();

        int i=0;
        int j=0;
        ArrayList<IntegerPair> coords =new ArrayList<>();

        ArrayList< HousingUnit> housingUnits = board.getHousingUnits();
        while (i<punishment){
            if(housingUnits.get(j).isPurpleAlien()){
                coords.add(new IntegerPair(housingUnits.get(j).getX(),housingUnits.get(j).getY()));
                i++;
            }
            else if(housingUnits.get(j).isBrownAlien()){
                coords.add(new IntegerPair(housingUnits.get(j).getX(),housingUnits.get(j).getY()));
                i++;
            }
            else{
                for( int z=0;z<housingUnits.get(j).getNumHumans() && i<punishment;z++){
                    coords.add(new IntegerPair(housingUnits.get(j).getX(),housingUnits.get(j).getY()));
                    i++;
                }
            }
            j++;
        }
        return new KillCommand(coords,gameId,player.GetID(),lv,"KillCommand","placeholder");
    }

    /**
     * Converts the current player state to a client-facing state representation.
     * This method is used to generate a PhaseEvent object encapsulating a KillingClient,
     * which represents the player's current state in the client view.
     *
     * @return a PhaseEvent object containing the KillingClient representation of the current player state.
     */
    @Override
    public PhaseEvent toClientState() {
        return new PhaseEvent(new KillingClient());
    }
}
