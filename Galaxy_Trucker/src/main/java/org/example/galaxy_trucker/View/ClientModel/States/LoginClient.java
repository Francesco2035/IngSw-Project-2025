package org.example.galaxy_trucker.View.ClientModel.States;

import org.example.galaxy_trucker.View.GUI.GuiOut;
import org.example.galaxy_trucker.View.TUI.ASCII_ART;
import org.example.galaxy_trucker.View.TUI.Out;

import java.util.ArrayList;
import java.util.List;

public class LoginClient extends PlayerStateClient {


    public LoginClient() {

    }
    @Override
    public void showGame(Out out) {
        StringBuilder sb = new StringBuilder();
        sb.append(ASCII_ART.Title);
        sb.append(ASCII_ART.Border);
        out.render(sb);
    }

    @Override
    public void showGame(GuiOut out){
        out.getRoot().goToFirstScene();
    }

    @Override
    public ArrayList<String> getCommands() {
        return new ArrayList<>(List.of("Lobby", "Create", "Join", "Reconnect"));
    }
}
