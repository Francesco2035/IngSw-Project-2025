package org.example.galaxy_trucker.View.ClientModel.States;

import org.example.galaxy_trucker.View.GUI.GuiOut;
import org.example.galaxy_trucker.View.TUI.Out;

import java.util.List;

public class GiveSpeedClient  extends PlayerStateClient{

    @Override
    public void showGame(Out out) {
        StringBuilder toPrint = new StringBuilder();
        toPrint.append(out.getTitleCard());
        toPrint.append("Giving speed...\n");
        toPrint.append(out.showPlayers());
        toPrint.append(out.printGameboard());
        toPrint.append(out.showCard());
        toPrint.append(out.showPbInfo());
        toPrint.append(out.printBoard());
        toPrint.append(out.showException());
        out.render(toPrint);
    }

    @Override
    public List<String> getCommands() {
        return List.of("GiveSpeed");
    }

    //giveSpeed x1 y1 ... motori doppi (ma va bene anche singoli)
    public void showGame(GuiOut out){
        out.getRoot().giveTiles("GiveSpeed", "Select the engines you want to activate!");
    }
}
