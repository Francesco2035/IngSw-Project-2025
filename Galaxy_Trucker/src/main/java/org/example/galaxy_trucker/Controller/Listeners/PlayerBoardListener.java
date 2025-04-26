package org.example.galaxy_trucker.Controller.Listeners;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.galaxy_trucker.Controller.Messages.PlayerBoardEvents.TileEvent;

public interface PlayerBoardListener {

    public void playerBoardChanged(TileEvent event) ;
}
