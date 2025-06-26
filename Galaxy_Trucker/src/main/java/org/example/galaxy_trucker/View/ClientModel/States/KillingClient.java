package org.example.galaxy_trucker.View.ClientModel.States;

import org.example.galaxy_trucker.View.GUI.GuiOut;
import org.example.galaxy_trucker.View.TUI.Out;

import java.util.ArrayList;
import java.util.List;

/**
 * The KillingClient class represents a specific player state where the player
 * is performing actions related to "killing". This class extends the abstract
 * PlayerStateClient and provides implementations for displaying the game state
 * and retrieving available commands during this state.
 */
public class KillingClient extends PlayerStateClient{

    /**
     * Default constructor for the KillingClient class.
     *
     * This constructor initializes an instance of the KillingClient state,
     * representing the game state where the player performs actions related to "killing."
     * It provides specific logic for rendering the game state and retrieving relevant commands
     * during the "killing" phase.
     */
    public KillingClient() {

    }

    /**
     * Displays the current game state related to the "killing" phase.
     * This method assembles various game components such as the title card, player details,
     * game board, cards, and additional player or board information. Once all components
     * are gathered, the complete formatted game state is rendered using the provided output.
     *
     * @param out the output interface used to generate and display the game state.
     *            It provides methods to retrieve the necessary game components
     *            and renders the final assembled game state.
     */
    @Override
    public void showGame(Out out) {
        StringBuilder toPrint = new StringBuilder();
        toPrint.append(out.getTitleCard());
        toPrint.append("Killing...\n");
        toPrint.append(out.showPlayers());
        toPrint.append(out.printGameBoard());
        toPrint.append(out.showCard());
        toPrint.append(out.showPbInfo());
        toPrint.append(out.printBoard());
        toPrint.append(out.showException());
        out.render(toPrint);
    }

    /**
     * Retrieves the list of available commands specific to the "Killing" game state.
     *
     * @return an ArrayList of Strings containing the command "Kill".
     */
    @Override
    public ArrayList<String> getCommands() {
        return new ArrayList<>(List.of("Kill"));
    }


    /**
     * Displays the current game state related to the "killing" phase using a graphical user interface.
     * This method invokes the graphical rendering specific to the "killing" phase through the root GUI element.
     *
     * @param out the graphical output interface used to render the game state. It provides
     *            methods and access to GUI elements required for displaying the current state.
     */
    public void showGame(GuiOut out){
        out.getRoot().killing();
    }
}
