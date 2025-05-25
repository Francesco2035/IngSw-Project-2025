package org.example.galaxy_trucker.View.ClientModel.States;

import org.example.galaxy_trucker.View.TUI.Out;

public class DefendingFromLargeClient  extends PlayerStateClient{

    @Override
    public void showGame(Out out) {
//TODO: messaggi della carta
        StringBuilder sb = new StringBuilder();
        sb.append("Defending From Large...\n");
        sb.append(out.showPlayers());
        sb.append(out.printGameboard());
        sb.append(out.printBoard());
//        out.showPlayers();
//        out.printGameboard();
//        out.showCard();
//        out.printBoard();
        out.render(sb);
    }
}
