package org.example.galaxy_trucker.View.ClientModel.States;

import org.example.galaxy_trucker.Model.PlayerStates.PlayerState;
import org.example.galaxy_trucker.View.ClientModel.PlayerClient;
import org.example.galaxy_trucker.View.GUI.GuiOut;
import org.example.galaxy_trucker.View.TUI.Out;

import java.util.List;

public class LobbyClient extends PlayerStateClient {

    @Override
    public void showGame(Out out) {
        StringBuilder toPrint = new StringBuilder();
        toPrint.append("Lobby\n\n");
        toPrint.append(out.showLobby());
        toPrint.append(out.showException());
        out.render(toPrint);

    }

    @Override
    public void showGame(GuiOut out){
        out.printLobby();
    }

    @Override
    public List<String> getCommands() {
        return List.of("");
    }
}
