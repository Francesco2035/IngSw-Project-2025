package org.example.galaxy_trucker.Controller.ClientServer.RMI;

import org.example.galaxy_trucker.Controller.Messages.HandEvent;
import org.example.galaxy_trucker.Controller.Messages.PlayerBoardEvents.TileEvent;
import org.example.galaxy_trucker.Controller.Messages.TileSets.CardEvent;
import org.example.galaxy_trucker.Controller.Messages.TileSets.CoveredTileSetEvent;
import org.example.galaxy_trucker.Controller.Messages.TileSets.UncoverdTileSetEvent;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface ClientInterface extends Remote {


    void StartClient() throws IOException, NotBoundException;

    void receiveMessage(HandEvent event) throws RemoteException;

    void receiveMessage(TileEvent event) throws RemoteException;

    void receiveMessage(UncoverdTileSetEvent event)throws RemoteException;

    void receiveMessage(CoveredTileSetEvent event)throws RemoteException ;

    void receiveDeck(ArrayList<CardEvent> deck)throws RemoteException;
//    public Player getPlayer() throws RemoteException;
//    public Game getGame() throws RemoteException;
//
//    public void setGame(Game game) throws RemoteException;
//    public void setPlayerId(String id) throws RemoteException;

}
