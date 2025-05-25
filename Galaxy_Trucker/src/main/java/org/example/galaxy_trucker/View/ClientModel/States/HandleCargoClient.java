package org.example.galaxy_trucker.View.ClientModel.States;

import org.example.galaxy_trucker.View.TUI.Out;

public class HandleCargoClient  extends PlayerStateClient{
    @Override
    public void showGame(Out out) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(out.showPlayers());
        stringBuilder.append(out.printGameboard());
        stringBuilder.append(out.showRewards());
        stringBuilder.append(out.printBoard());
    }
}
