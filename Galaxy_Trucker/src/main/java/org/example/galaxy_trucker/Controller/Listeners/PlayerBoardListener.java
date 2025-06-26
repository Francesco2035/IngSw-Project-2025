package org.example.galaxy_trucker.Controller.Listeners;

import org.example.galaxy_trucker.ClientServer.Messages.PBInfoEvent;
import org.example.galaxy_trucker.ClientServer.Messages.PlayerBoardEvents.TileEvent;

public interface PlayerBoardListener {

    public void playerBoardChanged(TileEvent event) ;

    public void PBInfoChanged(PBInfoEvent event) ;
}
