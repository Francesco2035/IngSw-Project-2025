package org.example.galaxy_trucker.View.ClientModel.States;

import org.example.galaxy_trucker.View.GUI.GuiOut;
import org.example.galaxy_trucker.View.TUI.Out;

import java.util.List;

public class LoginClient extends PlayerStateClient {

    @Override
    public void showGame(Out out) {

    }

    @Override
    public void showGame(GuiOut out){
        out.printGameLobby();
    }

    @Override
    public List<String> getCommands() {
        return List.of("");
    }
}
