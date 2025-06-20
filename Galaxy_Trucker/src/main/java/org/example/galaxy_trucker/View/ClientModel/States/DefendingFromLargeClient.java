package org.example.galaxy_trucker.View.ClientModel.States;

import org.example.galaxy_trucker.View.GUI.GuiOut;
import org.example.galaxy_trucker.View.TUI.Out;

import java.util.ArrayList;
import java.util.List;

public class DefendingFromLargeClient  extends PlayerStateClient{

    @Override
    public void showGame(Out out) {
//TODO: messaggi della carta
        StringBuilder toPrint = new StringBuilder();
        toPrint.append(out.getTitleCard());
        toPrint.append("Defending From Large...\n");
        toPrint.append(out.showPlayers());
        toPrint.append(out.printGameBoard());
        toPrint.append(out.showPbInfo());
        toPrint.append(out.printBoard());
        toPrint.append(out.showCardEffect());
        toPrint.append(out.showException());
        out.render(toPrint);
    }

    @Override
    public ArrayList<String> getCommands() {
        return new ArrayList<>(List.of("DefendLarge"));
    }

    //defendLarge x y x2 y2 (cannone, energia)
    //doNothing
    public void showGame(GuiOut out){
        out.getRoot().defend("DefendLarge", "1) Choose a cannon to activate \n2) Choose the energy to consume\n3) Defend!");
    }

}
