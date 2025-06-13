package org.example.galaxy_trucker.View.ClientModel.States;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.galaxy_trucker.View.GUI.GuiOut;
import org.example.galaxy_trucker.View.TUI.Out;

import java.util.List;

public class AddCrewClient  extends PlayerStateClient{

    @JsonProperty("type")
    private final String type = "AddCrew";

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

    public void showGame(GuiOut out){
        out.getRoot().AddCrewScene();
        out.printAddCrewScreen();
    }
    @Override
    public List<String> getCommands() {
        return List.of("AddCrew", "AddPurpleAlien", "AddBrownAlien");
    }
}
