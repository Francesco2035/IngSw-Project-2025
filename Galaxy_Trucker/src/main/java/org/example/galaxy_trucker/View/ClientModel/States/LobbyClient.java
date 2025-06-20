package org.example.galaxy_trucker.View.ClientModel.States;

import org.example.galaxy_trucker.View.GUI.GuiOut;
import org.example.galaxy_trucker.View.TUI.ASCII_ART;
import org.example.galaxy_trucker.View.TUI.Out;

import java.util.ArrayList;
import java.util.List;

public class LobbyClient extends PlayerStateClient {

    @Override
    public void showGame(Out out) {
        System.out.println("render di "+ this.getClass().getName());
        StringBuilder toPrint = new StringBuilder();
        toPrint.append(ASCII_ART.Title);
        toPrint.append(ASCII_ART.Border);

        toPrint.append(out.showLobby());
        toPrint.append(out.showException());
        out.render(toPrint);

    }

    @Override
    public void showGame(GuiOut out){
        out.printLobby();
    }

    @Override
    public ArrayList<String> getCommands() {
        return new ArrayList<>(List.of(""));
    }
}
