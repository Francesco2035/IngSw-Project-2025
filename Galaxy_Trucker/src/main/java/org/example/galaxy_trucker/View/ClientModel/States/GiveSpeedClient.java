package org.example.galaxy_trucker.View.ClientModel.States;

import org.example.galaxy_trucker.View.TUI.Out;

public class GiveSpeedClient  extends PlayerStateClient{

    @Override
    public void showGame(Out out) {
        StringBuilder sb = new StringBuilder();
        sb.append("Giving speed...\n");
        sb.append(out.showPlayers());
        sb.append(out.printGameboard());
        sb.append(out.showCard());
        sb.append(out.printBoard());
        out.render(sb);
    }
}
