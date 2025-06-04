package org.example.galaxy_trucker.View.ClientModel.States;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.galaxy_trucker.View.GUI.GuiOut;
import org.example.galaxy_trucker.View.TUI.Out;

import java.util.List;

public class BaseStateClient extends PlayerStateClient{


    @JsonProperty("type")
    private final String type = "Base";

    public BaseStateClient(){

    }

    @Override
    public void showGame(Out out) {

        StringBuilder toPrint = new StringBuilder();
        toPrint.append("BaseState\n\n");
        //toPrint.append(out.printGameboard());
        toPrint.append(out.printBoard());
        toPrint.append(out.showException());
        out.render(toPrint);

    }

    public void showGame(GuiOut out){
        out.getRoot().buildLobbyGameScreen();
        out.printGameLobby();
    }

    @Override
    public List<String> getCommands() {
        return List.of("Ready", "SeeBoard", "NotReady","Quit");
    }

}
