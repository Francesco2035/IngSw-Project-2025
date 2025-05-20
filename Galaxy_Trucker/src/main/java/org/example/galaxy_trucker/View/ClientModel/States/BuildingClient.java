package org.example.galaxy_trucker.View.ClientModel.States;

import org.example.galaxy_trucker.View.TUI.Out;

public class BuildingClient  extends PlayerStateClient{

    @Override
    public void showGame(Out out) {
        out.printMessage("Building...");
        out.showPlayers();
        out.printGameboard();
        out.printHand();
        out.showUncoveredTiles();
        out.showCovered();
        out.printBoard();
    }
}
