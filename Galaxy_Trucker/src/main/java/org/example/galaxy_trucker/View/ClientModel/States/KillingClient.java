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
     * Initializes an instance of the KillingClient state, which represents a game state
     * where the player is engaged in actions related to "killing". This state is responsible
     * for*/
    public KillingClient() {

    }

    /**
     * Displays the current state of the game by rendering various elements such as
     * the title card, player details, the game board, card information, and additional
     * game-specific messages. This method is invoked to update the game's visual
     * representation for the user.
     *
     * @param out the {@code*/
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
     * Displays the "killing" phase of the game using the provided graphical user interface (GUI) output.
     * This method is specific to the "killing" state and updates the game GUI to allow the user to
     * select crew members for elimination. The method invokes the killing logic of the current
     * GUI root to set up the appropriate prompts,*/
    public void showGame(GuiOut out){
        out.getRoot().killing();
    }
}
