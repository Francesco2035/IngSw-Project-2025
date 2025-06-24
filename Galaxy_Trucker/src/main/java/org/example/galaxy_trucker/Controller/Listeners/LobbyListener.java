package org.example.galaxy_trucker.Controller.Listeners;

import org.example.galaxy_trucker.Controller.Messages.LobbyEvent;

public interface LobbyListener {
    public void sendEvent(LobbyEvent event);
}
