package org.example.galaxy_trucker.Controller.Listeners;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.galaxy_trucker.Controller.Messages.TileSets.CoveredTileSetEvent;
import org.example.galaxy_trucker.Controller.Messages.TileSets.UncoverdTileSetEvent;

import java.rmi.RemoteException;

public interface TileSestListener {

    public void tilesSetChanged(CoveredTileSetEvent event);

    public void tilesSetChanged(UncoverdTileSetEvent event) throws RemoteException;



}
