package org.example.galaxy_trucker.View.ClientModel.States;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.galaxy_trucker.View.GUI.GuiOut;
import org.example.galaxy_trucker.View.TUI.Out;

import java.util.ArrayList;
import java.util.List;

/**
 * The AddCrewClient class is responsible for handling the "Add Crew" state
 * during the game. It extends the PlayerStateClient class and provides
 * functionality to display the state and process commands specific to adding crew members.
 */
public class AddCrewClient  extends PlayerStateClient{

    /**
     * Default constructor for the AddCrewClient class.
     * Initializes an instance of the AddCrewClient state, which facilitates the "Add Crew" phase
     * in the game. This state allows players to add crew members or aliens to their game setup.
     */
    public AddCrewClient() {

    }

    /**
     * A constant string that represents the type of the AddCrewClient state.
     * This is a serialized field using the @JsonProperty annotation with the value "type".
     * It is used to identify the current state as "AddCrew" within the application or during
     * serialization/deserialization processes.
     */
    @JsonProperty("type")
    private final String type = "AddCrew";

    /**
     * Displays the current state of the game during the "Add Crew" phase by rendering
     * relevant information using the provided output handler.
     *
     * @param out the output handler responsible for rendering textual representation of the game's state
     */
    @Override
    public void showGame(Out out) {
        StringBuilder toPrint = new StringBuilder();
        toPrint.append("AddCrew\n\n");
        toPrint.append(out.showPlayers());
        toPrint.append(out.printGameBoard());
        toPrint.append(out.showPbInfo());
        toPrint.append(out.printBoard());
        toPrint.append(out.showException());
        out.render(toPrint);

    }

    /**
     * Displays the "Add Crew" screen in the graphical user interface.
     * This method transitions the GUI to show the "Add Crew" scene, which is
     * managed by the GuiOut class, and ensures the corresponding screen is rendered.
     *
     * @param out the GuiOut instance responsible for managing and rendering GUI scenes.
     */
    public void showGame(GuiOut out){
        out.getRoot().AddCrewScene();
        out.printAddCrewScreen();
    }
    /**
     * Retrieves the list of available commands specific to the "AddCrew" game state.
     *
     * @return an ArrayList of Strings containing the commands "AddCrew", "AddPurpleAlien", and "AddBrownAlien".
     */
    @Override
    public ArrayList<String> getCommands() {
        return new ArrayList<>(List.of("AddCrew", "AddPurpleAlien", "AddBrownAlien"));
    }
}
