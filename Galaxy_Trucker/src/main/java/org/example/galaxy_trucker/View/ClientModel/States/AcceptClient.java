package org.example.galaxy_trucker.View.ClientModel.States;

import org.example.galaxy_trucker.View.GUI.GuiOut;
import org.example.galaxy_trucker.View.TUI.Out;

import java.util.ArrayList;
import java.util.List;

/**
 * The AcceptClient class represents a specific state in the game where the client
 * can either accept or decline a particular action. This state is a subclass of
 * PlayerStateClient and implements its specific behavior by overriding necessary methods.
 */
public class AcceptClient  extends PlayerStateClient{


    /**
     * Default constructor for the AcceptClient class.
     * Initializes an instance of the AcceptClient state, which represents a game state
     * where the client is prompted to either accept or decline a specific action.
     */
    public AcceptClient() {

    }

    /**
     * Displays the current state of the game using the given output mechanism.
     * The method organizes a series of game-related data, such as the title card,
     * players, game board, and other associated information, and renders it
     * through the provided output instance.
     *
     * @param out the output mechanism used to display the game state.
     */
    @Override
    public void showGame(Out out) {
        StringBuilder toPrint = new StringBuilder();
        toPrint.append(out.getTitleCard());
        toPrint.append("Accepting...\n");
        toPrint.append(out.showPlayers());
        toPrint.append(out.printGameBoard());
        toPrint.append(out.showCard());
        toPrint.append(out.printBoard());
        toPrint.append(out.showException());
        out.render(toPrint);
    }



    /**
     * Retrieves the list of available commands for the current state.
     *
     * @return an ArrayList of Strings containing the commands "Accept" and "Decline"
     */
    @Override
    public ArrayList<String> getCommands() {
        return new ArrayList<>(List.of("Accept", "Decline"));
    }

    /**
     * Displays the "Accept or Decline" game state in the GUI.
     * This method updates the user interface to reflect a state where the user
     * can make a choice to "Accept" or "Decline" by interacting with the GUI elements.
     *
     * @param out the output interface responsible for managing GUI updates and transitioning scenes.
     */
    public void showGame(GuiOut out){
        out.getRoot().acceptState();
    }
}
