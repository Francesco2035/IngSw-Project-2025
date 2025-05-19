package org.example.galaxy_trucker.View.ClientModel.States;

import org.example.galaxy_trucker.View.TUI.Out;

public class AddCrewClient  extends PlayerStateClient{

    @Override
    public void showGame(Out out) {
        out.printMessage("AddCrew");
        out.showPlayers();
        out.printGameboard();
        out.printBoard();
    }
}
