package org.example.galaxy_trucker.View.ClientModel.States;

import org.example.galaxy_trucker.View.GUI.GuiOut;
import org.example.galaxy_trucker.View.TUI.Out;

import java.util.ArrayList;
import java.util.List;

/**
 * ConsumingEnergyClient represents a specific state of a player where energy is being consumed
 * in the game. This class extends the abstract PlayerStateClient and overrides methods to
 * define behavior specific to the "consuming energy" state.
 */
public class ConsumingEnergyClient  extends PlayerStateClient{

    /**
     * Default constructor for the ConsumingEnergyClient class.
     * Initializes an instance of ConsumingEnergyClient, which represents
     * a state where energy is being consumed within the game. This state
     * is specific to a player and is managed*/
    public ConsumingEnergyClient() {

    }

    /**
     * Renders the game's current state by appending various outputs and
     * rendering the resulting content through the provided output mechanism.
     *
     * @param out An instance of the Out class used to retrieve and render
     *            the state of the game, including title, players, board,
     *            cards, and exceptions.
     */
    @Override
    public void showGame(Out out) {
        StringBuilder toPrint = new StringBuilder();
        toPrint.append(out.getTitleCard());
        toPrint.append("Consuming energy...\n");
        toPrint.append(out.showPlayers());
        toPrint.append(out.printGameBoard());
        toPrint.append(out.showCard());
        toPrint.append(out.showPbInfo());
        toPrint.append(out.printBoard());
        toPrint.append(out.showException());
        out.render(toPrint);
    }

    /**
     * Retrieves the list of available commands for the current game state.
     *
     * @return an ArrayList of Strings containing the command "ConsumeEnergy".
     */
    @Override
    public ArrayList<String> getCommands() {
        return new ArrayList<>(List.of("ConsumeEnergy"));
    }

    /**
     * Displays the current state of the game on the GUI by triggering the consuming energy process.
     *
     * @param out*/
    public void showGame(GuiOut out){
        out.getRoot().consumingEnergy();
    }
}
