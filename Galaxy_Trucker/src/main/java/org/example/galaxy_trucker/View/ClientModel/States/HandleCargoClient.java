package org.example.galaxy_trucker.View.ClientModel.States;

import org.example.galaxy_trucker.View.GUI.GuiOut;
import org.example.galaxy_trucker.View.TUI.Out;

import java.util.ArrayList;
import java.util.List;

/**
 * The HandleCargoClient class represents a specific state in the game where the player handles their cargo.
 * It extends the abstract class PlayerStateClient and provides implementations for displaying game information
 * and retrieving available commands during this state.
 */
public class HandleCargoClient  extends PlayerStateClient{

    /**
     * Default constructor for the HandleCargoClient class.
     * Initializes an instance of the HandleCargoClient state, which represents
     * the phase in the game where the player manages their cargo. This state allows
     * players to perform cargo-related actions such as retrieving rewards, switching,
     * discarding cargo, or finishing the cargo management process.
     */
    public HandleCargoClient() {

    }

    /**
     * Displays the current state of the game including players, game board, rewards, and other relevant information,
     * formatted as a string and rendered using the provided output mechanism.
     *
     * @param out an instance of the Out class responsible for formatting and rendering game-related information.
     */
    @Override
    public void showGame(Out out) {
        StringBuilder toPrint = new StringBuilder();
        toPrint.append(out.getTitleCard());
        toPrint.append("HandleCargo...\n\n");
        toPrint.append(out.showPlayers());
        toPrint.append(out.printGameBoard());
        toPrint.append(out.showRewards());
        toPrint.append(out.showPbInfo());
        toPrint.append(out.printBoard());
        toPrint.append(out.showException());
        out.render(toPrint);
    }

    /**
     * Retrieves the list of available commands specific to the "HandleCargo" game state.
     *
     * @return an ArrayList of Strings containing the commands "GetReward", "Switch",
     *         "DiscardCargo", and "FinishCargo".
     */
    @Override
    public ArrayList<String> getCommands() {
        return new ArrayList<>(List.of("GetReward","Switch", "DiscardCargo", "FinishCargo"));
    }

    /**
     * Displays the current game interface for the handling cargo phase using the provided GUI output system.
     *
     * @param out an instance of GuiOut responsible for handling and rendering the graphical user interface
     *            for the game. The method invokes the cargo-handling operation on the root GUI element
     *            associated with this instance.
     */
    public void showGame(GuiOut out){
        out.getRoot().handleCargo();
    }
}
