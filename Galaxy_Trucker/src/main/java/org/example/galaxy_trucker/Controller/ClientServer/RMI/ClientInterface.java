package org.example.galaxy_trucker.Controller.ClientServer.RMI;

import org.example.galaxy_trucker.Model.Game;
import org.example.galaxy_trucker.Model.Player;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientInterface extends Remote {

    void StartClient() throws IOException, NotBoundException;

    public Player getPlayer() throws RemoteException;
    public Game getGame() throws RemoteException;

    public void setGame(Game game) throws RemoteException;
    public void setPlayerId(String id) throws RemoteException;

}
