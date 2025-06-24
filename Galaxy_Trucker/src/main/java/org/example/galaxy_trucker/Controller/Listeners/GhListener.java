package org.example.galaxy_trucker.Controller.Listeners;

import org.example.galaxy_trucker.Commands.QuitCommand;
import org.example.galaxy_trucker.Messages.LobbyEvent;

public interface GhListener {
    public void sendEvent(LobbyEvent event);

    public void quitPlayer(QuitCommand event);
}
