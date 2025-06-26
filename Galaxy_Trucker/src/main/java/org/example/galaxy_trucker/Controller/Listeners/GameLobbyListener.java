package org.example.galaxy_trucker.Controller.Listeners;

import org.example.galaxy_trucker.ClientServer.Messages.GameLobbyEvent;

public interface GameLobbyListener {
    public void GameLobbyChanged(GameLobbyEvent e);
}
