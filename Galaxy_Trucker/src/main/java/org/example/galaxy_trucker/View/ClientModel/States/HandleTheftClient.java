package org.example.galaxy_trucker.View.ClientModel.States;

import org.example.galaxy_trucker.View.GUI.GuiOut;
import org.example.galaxy_trucker.View.TUI.Out;

import java.util.ArrayList;
import java.util.List;

public class HandleTheftClient  extends PlayerStateClient{
    @Override
    public void showGame(Out out) {
        StringBuilder toPrint = new StringBuilder();
        toPrint.append(out.getTitleCard());
        toPrint.append("Handling theft...\n");
        toPrint.append(out.showPlayers());
        toPrint.append(out.printGameBoard());
        toPrint.append(out.showCard());
        toPrint.append(out.showPbInfo());
        toPrint.append(out.printBoard());
        toPrint.append(out.showException());
        out.render(toPrint);
    }

    @Override
    public ArrayList<String> getCommands() {
        return new ArrayList<>(List.of("Theft"));
    }

    //Theft x y p1
    //per scegliere quale cargo buttare ai pirati
    public void showGame(GuiOut out){
        out.getRoot().handleTheft();
    }
}

