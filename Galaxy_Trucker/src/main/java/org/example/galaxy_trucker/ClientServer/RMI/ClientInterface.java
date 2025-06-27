package org.example.galaxy_trucker.ClientServer.RMI;

import org.example.galaxy_trucker.ClientServer.Messages.Event;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * The ClientInterface defines the contract for client-side operations
 * in a remote system using Java RMI. It allows the client to interact
 * with the remote server or other components via the methods specified
 * in this interface. This interface extends both the Remote and
 * Serializable interfaces to support remote invocation of methods
 * and ensure objects can be serialized during networking.
 *
 * Implementations of this interface must handle remote exceptions properly
 * and are expected to provide specific functionality for starting the client,
 * receiving server-sent events, handling pings for connection monitoring,
 * and processing tokens for session or authentication management.
 */
public interface ClientInterface extends Remote, Serializable {


    /**
     * Starts the client-side operations by establishing a connection to the remote server.
     * This method is pivotal for initiating the interaction between the client and the
     * remote system. It attempts to bind to the specified remote object hosted on the
     * server and prepares the client for subsequent remote method calls and communication.
     *
     * @throws IOException if a network-related error occurs while trying to connect to the remote server.
     * @throws NotBoundException if the specified name for the remote object is not currently bound in the remote registry.
     */
    void StartClient() throws IOException, NotBoundException;

    /**
     * Receives an event from the remote system and processes it accordingly.
     * This method allows the client to handle events sent by the remote server
     * or other components in the system. Implementations should define
     * the logic for processing specific types of events.
     *
     * @param event the event instance to be processed, which implements
     *              the {@link Event} interface.
     * @throws RemoteException if a communication-related exception occurs
     *                         during the remote method call.
     */
    void receiveMessage(Event event) throws RemoteException;

    /**
     * Handles the receipt of a ping signal from the remote server or another client.
     * This method is used to verify connectivity or ensure that the client's connection
     * remains active. Implementations of this method should address any necessary
     * logic required for responding to or processing the ping request.
     *
     * @throws RemoteException if a remote communication error occurs during the operation
     */
    void receivePing() throws  RemoteException;

    /**
     * Receives a token from a remote source. This token can be used for
     * session management, authentication, or other client-side processes
     * that depend on receiving tokens from the server.
     *
     * @param token the token string sent by the remote server
     * @throws RemoteException if a remote communication error occurs
     */
    void receiveToken(String token) throws RemoteException;
}
