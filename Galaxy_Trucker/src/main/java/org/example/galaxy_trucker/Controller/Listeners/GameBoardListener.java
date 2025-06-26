package org.example.galaxy_trucker.Controller.Listeners;

import org.example.galaxy_trucker.Controller.Messages.GameBoardEvent;

/**
 * Represents a listener interface for monitoring changes in the game board.
 * This interface should be implemented by any class that wants to be notified
 * when updates or modifications occur on the game board.
 */
public interface GameBoardListener {

    /**
     * Called when there is a change or update on the game board.
     * Implementers of this method will receive the relevant details
     * about the changes through the provided GameBoardEvent.
     *
     * @param event the GameBoardEvent containing information about the change
     *              on the game board, such as position and the player involved.
     */
    void gameBoardChanged(GameBoardEvent event);
}
