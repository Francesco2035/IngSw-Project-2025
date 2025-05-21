package org.example.galaxy_trucker.View.ClientModel.States;

import org.example.galaxy_trucker.Model.PlayerStates.PlayerState;
import org.example.galaxy_trucker.View.ClientModel.PlayerClient;
import org.example.galaxy_trucker.View.TUI.Out;

public class LobbyClient extends PlayerStateClient {

    @Override
    public void showGame(Out out) {
        StringBuilder toPrint = new StringBuilder();
        toPrint.append("Lobby\n\n");
        toPrint.append(out.showLobby());
        out.render(toPrint);
        //out.printMessage(toPrint.toString());
//        out.printMessage("Lobby");
//        out.showLobby();
    }
}
