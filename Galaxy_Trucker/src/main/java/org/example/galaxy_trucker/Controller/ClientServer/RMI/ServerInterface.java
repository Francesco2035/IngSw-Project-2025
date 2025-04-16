package org.example.galaxy_trucker.Controller.ClientServer.RMI;

import org.example.galaxy_trucker.Commands.Command;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerInterface extends Remote {

    public void command(Command cmd)throws RemoteException;

    void StartServer() throws RemoteException;

    public void login(ClientInterface client) throws RemoteException;

    public void JoinGame(ClientInterface joiner, String playerName, String GameName) throws RemoteException;

    public void CreateGame(ClientInterface joiner, String playerName, String GameName, int lv) throws RemoteException;
}
