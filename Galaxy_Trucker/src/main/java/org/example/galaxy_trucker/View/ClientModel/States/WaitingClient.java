package org.example.galaxy_trucker.View.ClientModel.States;

import org.example.galaxy_trucker.View.TUI.Out;

public class WaitingClient extends PlayerStateClient{

    @Override
    public void showGame(Out out) {
        out.printMessage("Waiting for players...");
        out.showPlayers();
        out.printGameboard();
        out.showCard();
        out.printBoard();
    }
}
