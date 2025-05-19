package org.example.galaxy_trucker.View.ClientModel.States;

import org.example.galaxy_trucker.View.TUI.Out;

public class ChoosingPlanetClient  extends PlayerStateClient{

    @Override
    public void showGame(Out out) {
        out.showPlayers();
        out.printGameboard();
        out.showCard();
        out.printBoard();
    }
}
