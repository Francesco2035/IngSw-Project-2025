package org.example.galaxy_trucker.Commands;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.galaxy_trucker.Controller.ClientServer.Client;
import org.example.galaxy_trucker.Controller.ClientServer.RMI.ClientInterface;

import java.io.Serial;
import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ReconnectCommand extends Command implements Serializable {
    @JsonProperty("commandType")
    private final String commandType = "ReconnectCommand";

    @JsonIgnore
    private ClientInterface client;
    public ReconnectCommand(String token, String gameId, String playerId, int lv, String title) {
        super(gameId, playerId, lv, title, token);
    }
    public void execute() {
    }

    public ReconnectCommand() {

    }

    @Override
    public ClientInterface getClient() {
        return client;
    }


    public void setClient(ClientInterface client) {
        this.client = client;
    }


}
