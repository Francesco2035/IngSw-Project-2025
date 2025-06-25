package org.example.galaxy_trucker.View.ClientModel.States;

import org.example.galaxy_trucker.View.GUI.GuiOut;
import org.example.galaxy_trucker.View.TUI.Out;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the finish state of a player during the game. This class is a specific implementation
 * of the {@link PlayerStateClient}. The FinishStateClient is generally used to handle and display
 * the game's final results or outcomes once the game has concluded.
 */
public class FinishStateClient extends PlayerStateClient{


    /**
     * Displays the outcome of the game by rendering it through the provided output mechanism.
     *
     * @param out The output instance used for rendering the outcome of the game.
     */
    public void showGame(Out out) {
        StringBuilder toPrint = new StringBuilder();
        toPrint.append(out.showOutcome());
        toPrint.append(out.showScoreboard());
        toPrint.append("\n");
        out.render(toPrint);
    }

    /**
     * Displays the game state using the provided GuiOut instance.
     * This method is used to render the graphical representation of the game in the GUI.
     *
     * @param out the GuiOut instance responsible for managing and displaying
     *            scenes in the graphical user interface.
     */
    public void showGame(GuiOut out) {
    }



    /**
     * Retrieves the list of available commands for the current state.
     * This method provides a collection of commands that the user can execute
     * based on the specific state of the game.
     *
     * @return an ArrayList of Strings representing the available commands for the current state
     */
    @Override
    public ArrayList<String> getCommands() {
        return new ArrayList<>(List.of(""));
    }
}
