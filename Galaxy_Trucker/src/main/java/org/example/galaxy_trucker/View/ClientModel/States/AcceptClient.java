package org.example.galaxy_trucker.View.ClientModel.States;

import org.example.galaxy_trucker.View.TUI.Out;

import java.util.ArrayList;
import java.util.List;

public class AcceptClient  extends PlayerStateClient{


    @Override
    public void showGame(Out out) {
        StringBuilder toPrint = new StringBuilder();
        toPrint.append(out.getTitleCard());
        toPrint.append("Accepting...\n");
        toPrint.append(out.showPlayers());
        toPrint.append(out.printGameBoard());
        toPrint.append(out.showCard());
        toPrint.append(out.printBoard());
        toPrint.append(out.showException());
        out.render(toPrint);
    }



    @Override
    public ArrayList<String> getCommands() {
        return new ArrayList<>(List.of("Accept", "Decline"));
    }
}
