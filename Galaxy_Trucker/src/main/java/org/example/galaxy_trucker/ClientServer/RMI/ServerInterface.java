package org.example.galaxy_trucker.ClientServer.RMI;

import org.example.galaxy_trucker.Commands.Command;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerInterface extends Remote {

    public void command(Command cmd)throws RemoteException;

    void StartServer() throws RemoteException;


    void receivePong() throws RemoteException;
}
