package org.example.galaxy_trucker.Model.PlayerStates;

import org.example.galaxy_trucker.Commands.*;
import org.example.galaxy_trucker.ClientServer.Messages.PhaseEvent;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.View.ClientModel.States.BaseStateClient;

/**
 * BaseState represents a specific state of a player in the game.
 * It extends the abstract PlayerState class and defines the behavior
 * and commands allowed during this state.
 *
 * The BaseState allows all commands provided in its implementation
 * and defines a default behavior for creating commands and transitioning
 * to the appropriate client-side state.
 */
public class BaseState extends PlayerState {


    /**
     * Default constructor for the BaseState class.
     *
     * This constructor initializes a new instance of BaseState, which represents
     * a state where the player is allowed to perform various commands, such as
     * LoginCommand, ReadyCommand, and QuitCommand. The state defines default
     * behavior for command permissions and transitions to corresponding client-side states.
     */
    public BaseState(){

    }

    /**
     * Determines whether the given LoginCommand is allowed in the current player state.
     *
     * @param command the LoginCommand to be checked for allowance.
     * @return true if the LoginCommand is permitted in the current state, false otherwise.
     */
    @Override
    public boolean allows(LoginCommand command){
        return true;
    }

    /**
     * Determines whether the given ReadyCommand is allowed in the current player state.
     * This method provides logic to verify if the specified ReadyCommand can be executed
     * in the context of this state.
     *
     * @param command the ReadyCommand object to be checked
     * @return true if the ReadyCommand is allowed, false otherwise
     */
    @Override
    public boolean allows(ReadyCommand command){
        return true;
    }

    /**
     * Determines if the given QuitCommand is allowed in the current player state.
     *
     * @param command the QuitCommand to be checked for allowance
     * @return true if the QuitCommand is allowed, otherwise false
     */
    @Override
    public boolean allows(QuitCommand command){
        return true;
    }

    /**
     * Checks whether the DebugShip command is allowed in the current state.
     *
     * @param command the DebugShip command to check for permission
     * @return true if the DebugShip command is allowed, otherwise false
     */
    @Override
    public boolean allows(DebugShip command){return true;}

    /**
     * Creates a default command for the player in the current game.
     * This method generates a ReadyCommand with the required parameters
     * to indicate the player's readiness in the game.
     *
     * @param gameId the unique identifier of the game.
     * @param player the player for whom the default command is being created.
     * @return a ReadyCommand object containing the necessary details about the player's readiness.
     */
    @Override
    public Command createDefaultCommand(String gameId,Player player) {
        //tecnicamente potremmo aspettare una decina di secondi, anche se in realtà potrebbero decidere gli altri di aspettare il bro
        int lv= player.getCommonBoard().getLevel();
        return new ReadyCommand(gameId,player.GetID(),lv,"Ready",true,"placeholder");
    }

    /**
     * Transforms the current state into its client-side representation by generating
     * a PhaseEvent object. The PhaseEvent encapsulates a BaseStateClient instance,
     * which represents the corresponding client-side state of the game.
     *
     * @return a PhaseEvent containing a BaseStateClient object that represents
     *         the client-side state associated with the current game state.
     */
    @Override
    public PhaseEvent toClientState() {
        return new PhaseEvent(new BaseStateClient());
    }
}
