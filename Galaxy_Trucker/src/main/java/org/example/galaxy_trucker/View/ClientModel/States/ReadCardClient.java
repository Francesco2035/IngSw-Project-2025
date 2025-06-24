package org.example.galaxy_trucker.View.ClientModel.States;

import org.example.galaxy_trucker.View.GUI.GuiOut;
import org.example.galaxy_trucker.View.TUI.Out;

import java.util.ArrayList;
import java.util.List;

public class ReadCardClient extends PlayerStateClient{

    public ReadCardClient() {

    }

    public void showGame(Out out) {
        StringBuilder toPrint = new StringBuilder();
        toPrint.append(out.getTitleCard());
        toPrint.append("New card drawn \n\n\n");
        toPrint.append(out.showCard());
        toPrint.append("\n\n\n");
        toPrint.append(out.showException());
        out.render(toPrint);
    }

    public void showGame(GuiOut out) {
    }

    @Override
    public ArrayList<String> getCommands() {
        return new ArrayList<>(List.of(""));
    }

}
