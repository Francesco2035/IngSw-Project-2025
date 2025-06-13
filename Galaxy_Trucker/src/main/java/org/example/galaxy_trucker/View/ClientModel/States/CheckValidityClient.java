package org.example.galaxy_trucker.View.ClientModel.States;

import org.example.galaxy_trucker.View.GUI.GuiOut;
import org.example.galaxy_trucker.View.TUI.Out;

import java.util.List;

public class CheckValidityClient  extends PlayerStateClient{


    @Override
    public void showGame(Out out) {
        StringBuilder toPrint = new StringBuilder();
        toPrint.append(out.getTitleCard());
        toPrint.append("CheckValidity...");
        toPrint.append(out.showPlayers());
        toPrint.append(out.printGameBoard());
        toPrint.append(out.showPbInfo());
        toPrint.append(out.printBoard());
        toPrint.append(out.showException());
        out.render(toPrint);
    }

    @Override
    public void showGame(GuiOut out){
        out.getRoot().checkValidityScene();
        out.printCheckValidityScreen();
    }

    @Override
    public List<String> getCommands() {
        return List.of("RemoveTile");
    }
}
