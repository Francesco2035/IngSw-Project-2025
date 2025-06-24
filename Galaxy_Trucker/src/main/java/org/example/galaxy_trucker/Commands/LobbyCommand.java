package org.example.galaxy_trucker.Commands;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
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


    @JsonCreator
    public LobbyCommand(@JsonProperty("title") String title) {
        super("-1","-1", -1, title, "",-1);
    }




    @Override
    public void execute(Player player) throws IOException {}

    @Override
    public boolean allowedIn(PlayerState playerState) {
        return playerState.allows(this);
    }

    @JsonIgnore
    @Override
    public ClientInterface getClient() {
        return client;
    }


    public void setClient(ClientInterface client) {
        this.client = client;
    }


}
