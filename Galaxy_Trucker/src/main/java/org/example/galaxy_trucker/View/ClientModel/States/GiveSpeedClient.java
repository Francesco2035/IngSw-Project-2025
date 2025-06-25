package org.example.galaxy_trucker.View.ClientModel.States;

import org.example.galaxy_trucker.View.GUI.GuiOut;
import org.example.galaxy_trucker.View.TUI.Out;

import java.util.ArrayList;
import java.util.List;

/**
 * The GiveSpeedClient class is a client-side representation of the "Give Speed" state
 * in the game. It extends the abstract PlayerStateClient class and implements specific
 * behaviors for the "Give Speed" phase of the game. This state allows a player to activate
 * engines and manage speed-related actions.
 */
public class GiveSpeedClient  extends PlayerStateClient{

    /**
     * Default constructor for the GiveSpeedClient class.
     * Initializes an instance of the GiveSpeedClient state, representing the "Give Speed" phase
     * in the game where players manage speed-related operations by activating engines or taking
     * other speed-related actions.
     */
    public GiveSpeedClient(){

    }

    /**
     * Displays the current game state including player information, game board, and additional
     * information relevant to the "Give Speed" phase. Renders the output via the provided output handler.
     *
     * @param out the output handler used to construct and render the game state information
     */
    @Override
    public void showGame(Out out) {
        StringBuilder toPrint = new StringBuilder();
        toPrint.append(out.getTitleCard());
        toPrint.append("Giving speed...\n");
        toPrint.append(out.showPlayers());
        toPrint.append(out.printGameBoard());
        toPrint.append(out.showCard());
        toPrint.append(out.showPbInfo());
        toPrint.append(out.printBoard());
        toPrint.append(out.showException());
        out.render(toPrint);
    }

    /**
     * Retrieves the list of available commands specific to the "Give Speed" game state.
     *
     * @return an ArrayList of Strings containing the command "GiveSpeed".
     */
    @Override
    public ArrayList<String> getCommands() {
        return new ArrayList<>(List.of("GiveSpeed"));
    }

    /**
     * Displays the game state for the "Give Speed" phase using the graphical user interface.
     * It prompts the user to select engines they want to activate and configures the interface
     * for tile selection based on the given parameters.
     *
     * @param out the GuiOut instance responsible for managing and displaying GUI elements.
     */
    public void showGame(GuiOut out){
        out.getRoot().giveTiles("GiveSpeed", "Select the engines you want to activate!", false);
    }
}
