package org.example.galaxy_trucker.View.ClientModel.States;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.galaxy_trucker.View.GUI.GuiOut;
import org.example.galaxy_trucker.View.TUI.Out;

import java.util.ArrayList;
import java.util.List;


/**
 * Represents a client state for the "Building" phase of the game.
 * This class extends PlayerStateClient and provides functionality specific
 * to interacting with the building phase, such as visualizing the game state
 * and retrieving commands relevant to this phase.
 */
public class BuildingClient  extends PlayerStateClient{



    /**
     * Default constructor for the BuildingClient class.
     * Initializes an instance of the BuildingClient state, which represents
     * a phase in the game where the client is engaged in building actions.
     */
    public BuildingClient() {

    }

    /**
     * Displays the current game state during the "Building" phase by synchronizing
     * access to the output object and rendering various game components through it.
     * The method generates a text representation of the game state and sends it
     * for rendering using the provided output mechanism.
     *
     * @param out the output object used to display the game state. It provides
     *            methods for retrieving relevant game data (e.g., players,
     *            game board, tiles, and deck) and ensures thread safety
     *            through synchronization.
     */
    @Override
    public void showGame(Out out) {
        synchronized (out.getLock()){
            StringBuilder toPrint = new StringBuilder();
            toPrint.append("Building\n\n");
            toPrint.append(out.showPlayers());
            toPrint.append(out.printGameBoard());
            toPrint.append(out.showCovered());
            toPrint.append(out.showUncoveredTiles());
            toPrint.append(out.showPbInfo());
            toPrint.append(out.printBoard());
            toPrint.append(out.printHand());
            toPrint.append(out.showDeck());
            toPrint.append(out.showHorglass());
            toPrint.append(out.showException());
            toPrint.append(out.showCardEffect());
            out.render(toPrint);

        }

    }

    /**
     * Displays the "Building" phase of the game using the GUI output.
     * This method updates the GUI to showcase the building scene
     * and provides the necessary visual representation for the player
     * to interact with during the building phase.
     *
     * @param out the GuiOut object that manages and displays
     *            the various GUI scenes, including the building scene.
     */
    @Override
    public void showGame(GuiOut out){
        out.getRoot().buildingScene();
        out.printBuildingScreen();
    }

    /**
     * Retrieves the list of available commands specific to the building phase of the game.
     * These commands represent the possible actions a player can take in this state.
     *
     * @return an ArrayList of Strings containing the commands: "Discard", "PickTile",
     *         "FinishBuilding", "InsertTile", "SeeDeck", "FromBuffer", and "ToBuffer".
     */
    @JsonIgnore
    @Override
    public ArrayList<String> getCommands() {
        return new ArrayList<>(List.of("Discard", "PickTile", "FinishBuilding", "InsertTile", "SeeDeck", "FromBuffer", "ToBuffer"));
    }
}
