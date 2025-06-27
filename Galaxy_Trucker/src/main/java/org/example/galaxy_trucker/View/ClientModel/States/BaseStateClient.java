package org.example.galaxy_trucker.View.ClientModel.States;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.galaxy_trucker.View.GUI.GuiOut;
import org.example.galaxy_trucker.View.TUI.Out;

import java.util.ArrayList;
import java.util.List;

/**
 * The BaseStateClient class extends PlayerStateClient and represents
 * the base state of the client-side game logic. This class handles the
 * rendering and display logic of the game state to both console-based
 * and GUI-based outputs. It also provides a set of commands available
 * in this specific state.
 */
public class BaseStateClient extends PlayerStateClient{


    /**
     * Default constructor for the BaseStateClient class.
     * Initializes an instance of the BaseStateClient state, which represents the
     * base state of the game logic on the client side. This class controls how the
     * game state is rendered and displayed in different output formats, such as
     * console-based or GUI-based interfaces.
     */
    public BaseStateClient(){

    }

    /**
     * Renders and displays the current state of the game using the provided output handler.
     * The method generates and outputs information about the players, game board,
     * player-related information, and any exceptions that have occurred.
     *
     * @param out the output handler responsible for rendering the game state and managing the
     *            display of relevant information such as player details, game board, and exceptions.
     */
    @Override
    public void showGame(Out out) {
        StringBuilder toPrint = new StringBuilder();
        toPrint.append("BaseState\n\n");
        toPrint.append(out.showPlayers());
        toPrint.append(out.printGameBoard());
        toPrint.append(out.showPbInfo());
        toPrint.append(out.printBoard());
        toPrint.append(out.showException());
        out.render(toPrint);

    }

    /**
     * Displays the appropriate game screen based on the current game state.
     * If the game has started, the flight scene and base state are displayed.
     * Otherwise, the game lobby screen is displayed.
     *
     * @param out the GuiOut instance responsible for managing and rendering the GUI.
     */
    public void showGame(GuiOut out){
        if(out.getRoot().isGameStarted()){
            out.getRoot().flightScene();
            out.getRoot().baseState();
            out.printFlightScreen();
        }
        else{
            out.getRoot().LobbyGameScreen();
            out.printGameLobby();
        }
    }

    /**
     * Retrieves a list of available commands for the current state.
     *
     * @return an ArrayList of Strings containing the commands "Ready", "SeeBoard", "NotReady", and "Quit".
     */
    @JsonIgnore
    @Override
    public ArrayList<String> getCommands() {
        return new ArrayList<>(List.of("Ready", "SeeBoard", "NotReady", "Quit"));
    }

}
