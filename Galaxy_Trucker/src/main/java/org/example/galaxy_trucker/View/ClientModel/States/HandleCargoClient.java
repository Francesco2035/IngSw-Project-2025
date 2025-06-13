package org.example.galaxy_trucker.View.ClientModel.States;

import org.example.galaxy_trucker.View.TUI.Out;

import java.util.List;

public class HandleCargoClient  extends PlayerStateClient{
    @Override
    public void showGame(Out out) {
        StringBuilder toPrint = new StringBuilder();
        toPrint.append(out.getTitleCard());
        toPrint.append("HandleCargo...\n\n");
        toPrint.append(out.showPlayers());
        toPrint.append(out.printGameBoard());
        toPrint.append(out.showRewards());
        toPrint.append(out.showPbInfo());
        toPrint.append(out.printBoard());
        toPrint.append(out.showException());
        out.render(toPrint);
    }

    @Override
    public List<String> getCommands() {
        return List.of("GetReward","Switch", "DiscardCargo", "FinishCargo");
    }

    //tutti questi comandi fino a finishCargo

    //GetRewards r1 (indice del reward) x1 y1
    //DiscardCargo x1 y1 p1
    //Switch x1 y1 p1 (indice cargo) x2 y2 p2
    //FinishCargo
}
