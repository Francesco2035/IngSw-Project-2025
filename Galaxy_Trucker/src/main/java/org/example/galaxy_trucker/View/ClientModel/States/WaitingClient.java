package org.example.galaxy_trucker.View.ClientModel.States;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import org.example.galaxy_trucker.View.TUI.Out;

public class WaitingClient extends PlayerStateClient{

    @JsonProperty("type")
    private final String type = "Waiting";



    @Override
    public void showGame(Out out) {
        out.printMessage("\rWaiting for players...");
        out.showPlayers();
        out.printGameboard();
        out.showCard();
        out.printBoard();
    }
}
