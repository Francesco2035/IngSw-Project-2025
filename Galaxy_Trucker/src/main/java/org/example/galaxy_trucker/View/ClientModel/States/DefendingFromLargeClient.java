package org.example.galaxy_trucker.View.ClientModel.States;

import org.example.galaxy_trucker.View.TUI.Out;

import java.util.List;

public class DefendingFromLargeClient  extends PlayerStateClient{

    @Override
    public void showGame(Out out) {
//TODO: messaggi della carta
        StringBuilder toPrint = new StringBuilder();
        toPrint.append("Defending From Large...\n");
        toPrint.append(out.showPlayers());
        toPrint.append(out.printGameboard());
        toPrint.append(out.printBoard());
        toPrint.append(out.showException());
        out.render(toPrint);
    }

    @Override
    public List<String> getCommands() {
        return List.of("DefendFromLarge");
    }
}
