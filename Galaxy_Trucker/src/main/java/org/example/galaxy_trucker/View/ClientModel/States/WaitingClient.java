package org.example.galaxy_trucker.View.ClientModel.States;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import org.example.galaxy_trucker.View.TUI.Out;

public class WaitingClient extends PlayerStateClient{

    @JsonProperty("type")
    private final String type = "Waiting";



    @Override
    public void showGame(Out out) {
        StringBuilder sb = new StringBuilder();
        sb.append("Waiting for players...\n");
        sb.append(out.showPlayers());
        sb.append(out.printGameboard());
        sb.append(out.showCard());
        sb.append(out.printBoard());
        out.render(sb);
    }
}
