package org.example.galaxy_trucker.Controller;

import org.example.galaxy_trucker.ClientServer.RMI.ClientInterface;

import java.util.UUID;

public class DisconnectedClient {
    public UUID token;
    public ClientInterface client;

    public DisconnectedClient(UUID token, ClientInterface client) {
        this.token = token;
        this.client = client;
    }



}
