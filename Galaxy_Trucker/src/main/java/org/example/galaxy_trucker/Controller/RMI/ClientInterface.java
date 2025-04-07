package org.example.galaxy_trucker.Controller.RMI;

import org.example.galaxy_trucker.Model.Game;
import org.example.galaxy_trucker.Model.Player;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientInterface extends Remote {

    public Player getPlayer() throws RemoteException;
    public Game getGame() throws RemoteException;

    public void setGame(Game game) throws RemoteException;
    public void setPlayerId(String id) throws RemoteException;

}
