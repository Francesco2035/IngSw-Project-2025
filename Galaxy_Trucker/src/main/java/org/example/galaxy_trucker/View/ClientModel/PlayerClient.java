package org.example.galaxy_trucker.View.ClientModel;

import org.example.galaxy_trucker.View.ClientModel.States.PlayerStateClient;
import org.example.galaxy_trucker.View.GUI.GuiOut;
import org.example.galaxy_trucker.View.TUI.CommandCompleter;
import org.example.galaxy_trucker.View.TUI.DynamicCompleter;
import org.example.galaxy_trucker.View.TUI.Out;
import org.jline.reader.Completer;

/**
 * Represents a player client in the game, managing the player's state and dynamic command completion.
 * This class acts as a bridge between the game state logic (PlayerStateClient) and output mechanisms
 * (Out and GuiOut). It allows interaction with the player's current state and provides
 * support for dynamic command completion.
 */
public class PlayerClient {


    /**
     * Represents the current state of the player within the game.
     *
     * This variable acts as a reference to the player's state, which is an instance
     * of a subclass of the abstract `PlayerStateClient`. The state determines the
     * behavior, actions, and commands available to the player during a specific phase
     * of the game. It is dynamically updated as the game progresses to reflect changes
     * in the player's situation.
     *
     * The associated state supports polymorphic behavior through the use of concrete
     * implementations of `PlayerStateClient`, each representing a specific game state
     * such as choosing a planet, defending, or managing resources. The state is responsible
     * for rendering game outputs and providing the available commands.
     *
     * This variable interacts with other components such as output mechanisms (`Out` and
     * `GuiOut`) to display the game state and supports dynamic updates to commands and
     * behaviors through the containing `PlayerClient` class.
     */
    private PlayerStateClient state;
    /**
     * Represents a dynamic command completer for the {@code PlayerClient}.
     * This variable is responsible for managing and providing command suggestions
     * based on the player's current state within the game. The {@code completer}
     * adapts dynamically to the player's context, allowing for flexible
     * command completion.
     *
     * The associated {@code DynamicCompleter} interface allows setting and managing
     * a list of available commands, which are typically derived from the
     * {@code PlayerStateClient}'s behavior. The {@code completer} enables
     * interactive and context-sensitive command suggestions for a smoother
     * gameplay experience.
     */
    private DynamicCompleter completer;


    /**
     * Displays the current game state to the specified output.
     *
     * @param out the output mechanism to which the game state will be displayed
     */
    public void showGame(Out out){
        state.showGame(out);
    }

    /**
     * Displays the game state using the provided graphical user interface output object.
     *
     * This method delegates the responsibility of rendering the current game state to
     * the `state` object, which utilizes the given `GuiOut` instance for managing the
     * graphical scenes associated with the game.
     *
     * @param out the GuiOut object responsible for managing and displaying graphical scenes.
     */
    public void showGame(GuiOut out){
        state.showGame(out);
    }

    /**
     * Sets the current state of the player in the game.
     *
     * This method assigns a new state to the player by replacing the existing
     * state with the specified {@code playerState}. The state controls the behavior
     * and actions of the player within the game.
     *
     * @param playerState the new state to assign to the player. It must be an instance
     *                    of a subclass of {@code PlayerStateClient}, defining the specific
     *                    behavior and commands available in the player's current context.
     */
    public void setPlayerState(PlayerStateClient playerState) {
        this.state = playerState;
    }

    /**
     * Retrieves the current state of the player associated with the PlayerClient instance.
     * The state represents the player's context and behavior within the game, and it is managed
     * by an instance of the PlayerStateClient or one of its concrete subclasses.
     *
     * @return an instance of PlayerStateClient representing the player's current state.
     *         The returned state may correspond to different game phases or actions available
     *         to the player, as implemented by specific subclasses of PlayerStateClient.
     */
    public PlayerStateClient getPlayerState() {
        return state;
    }

    /**
     * Sets the dynamic command completer for the player client. This completer is responsible
     * for dynamically modifying the available commands based on the current game state.
     *
     * @param completer the dynamic command completer to be associated with the player client
     */
    public void setCompleter(DynamicCompleter completer) {
        this.completer = completer;
    }

    /**
     * Retrieves the DynamicCompleter associated with the player client.
     * The DynamicCompleter is responsible for handling dynamic command
     * completions based on the current state of the game.
     *
     * @return the DynamicCompleter instance associated with this PlayerClient
     */
    public DynamicCompleter getCompleter() {
        return this.completer;
    }

}
