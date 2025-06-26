package org.example.galaxy_trucker.View.ClientModel.States;

import org.example.galaxy_trucker.View.GUI.GuiOut;
import org.example.galaxy_trucker.View.TUI.ASCII_ART;
import org.example.galaxy_trucker.View.TUI.Out;

import java.util.ArrayList;
import java.util.List;

/**
 * The LoginClient class represents the initial state for a user in the game,
 * allowing them to log in and perform primary actions like joining a lobby,
 * creating a new game, or reconnecting to an existing session.
 * This class extends PlayerStateClient and overrides specific methods
 * to customize behaviors for the login state.
 */
public class LoginClient extends PlayerStateClient {


    /**
     * Default constructor for the LoginClient class.
     *
     * This constructor initializes an instance of the LoginClient state,
     * which represents the initial state where a user can log in or select
     * other primary actions such as joining a lobby, creating a game, or reconnecting.
     */
    public LoginClient() {

    }
    /**
     * Displays the game screen by rendering the game's title and border using the specified output.
     *
     * @param out The output object used to render the game content.
     */
    @Override
    public void showGame(Out out) {
        StringBuilder sb = new StringBuilder();
        sb.append(ASCII_ART.Title);
        sb.append(ASCII_ART.Border);
        out.render(sb);
    }

    /**
     * Displays the initial game screen by transitioning to the first scene.
     *
     * @param out the GuiOut instance responsible for managing and displaying GUI scenes.
     */
    @Override
    public void showGame(GuiOut out){
        out.getRoot().goToFirstScene();
    }

    /**
     * Retrieves the list of available commands for the current state.
     *
     * @return an ArrayList of Strings containing the commands "Lobby", "Create", "Join", and "Reconnect".
     */
    @Override
    public ArrayList<String> getCommands() {
        return new ArrayList<>(List.of("Lobby", "Create", "Join", "Reconnect"));
    }
}
