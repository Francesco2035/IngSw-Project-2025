package org.example.galaxy_trucker.View.ClientModel.States;

import org.example.galaxy_trucker.View.GUI.GuiOut;
import org.example.galaxy_trucker.View.TUI.Out;

import java.util.ArrayList;
import java.util.List;

/**
 * The ChoosingPlanetClient class represents the client state where a player is in the process
 * of selecting a planet during the game.
 * This class extends PlayerStateClient and provides specific implementations for how the
 * "Choosing Planet" phase is displayed and the commands available during this state.
 */
public class ChoosingPlanetClient  extends PlayerStateClient{

    /**
     * Default constructor for the ChoosingPlanetClient class.
     * Initializes an instance of the ChoosingPlanetClient state, representing the phase
     * where the player selects a planet during the game.
     */
    public ChoosingPlanetClient() {

    }

    /**
     * Displays the current game state during the "Choosing Planet" phase.
     * This includes relevant game information such as the title card, players, game board,
     * cards, player board information, and any potential exceptions.
     *
     * @param out the {@code Out} object used to render the game information to the desired output.
     */
    @Override
    public void showGame(Out out) {
        StringBuilder toPrint = new StringBuilder();
        toPrint.append(out.getTitleCard());
        toPrint.append("Choosing Planet...\n");
        toPrint.append(out.showPlayers());
        toPrint.append(out.printGameBoard());
        toPrint.append(out.showCard());
        toPrint.append(out.showPbInfo());
        toPrint.append(out.printBoard());
        toPrint.append(out.showException());
        out.render(toPrint);
    }

    /**
     * Retrieves the list of available commands for the "Choosing Planet" state.
     *
     * @return an ArrayList of Strings containing the command "ChoosePlanet".
     */
    @Override
    public ArrayList<String> getCommands() {
        return new ArrayList<>(List.of("ChoosePlanet"));
    }

    /**
     * Displays the "Choosing Planet" phase of the game in the graphical user interface.
     * This method uses the GuiOut instance to invoke the appropriate method
     * in the GuiRoot class for initializing and displaying the UI for this phase.
     *
     * @param out the GuiOut object responsible for managing and transitioning between
     *            different graphical user interface views. It provides access to the
     *            root GUI component that handles the "Choosing Planet" functionality.
     */
    public void showGame(GuiOut out){
        out.getRoot().choosingPlanet();
    }
}
