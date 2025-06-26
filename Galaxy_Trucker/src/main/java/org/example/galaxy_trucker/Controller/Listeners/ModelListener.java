package org.example.galaxy_trucker.Controller.Listeners;

import org.example.galaxy_trucker.ClientServer.Messages.Event;

public interface ModelListener {
    public void sendEvent(Event event);
}
