package org.example.galaxy_trucker.View.ClientModel.States;

import org.example.galaxy_trucker.View.GUI.GuiOut;
import org.example.galaxy_trucker.View.TUI.Out;

import java.util.ArrayList;
import java.util.List;

/**
 * The CheckValidityClient class is a concrete implementation of the PlayerStateClient abstract class.
 * It is responsible for handling the "Check Validity" state in the game.
 */
public class CheckValidityClient  extends PlayerStateClient{

    /**
     * Default constructor for the CheckValidityClient class.
     * Initializes an instance of the CheckValidityClient state, which represents the "Check Validity" state in the game.
     * This state is responsible for ensuring the validity of player actions or game configurations.
     */
    public CheckValidityClient() {

    }

    /**
     * Displays the state of the game by rendering various components through the provided output object.
     *
     * @param out an instance of the Out class used to render the game state, including title card, players,
     *            game board, player and board information, and exceptions if present.
     */
    @Override
    public void showGame(Out out) {
        StringBuilder toPrint = new StringBuilder();
        toPrint.append(out.getTitleCard());
        toPrint.append("CheckValidity...");
        toPrint.append(out.showPlayers());
        toPrint.append(out.printGameBoard());
        toPrint.append(out.showPbInfo());
        toPrint.append(out.printBoard());
        toPrint.append(out.showException());
        out.render(toPrint);
    }

    /**
     * Displays the "Check Validity" game state in the GUI.
     *
     * This method ensures the GUI elements are updated to reflect the "Check Validity" state
     * by invoking the appropriate methods to transition the scene and render the screen.
     *
     * @param out the GuiOut instance used to manage and display the GUI scenes and updates.
     */
    @Override
    public void showGame(GuiOut out){
        out.getRoot().checkValidityScene();
        out.printCheckValidityScreen();
    }

    /**
     * Retrieves the list of available commands specific to the "CheckValidity" game state.
     *
     * @return an ArrayList of Strings containing a single command: "RemoveTile".
     */
    @Override
    public ArrayList<String> getCommands() {
        return new ArrayList<>(List.of("RemoveTile"));
    }
}
