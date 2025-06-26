package org.example.galaxy_trucker.View.ClientModel.States;

import org.example.galaxy_trucker.View.GUI.GuiOut;
import org.example.galaxy_trucker.View.TUI.Out;

import java.util.ArrayList;
import java.util.List;

/**
 * The HandleTheftClient class extends the PlayerStateClient to represent the state
 * where the player is handling a theft scenario in the game. It provides functionality
 * to render the game state during theft handling and defines commands specific to this state.
 */
public class HandleTheftClient  extends PlayerStateClient{

    /**
     * Default constructor for the HandleTheftClient class.
     * Initializes an instance of the HandleTheftClient state, which represents
     * a game state where the player is handling a theft scenario.
     */
    public HandleTheftClient() {

    }

    /**
     * Renders the current game state for the theft handling scenario by displaying the title card,
     * game board, player information, cards, and other relevant details. Outputs the result
     * to the provided output interface.
     *
     * @param out The output interface responsible for rendering the game state, such as
     *            printing details or displaying information to the user.
     */
    @Override
    public void showGame(Out out) {
        StringBuilder toPrint = new StringBuilder();
        toPrint.append(out.getTitleCard());
        toPrint.append("Handling theft...\n");
        toPrint.append(out.showPlayers());
        toPrint.append(out.printGameBoard());
        toPrint.append(out.showCard());
        toPrint.append(out.showPbInfo());
        toPrint.append(out.printBoard());
        toPrint.append(out.showException());
        out.render(toPrint);
    }

    /**
     * Retrieves the list of available commands specific to the "Handle Theft" game state.
     *
     * @return an ArrayList of Strings containing the command "Theft".
     */
    @Override
    public ArrayList<String> getCommands() {
        return new ArrayList<>(List.of("Theft"));
    }


    /**
     * Displays the current game state during a theft scenario using the GUI.
     *
     * This method instructs the GUI to handle a theft event by interacting with the
     * associated GuiRoot object. It ensures that the theft scenario is reflected in
     * the user interface, including updates to the card image, theft status, and prompt message.
     *
     * @param out the GuiOut object used to render the current game state and update the UI.
     */
    public void showGame(GuiOut out){
        out.getRoot().handleTheft();
    }
}

