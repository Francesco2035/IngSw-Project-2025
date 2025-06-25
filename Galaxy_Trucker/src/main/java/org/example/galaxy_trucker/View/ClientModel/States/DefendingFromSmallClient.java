package org.example.galaxy_trucker.View.ClientModel.States;

import org.example.galaxy_trucker.View.GUI.GuiOut;
import org.example.galaxy_trucker.View.TUI.Out;

import java.util.ArrayList;
import java.util.List;

/**
 * The DefendingFromSmallClient class represents the player's state during a small-scale defense scenario
 * within the game. It extends the PlayerStateClient class and provides specific implementations
 * for rendering the game state and supplying available commands during this phase.
 */
public class DefendingFromSmallClient  extends PlayerStateClient{

    /**
     * Default constructor for the DefendingFromSmallClient class.
     * Initializes an instance of the DefendingFromSmallClient state, which represents
     * the game state when the player is defending against a small-scale threat.
     */
    public DefendingFromSmallClient() {

    }

    /**
     * Displays the game's current state for a small-scale defense scenario.
     *
     * @param out an instance of the {@code Out} interface used for rendering game state and details.
     */
    @Override
    public void showGame(Out out) {

        StringBuilder toPrint = new StringBuilder();
        toPrint.append(out.getTitleCard());
        toPrint.append("Defending From Small...\n");
        toPrint.append(out.showPlayers());
        toPrint.append(out.printGameBoard());
        toPrint.append(out.showPbInfo());
        toPrint.append(out.printBoard());
        toPrint.append(out.showCardEffect());
        toPrint.append(out.showException());
        out.render(toPrint);
    }

    /**
     * Retrieves the list of available commands for the "Defending From Small" state.
     *
     * @return an ArrayList of Strings containing the command "DefendSmall".
     */
    @Override
    public ArrayList<String> getCommands() {
        return new ArrayList<>(List.of("DefendSmall"));
    }


    /**
     * Displays the game interface for the current "Defend Small" phase using the provided GuiOut instance.
     * This method triggers a defense action by initializing the user interface with specific options
     * and prompts for the player to choose energy consumption during the defense process.
     *
     * @param out the GuiOut instance responsible for rendering the game's GUI components and transitions.
     */
    public void showGame(GuiOut out){
        out.getRoot().defend("DefendSmall", "Choose the energy to consume!");
    }
}
