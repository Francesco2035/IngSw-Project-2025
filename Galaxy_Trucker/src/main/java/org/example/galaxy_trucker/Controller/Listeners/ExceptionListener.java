package org.example.galaxy_trucker.Controller.Listeners;

import org.example.galaxy_trucker.ClientServer.Messages.ExceptionEvent;

public interface ExceptionListener  {
    void exceptionOccured(ExceptionEvent event);
}
