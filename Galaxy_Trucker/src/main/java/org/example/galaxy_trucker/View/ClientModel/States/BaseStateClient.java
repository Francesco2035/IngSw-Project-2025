package org.example.galaxy_trucker.View.ClientModel.States;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.galaxy_trucker.View.GUI.GuiOut;
import org.example.galaxy_trucker.View.TUI.Out;

public class BaseStateClient extends PlayerStateClient{


    @JsonProperty("type")
    private final String type = "Base";

    @Override
    public void showGame(Out out) {



        StringBuilder toPrint = new StringBuilder();
        toPrint.append("BaseState\n\n");
        toPrint.append(out.showPlayers());
        //toPrint.append(out.printGameboard());
        toPrint.append(out.printBoard());
        out.render(toPrint);
        //out.printMessage(toPrint.toString());
//        out.printMessage("BaseState");
//        out.showPlayers();
//        out.printBoard();
    }

    public void showGame(GuiOut out){
        out.printGameLobby();
    }

}
