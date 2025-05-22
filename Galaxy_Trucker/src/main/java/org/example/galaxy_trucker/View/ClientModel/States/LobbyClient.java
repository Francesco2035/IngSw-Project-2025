package org.example.galaxy_trucker.View.ClientModel.States;

import org.example.galaxy_trucker.Model.PlayerStates.PlayerState;
import org.example.galaxy_trucker.View.ClientModel.PlayerClient;
import org.example.galaxy_trucker.View.GUI.GuiOut;
import org.example.galaxy_trucker.View.TUI.Out;

public class LobbyClient extends PlayerStateClient {

    @Override
    public void showGame(Out out) {
        out.printMessage("Lobby");
        out.showLobby();
    }

    @Override
    public void showGame(GuiOut out){
        out.printLobby();
    }
}
