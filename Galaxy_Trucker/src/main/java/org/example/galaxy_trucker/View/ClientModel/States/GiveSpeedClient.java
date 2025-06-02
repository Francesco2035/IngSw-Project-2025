package org.example.galaxy_trucker.View.ClientModel.States;

import org.example.galaxy_trucker.View.TUI.Out;

import java.util.List;

public class GiveSpeedClient  extends PlayerStateClient{

    @Override
    public void showGame(Out out) {
        StringBuilder toPrint = new StringBuilder();
        toPrint.append("Giving speed...\n");
        toPrint.append(out.showPlayers());
        toPrint.append(out.printGameboard());
        toPrint.append(out.showCard());
        toPrint.append(out.printBoard());
        toPrint.append(out.showException());
        out.render(toPrint);
    }

    @Override
    public List<String> getCommands() {
        return List.of("GiveSpeed");
    }
}
