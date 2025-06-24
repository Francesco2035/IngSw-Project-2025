package org.example.galaxy_trucker.ClientServer.RMI;

import org.example.galaxy_trucker.Messages.Event;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientInterface extends Remote, Serializable {


    void StartClient() throws IOException, NotBoundException;

    void receiveMessage(Event event) throws RemoteException;

    void receivePing() throws  RemoteException;

    void receiveToken(String token) throws RemoteException;
}
