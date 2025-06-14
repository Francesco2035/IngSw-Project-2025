package org.example.galaxy_trucker.View.ClientModel.States;

import org.example.galaxy_trucker.View.TUI.Out;

import java.util.ArrayList;
import java.util.List;

public class ConsumingEnergyClient  extends PlayerStateClient{

    @Override
    public void showGame(Out out) {
        StringBuilder toPrint = new StringBuilder();
        toPrint.append(out.getTitleCard());
        toPrint.append("Consuming energy...\n");
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
        return new ArrayList<>(List.of("ConsumeEnergy"));
    }

    //consumeEnergy x1 y2 x2 y2 ... (clic su tile batteria)
    //pirates, sbugglers, slavers, openSpace, warzone
}
