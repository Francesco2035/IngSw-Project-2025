package org.example.galaxy_trucker.Controller.Listeners;

import org.example.galaxy_trucker.ClientServer.Messages.PlayerBoardEvents.PlayerTileEvent;

public interface PlayersPBListener {


    public void receivePBupdate(PlayerTileEvent event);
}
