package org.example.galaxy_trucker.Controller;

import org.example.galaxy_trucker.Controller.ClientServer.RMI.ClientInterface;

import java.util.UUID;

public class DisconnectedClient {
    public UUID token;
    public long deadline;
    public ClientInterface client;

    public DisconnectedClient(UUID token, long deadline, ClientInterface client) {
        this.token = token;
        this.deadline = deadline;
        this.client = client;
    }



}
