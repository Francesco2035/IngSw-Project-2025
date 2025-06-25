package org.example.galaxy_trucker.View.ClientModel.States;

import org.example.galaxy_trucker.View.TUI.Out;

import java.util.ArrayList;
import java.util.List;

/**
 * The SeeBoardsClient class is a concrete implementation of the PlayerStateClient class,
 * representing a state in which the player views game boards. This state allows
 * specific interactions related to viewing and managing game board information.
 *
 * Features:
 * - Overrides methods to provide functionality specific to the "see boards" state.
 * - Facilitates rendering the game state and displays relevant data to the player.
 * - Offers a set of commands specific to this state.
 */
public class SeeBoardsClient extends PlayerStateClient{

    /**
     * Displays the game state by invoking the seeBoards method on the provided output interface.
     * This method is used to render the board information and provide a view of the current game state.
     *
     * @param out The output interface used to display the game state.
     */
    @Override
    public void showGame(Out out) {
        out.seeBoards();
    }

    /**
     * Retrieves the list of available commands for the current state.
     *
     * @return an ArrayList of Strings containing a single command, "Ok".
     */
    @Override
    public ArrayList<String> getCommands() {
        return new ArrayList<>(List.of("Ok"));
    }
}
