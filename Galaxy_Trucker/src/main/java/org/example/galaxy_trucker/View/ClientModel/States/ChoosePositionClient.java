package org.example.galaxy_trucker.View.ClientModel.States;

import org.example.galaxy_trucker.View.GUI.GuiOut;
import org.example.galaxy_trucker.View.TUI.Out;

import java.util.ArrayList;
import java.util.List;

public class ChoosePositionClient  extends PlayerStateClient{

    public ChoosePositionClient() {

    }

    @Override
    public void showGame(Out out) {
        StringBuilder toPrint = new StringBuilder();
        toPrint.append(out.getTitleCard());
        toPrint.append("ChoosePosition...\n");
        toPrint.append(out.showPlayers());
        toPrint.append(out.printGameBoard());
        toPrint.append(out.showPbInfo());
        toPrint.append(out.showException());
        out.render(toPrint);
    }



    @Override
    public ArrayList<String> getCommands() {
        return new ArrayList<>(List.of("FinishBuilding"));
    }


    @Override
    public void showGame(GuiOut out){
        out.getRoot().choosePosition();
    }
}
