package org.example.galaxy_trucker.Controller.Listeners;

import org.example.galaxy_trucker.ClientServer.Messages.GameBoardEvent;

public interface GameBoardListener {

    void gameBoardChanged(GameBoardEvent event);
}
