package org.example.galaxy_trucker.Model.PlayerStates;

import org.example.galaxy_trucker.Commands.*;
import org.example.galaxy_trucker.ClientServer.Messages.PhaseEvent;
import org.example.galaxy_trucker.Model.Boards.GameBoard;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.View.ClientModel.States.BuildingClient;

/**
 * Represents the state in which a player is constructing a ship.
 * This state allows specific commands related to ship building
 * and provides behavior to manage the construction process.
 */
public class BuildingShip extends PlayerState {


    /**
     * Determines whether the given BuildingCommand is allowed in the current player state.
     *
     * @param command the BuildingCommand that is to be checked for validity within this state
     * @return true if the provided command is allowed, otherwise false
     */
    @Override
    public boolean allows(BuildingCommand command) {
        return true;
    }

    /**
     * Determines whether the given ReadyCommand is allowed in the current state.
     *
     * @param command the ReadyCommand to be checked for permission
     * @return true if the ReadyCommand is allowed in the current state, false otherwise
     */
    @Override
    public boolean allows(ReadyCommand command) {
        return true;
    }

    /**
     * Determines whether the provided FinishBuildingCommand is allowed in the current
     * player state. The method evaluates if the given command can be executed based on
     * the rules and conditions of the current state.
     *
     * @param command the FinishBuildingCommand to evaluate
     * @return true if the command is permitted in this state, false otherwise
     */
    @Override
    public boolean allows(FinishBuildingCommand command){return true;}

    /**
     * Creates a default command for the BuildingShip state, representing the finishing
     * of the ship construction process. This command prepares the player by completing
     * the ship building, setting readiness, and marking them as having acted.
     *
     * @param gameId the unique identifier of the game
     * @param player the player for whom the default command is being created
     * @return a Command instance that finalizes the ship building process for the player
     */
    @Override
    public Command createDefaultCommand(String gameId, Player player) { // dovrebbe andare bene?

        return new Command() {
            @Override
            public void execute(Player player) {
                System.out.println("FINISH BUILDING");

                try{
                    GameBoard board = player.getCommonBoard();
                    player.EndConstruction();

                }
                catch(Exception e){
                    e.printStackTrace();
                }
                player.SetReady(true);
                player.SetHasActed(true);
                System.out.println("FINISH BUILDING FINISHED");

            }
        };



    }


    /**
     * Resets the player's "has acted" status to false, indicating
     * that the player can perform an action.
     *
     * @param player the player whose "has acted" status is to be reset
     */
    @Override
    public void shouldAct(Player player) {
        player.SetHasActed(false);
    }
    /**
     * Converts the current state of this object into a client-compatible representation
     * specifically for the "Building" phase of the game.
     *
     * @return a PhaseEvent object encapsulating a BuildingClient state, which represents
     *         the current phase-specific client state associated with the building
     *         phase of the game.
     */
    @Override
    public PhaseEvent toClientState() {
        return new PhaseEvent(new BuildingClient());
    }
}
