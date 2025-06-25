package org.example.galaxy_trucker.View.ClientModel.States;

import org.example.galaxy_trucker.View.GUI.GuiOut;
import org.example.galaxy_trucker.View.TUI.Out;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the defensive state of a player when defending against a large attack,
 * extending the {@code PlayerStateClient} abstract class. This state allows the player
 * to interact with the game in scenarios specific to defending from larger threats.
 */
public class DefendingFromLargeClient  extends PlayerStateClient{

    /**
     * Default constructor for the DefendingFromLargeClient class.
     * Initializes a new instance of the DefendingFromLargeClient state, which represents
     * a phase in the game where the player is defending against a large-scale attack.
     */
    public DefendingFromLargeClient() {

    }

    /**
     * Displays the current game state for the "Defending From Large" phase.
     * Constructs and renders the necessary information about the game
     * to the specified output.
     *
     * @param out The output interface used to render the game state.
     *            It provides methods to retrieve game-related data,
     *            such as the title card, player information, and game board details.
     */
    @Override
    public void showGame(Out out) {
        StringBuilder toPrint = new StringBuilder();
        toPrint.append(out.getTitleCard());
        toPrint.append("Defending From Large...\n");
        toPrint.append(out.showPlayers());
        toPrint.append(out.printGameBoard());
        toPrint.append(out.showPbInfo());
        toPrint.append(out.printBoard());
        toPrint.append(out.showCardEffect());
        toPrint.append(out.showException());
        out.render(toPrint);
    }

    /**
     * Retrieves the list of available commands for the "Defending From Large" state.
     *
     * @return an ArrayList of Strings containing a single command: "DefendLarge".
     */
    @Override
    public ArrayList<String> getCommands() {
        return new ArrayList<>(List.of("DefendLarge"));
    }


    /**
     * Displays the game state for the defend phase. It invokes the defend method
     * of the GuiRoot instance, providing instructions and actions specific to
     * the "DefendLarge" command.
     *
     * @param out the {@code GuiOut} instance used to manage and interact with the game GUI.
     */
    public void showGame(GuiOut out){
        out.getRoot().defend("DefendLarge", "1) Choose a cannon to activate \n2) Choose the energy to consume\n3) Defend!");
    }

}
