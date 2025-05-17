package org.example.galaxy_trucker.Commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.galaxy_trucker.ClientServer.RMI.ClientInterface;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.PlayerState;

import java.io.IOException;
import java.io.Serializable;

public class LobbyCommand extends Command implements Serializable {


    @JsonProperty("commandType")
    private final String commandType = "LobbyCommand";

    private ClientInterface client;


    public LobbyCommand(String title) {
        super.title = title;
    }


    @Override
    public void execute(Player player) throws IOException {

    }

    @Override
    public boolean allowedIn(PlayerState playerState) {
        return playerState.allows(this);
    }

    @Override
    public ClientInterface getClient() {
        return client;
    }


    public void setClient(ClientInterface client) {
        this.client = client;
    }


}
