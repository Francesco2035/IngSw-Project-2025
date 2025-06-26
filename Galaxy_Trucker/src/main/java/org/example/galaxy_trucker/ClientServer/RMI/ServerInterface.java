package org.example.galaxy_trucker.ClientServer.RMI;

import org.example.galaxy_trucker.Commands.Command;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * This interface defines the contract for handling remote server operations in an RMI environment.
 * It facilitates communication between a server and its clients by providing methods to process client commands,
 * monitor connection status, and manage the server lifecycle.
 */
public interface ServerInterface extends Remote {

    /**
     * Processes a command received from a client in a remote server environment.
     *
     * @param cmd the Command object containing the operation to be executed.
     * @throws RemoteException if a remote communication error occurs during the processing of the command.
     */
    void command(Command cmd)throws RemoteException;

    /**
     * Starts the server and initializes necessary resources to handle client connections
     * and remote method invocations. This method sets up the server to be ready for
     * communication with remote clients in an RMI environment.
     *
     * @throws RemoteException if there is a communication-related issue during the start process.
     */
    void StartServer() throws RemoteException;


    /**
     * Processes a "pong" signal received from a client. This method is part of the
     * connection monitoring mechanism and is invoked by the client to confirm
     * its presence or responsiveness. Implementations of this method should handle
     * any logic required upon receiving a pong response from the client.
     *
     * @param clientInterface the client instance that sent the pong signal. This
     *                        parameter allows the server to identify the client
     *                        and perform any necessary operations related to the
     *                        client's state or connectivity.
     * @throws RemoteException if a communication-related exception occurs during
     *                         the remote invocation of this method.
     */
    void receivePong(ClientInterface clientInterface) throws RemoteException;
}
