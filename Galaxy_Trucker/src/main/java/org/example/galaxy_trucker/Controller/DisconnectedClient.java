package org.example.galaxy_trucker.Controller;

import org.example.galaxy_trucker.ClientServer.RMI.ClientInterface;

import java.util.UUID;

/**
 * The DisconnectedClient class represents a client that is currently disconnected
 * in the system. It contains the unique identifier (UUID) of the client and an
 * interface reference for client-specific operations.
 */
public class DisconnectedClient {
    /**
     * A unique identifier for the disconnected client.
     * This UUID serves as the token to represent the client and is used to
     * distinguish between different disconnected clients in the system.
     */
    public UUID token;
    /**
     * Represents a reference to a client that implements the ClientInterface. This client
     * typically interacts with the system through remote method invocation, enabling features
     * such as message reception, token handling, and client lifecycle management.
     *
     * The `ClientInterface` provides methods to start the client, receive notifications
     * (e.g., messages, tokens, pings), and handle communication with the system in a distributed
     * environment.
     */
    public ClientInterface client;

    /**
     * Constructs a new DisconnectedClient instance with the specified unique token
     * and client interface. This represents a client that is disconnected from the system.
     *
     * @param token The unique identifier (UUID) representing the client's session or identity.
     * @param client The client interface implementation used for client-specific operations.
     */
    public DisconnectedClient(UUID token, ClientInterface client) {
        this.token = token;
        this.client = client;
    }



}
