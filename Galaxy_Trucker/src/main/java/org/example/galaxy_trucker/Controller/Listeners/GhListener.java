package org.example.galaxy_trucker.Controller.Listeners;

import org.example.galaxy_trucker.Commands.QuitCommand;
import org.example.galaxy_trucker.ClientServer.Messages.LobbyEvent;

/**
 * The GhListener interface provides methods to handle events related to the management
 * of the game lobby and player actions. Implementing classes should provide
 * the logic for handling these events to maintain and update the lobby's state.
 */
public interface GhListener {
    /**
     * Sends a {@link LobbyEvent} to notify listeners of an event related to the game lobby.
     *
     * @param event the {@link LobbyEvent} containing the details of the lobby event to be sent.
     */
    void sendEvent(LobbyEvent event);

    /**
     * Updates the state of the game lobby based on the specified event.
     * This method processes the provided LobbyEvent to reflect changes
     * in the lobby, such as player additions, removals, or updates to the
     * maximum player limit and game level.
     *
     * @param event the LobbyEvent containing details about the modifications
     *              to the game lobby, including the list of players, game ID,
     *              level, and maximum number of players.
     */
    void updateLobby(LobbyEvent event);

    /**
     * Handles the event triggered when a player decides to quit the game.
     * This method processes a QuitCommand event, which encapsulates the details
     * of the request, including the player's identity and other relevant information.
     *
     * @param event the QuitCommand containing details about the player quitting the game.
     */
    void quitPlayer(QuitCommand event);
}
