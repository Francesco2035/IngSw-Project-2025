package org.example.galaxy_trucker.View.ClientModel.States;

import org.example.galaxy_trucker.View.GUI.GuiOut;
import org.example.galaxy_trucker.View.TUI.Out;

import java.util.ArrayList;
import java.util.List;

/**
 * The ReadCardClient class represents the game state where the player has drawn
 * a new card. It extends the PlayerStateClient abstract class and provides specific
 * behaviors for managing this particular state in the game.
 *
 * In this state, the player is notified about the new card they have drawn, and
 * actions may be limited to observing this change before transitioning to the next
 * state.
 */
public class ReadCardClient extends PlayerStateClient{

    /**
     * Default constructor for the ReadCardClient class.
     *
     * This constructor initializes an instance of the ReadCardClient state.
     * This state represents the moment when a player has drawn a new card
     * and is notified about it within the game.
     */
    public ReadCardClient() {

    }

    /**
     * Renders the game state information to the output. This method composes
     * the game state view, including the title card, details of the newly drawn card,
     * and any associated exception information, and provides it to the specified output
     * renderer.
     *
     * @param out the output handler responsible for rendering the game state information.
     */
    public void showGame(Out out) {
        StringBuilder toPrint = new StringBuilder();
        toPrint.append(out.getTitleCard());
        toPrint.append("New card drawn \n\n\n");
        toPrint.append(out.showCard());
        toPrint.append("\n\n\n");
        toPrint.append(out.showException());
        out.render(toPrint);
    }

    /**
     * Displays the current state of the game using the graphical user interface (GUI) output.
     * This method is responsible for rendering the appropriate game-related information
     * to the GUI provided by the GuiOut object.
     *
     * @param out the GuiOut object used to display the game's graphical user interface.
     */
    public void showGame(GuiOut out) {
    }

    /**
     * Retrieves a list of available commands for the current state.
     *
     * @return an ArrayList of Strings containing the available commands. In this implementation,
     *         the list contains a single empty command represented by an empty string.
     */
    @Override
    public ArrayList<String> getCommands() {
        return new ArrayList<>(List.of(""));
    }

}
