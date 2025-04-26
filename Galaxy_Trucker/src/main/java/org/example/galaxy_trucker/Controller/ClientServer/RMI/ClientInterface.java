package org.example.galaxy_trucker.Controller.ClientServer.RMI;

import org.example.galaxy_trucker.Controller.Messages.Event;
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

    void receiveMessage(Event event) throws RemoteException;


}
