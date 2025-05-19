package org.example.galaxy_trucker.View.ClientModel.States;

import org.example.galaxy_trucker.View.TUI.Out;

public class BaseStateClient extends PlayerStateClient{

    @Override
    public void showGame(Out out) {
        out.printMessage("BaseState");
        out.showPlayers();
        out.printBoard();
    }
}
