package org.example.galaxy_trucker.Model.PlayerStates;
import org.example.galaxy_trucker.Commands.*;
import org.example.galaxy_trucker.ClientServer.Messages.PhaseEvent;
import org.example.galaxy_trucker.Model.Boards.Actions.*;
import org.example.galaxy_trucker.Model.Player;

/**
 * Represents an abstract state of a player in the game.
 *
 * This class serves as a base for various specific player states, each of which
 * can define distinct behaviors and permissions for allowed commands and actions.
 * Subclasses implement game-specific logic for evaluating commands and creating
 * default actions for their respective states.
 */
public abstract class PlayerState {


    /**
     * Determines whether the specified command is allowed in the current player state.
     *
     * @param command the command to be evaluated for permission
     * @return true if the command is allowed, false otherwise
     */
    public boolean allows(Command command) {
        System.out.println("Allows di default");
        return false;
    }

    /**
     * Determines whether the provided {@link AcceptCommand} is allowed in the current context.
     *
     * This method evaluates the {@link AcceptCommand} against the specific rules or conditions
     * defined by the {@code PlayerState}. The outcome indicates whether the command can be
     * executed or processed in this state.
     *
     * @param command the {@link AcceptCommand} to be evaluated
     * @return true if the command is allowed in the current player state, false otherwise
     */
    public boolean allows(AcceptCommand command) {
        return false;
    }

    /**
     * Determines if the provided AddCrewCommand is allowed in the current PlayerState.
     *
     * @param command the AddCrewCommand to check for allowance in the current state
     * @return true if the command is allowed, false otherwise
     */
    public boolean allows(AddCrewCommand command) {
        return false;
    }

    /**
     * Determines if the specified ChoosingPlanetsCommand is allowed in the current state.
     *
     * @param command the ChoosingPlanetsCommand to be checked for allowance
     * @return true if the command is allowed in the current state, false otherwise
     */
    public boolean allows(ChoosingPlanetsCommand command) {
        return false;
    }

    /**
     * Checks whether the specified ConsumeEnergyCommand is allowed in the current player state.
     *
     * This method determines the validity of executing the given ConsumeEnergyCommand based on the
     * rules and conditions defined by the player's state. It serves as a validation mechanism to ensure
     * the command can be executed within the context of the game.
     *
     * @param command the ConsumeEnergyCommand to be validated
     * @return true if the ConsumeEnergyCommand is allowed in the current player state, false otherwise
     */
    public boolean allows(ConsumeEnergyCommand command) {
        return false;
    }

    /**
     * Determines whether the given {@code DefendFromLargeCommand} is allowed in the current player state.
     *
     * @param command the {@code DefendFromLargeCommand} to be checked for allowance in the player's current state
     * @return {@code true} if the specified command is allowed in the current state, otherwise {@code false}
     */
    public boolean allows(DefendFromLargeCommand command) {
        return false;
    }

    /**
     * Determines whether the given DefendFromSmallCommand is allowed in the current
     * player state.
     *
     * @param command the DefendFromSmallCommand to be evaluated for permission within
     *                the current player state.
     * @return true if the DefendFromSmallCommand is allowed in the current state, false otherwise.
     */
    public boolean allows(DefendFromSmallCommand command) {
        return false;
    }

    /**
     * Determines whether the specified {@link FinishBuildingCommand} is allowed in the
     * current state of the player.
     *
     * @param command the {@code FinishBuildingCommand} to be checked for permission in the
     *                current player state
     * @return {@code true} if the command is allowed in the current state, otherwise {@code false}
     */
    public boolean allows(FinishBuildingCommand command) {
        return false;
    }

    /**
     * Determines whether the specified GiveAttackCommand is allowed in the current player state.
     *
     * @param command the GiveAttackCommand to evaluate; used to check if the command
     *                is permitted under the current state rules.
     * @return true if the command is allowed in the player's current state, false otherwise.
     */
    public boolean allows(GiveAttackCommand command) {
        return false;
    }

    /**
     * Determines if the specified `GiveSpeedCommand` is allowed in the current player state.
     *
     * @param command The `GiveSpeedCommand` to be evaluated for allowance.
     * @return {@code true} if the `GiveSpeedCommand` is permitted in the current player state; otherwise, {@code false}.
     */
    public boolean allows(GiveSpeedCommand command) {
        return false;
    }

    /**
     * Determines if the given {@code HandleCargoCommand} is allowed in the current player state.
     *
     * @param command the {@code HandleCargoCommand} to be evaluated
     * @return {@code true} if the command is allowed, {@code false} otherwise
     */
    public boolean allows(HandleCargoCommand command) {
        return false;
    }

    /**
     * Determines whether the specified TheftCommand is allowed in the current state.
     * This method evaluates the game rules or conditions defined in the state to decide
     * if the provided TheftCommand can be executed.
     *
     * @param command the TheftCommand to be evaluated for allowance in the current state
     * @return true if the TheftCommand is allowed in the current state, false otherwise
     */
    public boolean allows(TheftCommand command){
        return false;
    }

    /**
     * Determines whether the given {@code BuildingCommand} is allowed in the current state.
     *
     * @param command the {@code BuildingCommand} to be checked
     * @return {@code true} if the command is allowed, {@code false} otherwise
     */
    public boolean allows(BuildingCommand command) {
        return false;
    }

    /**
     * Determines if the given KillCommand is allowed in the current player state.
     *
     * @param command the KillCommand to check for permission in the current state
     * @return true if the command is allowed, false otherwise
     */
    public boolean allows(KillCommand command) {
        return false;
    }

    /**
     * Determines whether the provided `LoginCommand` is allowed in the current state.
     *
     * This method evaluates the rules and conditions associated with the current player state
     * and determines if the given `LoginCommand` can be executed.
     *
     * @param command the `LoginCommand` whose allowance is being checked
     * @return true if the command is allowed in the current state, false otherwise
     */
    public boolean allows(LoginCommand command) {
        return false;
    }

    /**
     * Determines whether the specified QuitCommand is allowed in the current player state.
     *
     * @param command the QuitCommand instance to be checked
     * @return true if the QuitCommand is allowed in the current state, false otherwise
     */
    public boolean allows(QuitCommand command){return false;}

    /**
     * Determines if the specified ReadyCommand is allowed in the current PlayerState.
     *
     * @param command the ReadyCommand to be evaluated
     * @return true if the ReadyCommand is permitted in the current PlayerState, false otherwise
     */
    public boolean allows(ReadyCommand command) {
        return false;
    }

    /**
     * Determines whether the specified {@code RemoveTileCommand} is allowed in the current player state.
     *
     * @param command the {@code RemoveTileCommand} to be checked
     * @return {@code true} if the command is permitted, {@code false} otherwise
     */
    public boolean allows(RemoveTileCommand command) {
        return false;
    }

    /**
     * Determines whether the provided SelectChunkCommand is allowed based on the current PlayerState.
     *
     * This method evaluates if the given command is valid and can be executed within the context
     * of the current PlayerState.
     *
     * @param command the SelectChunkCommand instance to evaluate
     * @return true if the command is allowed in the current PlayerState, false otherwise
     */
    public  boolean allows(SelectChunkCommand command){return false;};








    /**
     * Determines whether the specified AddCrewAction is allowed in the current player state.
     * The method checks if the action can be performed based on the player's current state
     * and applicable game rules.
     *
     * @param action the AddCrewAction to be evaluated, representing an attempt to add crew members
     *               (humans and/or aliens) to a housing unit.
     * @return true if the action is allowed in the current player state; false otherwise.
     */
    public boolean allows(AddCrewAction action) {
        return false;
    }

    /**
     * Determines whether the current state of the player allows executing the given AddGoodAction.
     *
     * @param action the AddGoodAction to be checked for permissibility in the current player state
     * @return true if the action is allowed in the current state, false otherwise
     */
    public boolean allows(AddGoodAction action) {
        return false;
    }

    /**
     * Determines if the GetEnginePower action is allowed in the current player state.
     *
     * This method evaluates whether the provided GetEnginePower action can be
     * executed based on the governing rules of the player's current state. It
     * returns a boolean indicating the permissibility of the action.
     *
     * @param action the GetEnginePower action to evaluate for allowance. This action
     *               aims to calculate and retrieve engine power information during gameplay.
     * @return true if the GetEnginePower action is allowed in the current state, false otherwise.
     */
    public boolean allows(GetEnginePower action) {
        return false;
    }

    /**
     * Determines if the specified GetGoodAction is allowed in the current player state.
     *
     * @param action the GetGoodAction to be evaluated
     * @return true if the action is allowed, false otherwise
     */
    public boolean allows(GetGoodAction action) {
        return false;
    }

    /**
     * Determines whether the specified GetPlasmaDrillPower action is allowed in the current PlayerState.
     *
     * @param action the GetPlasmaDrillPower action to be checked.
     * @return true if the action is allowed, false otherwise.
     */
    public boolean allows(GetPlasmaDrillPower action) {
        return false;
    }

    /**
     * Determines whether the specified KillCrewAction is allowed based on the current state of the player.
     *
     * @param action the KillCrewAction being evaluated for permissibility in the current player state
     * @return true if the specified action is allowed in the current state; false otherwise
     */
    public boolean allows(KillCrewAction action) {
        return false;
    }

    /**
     * Determines whether the specified UseEnergyAction is allowed based on the current state.
     *
     * @param action the UseEnergyAction to be evaluated
     * @return true if the action is allowed in the current state, false otherwise
     */
    public boolean allows(UseEnergyAction action) {
        return false;
    }

    /**
     * Determines if the specified DebugShip command is allowed in the current player state.
     *
     * @param command the DebugShip command to evaluate
     * @return true if the command is allowed in the current player state, false otherwise
     */
    public boolean allows(DebugShip command){return false;}

    /**
     * Creates a default command for the given game and player.
     * The specific command generated may depend on the state of the player and the game.
     *
     * @param gameId the identifier of the game for which the command is being created
     * @param player the player for whom the command is being created
     * @return a default instance of a {@code Command} appropriate for the given game and player
     */
    public abstract Command createDefaultCommand(String gameId,Player player);



    /**
     * This method resets the player's action state to indicate that the player has not acted.
     *
     * @param player the player object whose action state will be reset
     */
    public void shouldAct(Player player) {
        player.SetHasActed(false);
    }



    /**
     * Converts the current state of the player into a client-ready representation
     * encapsulated within a PhaseEvent object.
     *
     * This method is primarily used to translate the internal state of the PlayerState
     * into a client-facing structure (PhaseEvent) to facilitate communication and interaction
     * in the game architecture. The returned PhaseEvent object represents the specific game phase
     * and contains relevant data necessary for client-side functionalities.
     *
     * @return a PhaseEvent object that represents*/
    public abstract PhaseEvent toClientState();
}
