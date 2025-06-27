package org.example.galaxy_trucker.View.ClientModel.States;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.galaxy_trucker.View.GUI.GuiOut;
import org.example.galaxy_trucker.View.TUI.Out;

import java.util.ArrayList;
import java.util.List;

/**
 * The WaitingClient class represents a state where the player is waiting for other players in the game.
 * This class is a concrete implementation of PlayerStateClient and provides specific functionality
 * for the "waiting" state during gameplay.
 *
 * In this state, the player has limited interaction capabilities, with the main focus on displaying
 * game information while waiting for other players to join or take their actions.
 */
public class WaitingClient extends PlayerStateClient{


    /**
     * Default constructor for the WaitingClient class.
     *
     * Initializes an instance of the WaitingClient state, representing a phase in the game
     * where the player is waiting for other players. This state focuses on providing
     * game information and limited interaction while waiting.
     */
    public WaitingClient() {

    }

    /**
     * Represents the type of the client state within the game context.
     * This variable is used to indicate that the current state is "Waiting".
     * It is a constant value and cannot be modified after initialization.
     */
    @JsonProperty("type")
    private final String type = "Waiting";



    /**
     * Displays the current game state while waiting for other players. This method constructs
     * a comprehensive view of the game using various components and renders the result.
     *
     * @param out The output handler used to display all relevant game information, including
     *            the title card, player details, game board, cards, and any applicable effects
     *            or exceptions.
     */
    @Override
    public void showGame(Out out) {
        StringBuilder toPrint = new StringBuilder();
        toPrint.append(out.getTitleCard());
        toPrint.append("Waiting for players...\n");
        toPrint.append(out.showPlayers());
        toPrint.append(out.printGameBoard());
        toPrint.append(out.showCard());
        toPrint.append(out.showPbInfo());
        toPrint.append(out.printBoard());
        toPrint.append(out.showCardEffect());
        toPrint.append(out.showException());
        out.render(toPrint);
    }

    /**
     * Retrieves the list of available commands for the current state.
     *
     * @return an ArrayList of Strings containing the available commands,
     *         which in this implementation includes "SeeBoard".
     */
    @Override
    public ArrayList<String> getCommands() {
        return new ArrayList<>(List.of("SeeBoard"));
    }

    /**
     * Displays the game state in the GUI, specifically indicating that the player
     * is in a waiting state. This changes the user interface accordingly to reflect
     * this status, disabling interactions and updating the visual presentation to
     * inform the user about the waiting phase.
     *
     * @param out the GuiOut object managing and rendering the graphical user interface elements.
     *            It provides access to the root GUI component for performing the waiting state updates.
     */
    public void showGame(GuiOut out){
        out.getRoot().waiting();
    }
}
