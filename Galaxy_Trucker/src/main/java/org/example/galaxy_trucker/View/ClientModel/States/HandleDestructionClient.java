package org.example.galaxy_trucker.View.ClientModel.States;

import org.example.galaxy_trucker.View.GUI.GuiOut;
import org.example.galaxy_trucker.View.TUI.Out;

import java.util.ArrayList;
import java.util.List;

/**
 * The HandleDestructionClient class is responsible for managing the state of handling
 * ship destruction in the game. It extends the PlayerStateClient class and
 * provides implementations for rendering the game's state and handling user commands
 * when the game requires a player to handle ship destruction.
 */
public class HandleDestructionClient  extends PlayerStateClient{

    /**
     * Default constructor for the HandleDestructionClient class.
     * Initializes an instance of the HandleDestructionClient state, which manages
     * the process of handling ship destruction during the game. This state is
     * responsible for presenting the relevant information and providing commands
     * related to ship destruction to the player.
     */
    public HandleDestructionClient() {

    }

    /**
     * Renders the current game state to the specified output object.
     *
     * @param out the output object responsible for rendering the game state
     */
    @Override
    public void showGame(Out out) {
        StringBuilder toPrint = new StringBuilder();
        toPrint.append(out.getTitleCard());
        toPrint.append("Handling destruction...\n");
        toPrint.append(out.showPlayers());
        toPrint.append(out.printGameBoard());
        toPrint.append(out.showCard());
        toPrint.append(out.showPbInfo());
        toPrint.append(out.printBoard());
        toPrint.append(out.showException());
        out.render(toPrint);
    }

    /**
     * Retrieves the list of available commands for the "Handling Destruction" game state.
     *
     * @return an ArrayList of Strings containing the single command "SelectChunk".
     */
    @Override
    public ArrayList<String> getCommands() {
        return new ArrayList<>(List.of("SelectChunk"));
    }


    /**
     * Displays a graphical interface for the user to select a tile from a ship chunk.
     * This method interacts with the `GuiOut` object to configure the tile selection interface,
     * displaying a prompt and enabling interactivity for tile selection.
     *
     * @param out the `GuiOut` instance used to manage and display the current GUI state,
     *            including showing the tile selection prompt and handling user input.
     */
    public void showGame(GuiOut out){
        out.getRoot().giveTiles("SelectChunk", "Select one tile from the \nship chunk you want to keep!", true);
    }
}
