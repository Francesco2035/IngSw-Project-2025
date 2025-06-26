package org.example.galaxy_trucker.Controller.Listeners;

import org.example.galaxy_trucker.ClientServer.Messages.GameLobbyEvent;

/**
 * Represents a listener interface for receiving notifications about changes
 * in the state of a game lobby. Classes that are interested in monitoring
 * game lobby changes should implement this interface.
 * <p>
 * The implementing class should define the behavior within the GameLobbyChanged
 * method, which will be invoked whenever a GameLobbyEvent occurs.
 */
public interface GameLobbyListener {
    /**
     * Handles game lobby change events. This method is invoked
     * to notify listeners about updates or changes in the game lobby.
     *
     * @param e the GameLobbyEvent containing details about the change in
     *          the game lobby, such as the list of players and their readiness status.
     */
    void GameLobbyChanged(GameLobbyEvent e);
}
