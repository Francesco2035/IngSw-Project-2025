package org.example.galaxy_trucker.View.ClientModel.States;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.galaxy_trucker.View.GUI.GuiOut;
import org.example.galaxy_trucker.View.TUI.Out;

import java.util.ArrayList;
import java.util.List;


public class BuildingClient  extends PlayerStateClient{



    public BuildingClient() {

    }

    @Override
    public void showGame(Out out) {
        synchronized (out.getLock()){

            StringBuilder toPrint = new StringBuilder();

            toPrint.append("Building\n\n");
            toPrint.append(out.showPlayers());
            toPrint.append(out.printGameBoard());
            toPrint.append(out.showCovered());
            toPrint.append(out.showUncoveredTiles());
            toPrint.append(out.showPbInfo());
            toPrint.append(out.printBoard());
            toPrint.append(out.printHand());
            toPrint.append(out.showDeck());
            toPrint.append(out.showHorglass());
            toPrint.append(out.showException());
            toPrint.append(out.showCardEffect());
            out.render(toPrint);

        }

    }

    @Override
    public void showGame(GuiOut out){
        out.getRoot().buildingScene();
        out.printBuildingScreen();
    }

    @JsonIgnore
    @Override
    public ArrayList<String> getCommands() {
        return new ArrayList<>(List.of("Discard", "PickTile", "FinishBuilding", "InsertTile", "SeeDeck", "FromBuffer", "ToBuffer"));
    }
}
