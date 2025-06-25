package org.example.galaxy_trucker.View.ClientModel.States;

import org.example.galaxy_trucker.View.GUI.GuiOut;
import org.example.galaxy_trucker.View.TUI.ASCII_ART;
import org.example.galaxy_trucker.View.TUI.Out;

import java.util.ArrayList;
import java.util.List;

/**
 * The LobbyClient class represents a state where the player is in the lobby of the game.
 * It extends the PlayerStateClient abstract class and provides specific implementations
 * for rendering the game state when the player is in the lobby.
 */
public class LobbyClient extends PlayerStateClient {

    /**
     * Renders the current game state specific to the lobby context by composing
     * the lobby representation and any related exceptions, then passing it to the
     * specified output handler for rendering.
     *
     * @param out the output handler responsible for rendering the game state. It provides
     *            methods to generate and resolve the lobby and exception information.
     */
    @Override
    public void showGame(Out out) {
        System.out.println("render di "+ this.getClass().getName());
        StringBuilder toPrint = new StringBuilder();
        toPrint.append(ASCII_ART.Title);
        toPrint.append(ASCII_ART.Border);

        toPrint.append(out.showLobby());
        toPrint.append(out.showException());
        out.render(toPrint);

    }

    /**
     * Displays the lobby screen in the GUI.
     *
     * This method utilizes the `printLobby` method of the `GuiOut` class to
     * render the lobby screen. It is designed to update the GUI appropriately
     * to show the current lobby state.
     *
     * @param out the `GuiOut` instance responsible for rendering and managing
     *            the graphical user interface elements.
     */
    @Override
    public void showGame(GuiOut out){
        out.printLobby();
    }

    /**
     * Retrieves the list of available commands for the current state.
     *
     * @return an ArrayList of Strings containing the available commands. In this implementation,
     *         the list contains a single empty command represented by an empty string.
     */
    @Override
    public ArrayList<String> getCommands() {
        return new ArrayList<>(List.of(""));
    }
}
