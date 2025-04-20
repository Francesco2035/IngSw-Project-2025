package org.example.galaxy_trucker.Controller.ClientServer.RMI;

import org.example.galaxy_trucker.Controller.Messages.Event;
import org.example.galaxy_trucker.Controller.Messages.HandEvent;
import org.example.galaxy_trucker.Controller.Messages.PlayerBoardEvents.TileEvent;
import org.example.galaxy_trucker.Model.Game;
import org.example.galaxy_trucker.Model.Player;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientInterface extends Remote {

    void StartClient() throws IOException, NotBoundException;


    void receiveMessage(Event message) throws RemoteException;

    void receiveMessage(HandEvent event) throws RemoteException;

    void receiveMessage(TileEvent event) throws RemoteException;
//    public Player getPlayer() throws RemoteException;
//    public Game getGame() throws RemoteException;
//
//    public void setGame(Game game) throws RemoteException;
//    public void setPlayerId(String id) throws RemoteException;

}
