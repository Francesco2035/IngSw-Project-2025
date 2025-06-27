package org.example.galaxy_trucker.View.ClientModel.States;

import org.example.galaxy_trucker.View.TUI.ASCII_ART;
import org.example.galaxy_trucker.View.TUI.Out;

import java.util.ArrayList;
import java.util.List;

/**
 * The SeeLog class is a concrete implementation of the PlayerStateClient abstract class.
 * It represents a state in the game where the player can view the game log. The main
 * functionality of this class is to display the game logs and provide state-specific
 * behavior for this phase of the game.
 *
 * Responsibilities:
 * - Render the game log surrounded by a border using ASCII art.
 * - Return an empty list of commands, indicating no actions are available in this state.
 */
public class SeeLog extends PlayerStateClient{


    /**
     * Displays the game log surrounded by a border using ASCII art and renders it.
     *
     * @param out An instance of the Out class responsible for rendering and providing
     *            the game log content to be displayed.
     */
    @Override
    public void showGame(Out out){
        StringBuilder toPrint = new StringBuilder();
        toPrint.append(ASCII_ART.Border);
        toPrint.append(out.showLog());
        toPrint.append(ASCII_ART.Border);
        out.render(toPrint);
    }



    /**
     * Retrieves the list of available commands for the current state.
     *
     * @return an ArrayList of Strings containing the available commands,
     *         which is empty in this implementation, indicating no actions are available.
     */
    @Override
    public ArrayList<String> getCommands() {
        return new ArrayList<>(List.of());
    }
}
