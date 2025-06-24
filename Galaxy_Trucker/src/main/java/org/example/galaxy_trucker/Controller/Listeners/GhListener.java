package org.example.galaxy_trucker.Controller.Listeners;

import org.example.galaxy_trucker.Commands.QuitCommand;
import org.example.galaxy_trucker.Controller.Messages.LobbyEvent;
import org.example.galaxy_trucker.Model.Player;

import java.awt.desktop.QuitEvent;

public interface GhListener {
    public void sendEvent(LobbyEvent event);

    public void updateLobby(LobbyEvent event);

    public void quitPlayer(QuitCommand event);
}
