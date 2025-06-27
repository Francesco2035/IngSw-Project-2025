package org.example.galaxy_trucker.Model.PlayerStates;

import org.example.galaxy_trucker.Commands.AddCrewCommand;
import org.example.galaxy_trucker.Commands.Command;
import org.example.galaxy_trucker.ClientServer.Messages.PhaseEvent;
import org.example.galaxy_trucker.Model.Boards.Actions.AddCrewAction;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.Tiles.HousingUnit;
import org.example.galaxy_trucker.View.ClientModel.States.AddCrewClient;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Represents the state of a player when they are allowed to add crew members to their game board.
 * This class extends the abstract PlayerState and overrides specific methods that enable the player
 * to perform actions or issue commands related to adding crew.
 *
 * The AddCrewState signifies a phase in the game where the addition of human and alien crew members
 * is permitted. It provides the logic for default behavior during this state and handles the transition
 * of the game to the client's user interface through corresponding events.
 */
public class AddCrewState extends PlayerState {


        /**
         * Determines whether the provided AddCrewCommand is allowed in the current state.
         * This method evaluates the game state to decide if the specified action can be
         * performed, based on the rules governing the AddCrewState.
         *
         * @param command the AddCrewCommand to be checked for allowance in the current state
         * @return true if the command is permitted in the current state, false otherwise
         */
        @Override
        public boolean allows(AddCrewCommand command){
            return true;
        }

        /**
         * Determines whether the provided AddCrewAction is allowed in the current state.
         *
         * @param action the AddCrewAction to be checked for allowance in the current state
         * @return true if the action is permitted in this state, false otherwise
         */
        @Override
        public boolean allows(AddCrewAction action) {
            return true;
        }

    /**
     * Creates the default command for the AddCrewState. This command adds crew members
     * to empty housing units on the player's board, provided the units do not contain
     * any humans, purple aliens, or brown aliens. After executing the command, the
     * player's state is set to ready.
     *
     * @param gameId the unique identifier of the current game.
     * @param player the player for whom the default command is being created.
     * @return the default command to execute within the AddCrewState.
     */
    @Override
    public Command createDefaultCommand(String gameId,Player player) {
        return new Command() {
            @Override
            public void execute(Player player) throws IOException {
                System.out.println("DEF ADD CREW");
                ArrayList<HousingUnit> units = player.getmyPlayerBoard().getHousingUnits();
                for (HousingUnit unit : units) {
                    if(unit.getNumHumans() == 0 && !unit.isPurpleAlien() && !unit.isBrownAlien()) {
                        player.getmyPlayerBoard().performAction(unit, new AddCrewAction(2, false, false, player.getmyPlayerBoard()), player.getPlayerState());
                    }
                }
                player.SetReady(true);

            }
        };
    }


    /**
     * Transitions the current game state to a client-side representation of the "Add Crew" phase.
     * This is used to update the client interface with the appropriate view and commands
     * for the "Add Crew" phase where players can add crew members, including humans
     * and aliens, to their respective game boards.
     *
     * @return a {@code PhaseEvent} instance encapsulating the {@code AddCrewClient} state,
     *         which enables the client-side representation and interaction during the "Add Crew" phase.
     */
    @Override
    public PhaseEvent toClientState() {
        return new PhaseEvent(new AddCrewClient());
    }
}
