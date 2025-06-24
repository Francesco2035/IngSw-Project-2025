package org.example.galaxy_trucker.Controller.Listeners;

import org.example.galaxy_trucker.Messages.PBInfoEvent;
import org.example.galaxy_trucker.Messages.PlayerBoardEvents.TileEvent;

public interface PlayerBoardListener {

    public void playerBoardChanged(TileEvent event) ;

    public void PBInfoChanged(PBInfoEvent event) ;
}
