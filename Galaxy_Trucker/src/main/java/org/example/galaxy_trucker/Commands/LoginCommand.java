package org.example.galaxy_trucker.Commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.galaxy_trucker.Controller.ClientServer.RMI.ClientInterface;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.PlayerState;

import java.io.Serializable;
import java.net.Socket;


public class LoginCommand extends Command implements Serializable {

    @JsonProperty("commandType")
    private final String commandType = "LoginCommand";

    private ClientInterface client;
    //da parsare
    //private Socket echoSocket;
    public LoginCommand(String gameId, String playerId, int lv, String title) {
        super(gameId, playerId, lv, title);
    }


    public LoginCommand(){

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
