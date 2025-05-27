package org.example.galaxy_trucker.View.ClientModel.States;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.galaxy_trucker.View.TUI.Out;



public class BuildingClient  extends PlayerStateClient{


    @JsonProperty("type")
    private final String type = "Building";

    @Override
    public void showGame(Out out) {
        synchronized (out.getLock()){

            StringBuilder toPrint = new StringBuilder();

            toPrint.append("Building\n\n");
            toPrint.append(out.showPlayers());
            toPrint.append(out.printGameboard());
            toPrint.append(out.printHand());
            toPrint.append(out.showCovered());
            toPrint.append(out.showUncoveredTiles());
            toPrint.append(out.printBoard());
            out.render(toPrint);
            //out.printMessage(toPrint.toString());
//            out.printMessage("Building...");
//            //out.showPlayers();
//            out.printGameboard();
//            out.printHand();
//            out.showUncoveredTiles();
//            out.showCovered();
//            out.printBoard();
        }
    }
}
