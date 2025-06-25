package org.example.galaxy_trucker.View.ClientModel.States;

import org.example.galaxy_trucker.View.GUI.GuiOut;
import org.example.galaxy_trucker.View.TUI.Out;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the client state for handling the "Give Attack" action in the game.
 * It extends the {@code PlayerStateClient} abstract class to define specific behavior for this action.
 *
 * The primary role of this class is to handle the visual and command functionalities
 * for the "Give Attack" phase of the game in both text-based and graphical user interfaces.
 */
public class GiveAttackClient  extends PlayerStateClient{

    /**
     * The GiveAttackClient class represents a specific client state in the game,
     * where the player performs the "Give Attack" action. It is responsible for
     * handling the behavior and presentation during this game phase.
     *
     * This class extends the {@code PlayerStateClient} abstract class and provides
     * implementations for showing the game state and managing commands, tailored
     * to the "Give Attack" phase.
     */
    public GiveAttackClient() {

    }

    /**
     * Displays the current state of the game and renders the output for the "Give Attack" phase.
     * Combines various components of the game state into a textual representation and invokes the rendering process.
     *
     * @param out the output handler responsible for generating and rendering the game state visuals
     */
    @Override
    public void showGame(Out out) {
        StringBuilder toPrint = new StringBuilder();
        toPrint.append(out.getTitleCard());
        toPrint.append("Giving attack...\n");
        toPrint.append(out.showPlayers());
        toPrint.append(out.printGameBoard());
        toPrint.append(out.showCard());
        toPrint.append(out.showPbInfo());
        toPrint.append(out.printBoard());
        toPrint.append(out.showException());
        out.render(toPrint);
    }

    /**
     * Retrieves the list of available commands for the "Give Attack" game state.
     *
     * @return an ArrayList of Strings containing the command "GiveAttack".
     */
    @Override
    public ArrayList<String> getCommands() {
        return new ArrayList<>(List.of("GiveAttack"));
    }

    /**
     * Displays the game state for the "Give Attack" phase using a graphical user interface.
     * This method invokes a GUI-specific mechanism to prompt players to select tiles for defending against an attack.
     *
     * @param out the {@code GuiOut} object responsible for managing and rendering GUI components in the application.
     */
    public void showGame(GuiOut out){
        out.getRoot().giveTiles("GiveAttack", "Select the cannons you want to use to defend!", false);
    }
}
