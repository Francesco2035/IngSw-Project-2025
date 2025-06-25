package org.example.galaxy_trucker.View.ClientModel.States;

import org.example.galaxy_trucker.View.GUI.GuiOut;
import org.example.galaxy_trucker.View.TUI.Out;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a specific client-side state during the game where a player is required to choose a position.
 * This class extends the abstract PlayerStateClient class and provides the implementation
 * specific to handling the "Choose Position" state.
 */
public class ChoosePositionClient  extends PlayerStateClient{

    /**
     * Constructs a new instance of ChoosePositionClient.
     * This class represents the client-side state in the game where the player is required
     * to choose a position. It serves as a specific implementation of the abstract
     * PlayerStateClient class and handles functionality related to the "Choose Position" phase.
     */
    public ChoosePositionClient() {

    }

    /**
     * Displays the current game state to the user, providing all relevant information such as the title card,
     * player details, game board, game-specific information, and any exceptions, then renders the output.
     *
     * @param out the output handler responsible for rendering the game's state and managing output functionalities
     */
    @Override
    public void showGame(Out out) {
        StringBuilder toPrint = new StringBuilder();
        toPrint.append(out.getTitleCard());
        toPrint.append("ChoosePosition...\n");
        toPrint.append(out.showPlayers());
        toPrint.append(out.printGameBoard());
        toPrint.append(out.showPbInfo());
        toPrint.append(out.showException());
        out.render(toPrint);
    }



    /**
     * Retrieves the list of available commands for the "Choose Position" state.
     *
     * @return an ArrayList of Strings containing a single command "FinishBuilding".
     */
    @Override
    public ArrayList<String> getCommands() {
        return new ArrayList<>(List.of("FinishBuilding"));
    }


    /**
     * Displays the "Choose Position" user interface, allowing the player
     * to select a position for their ship. This method is overridden
     * to provide a graphical implementation by invoking the relevant
     * GUI logic within the {@link GuiOut} instance.
     *
     * @param out the {@link GuiOut} instance used to manage and render
     *            the graphical user interface for the current state.
     */
    @Override
    public void showGame(GuiOut out){
        out.getRoot().choosePosition();
    }
}
