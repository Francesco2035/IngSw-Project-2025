package org.example.galaxy_trucker.Controller.Listeners;

import org.example.galaxy_trucker.Controller.Messages.LobbyEvent;

/**
 * The LobbyListener interface provides a mechanism for handling and sending
 * lobby-related events. Classes implementing this interface are responsible
 * for managing the communication of events related to game lobbies, such as
 * player activity, game creation, updates, or deletions.
 *
 * Implementers of this interface can use the provided method to send
 * {@link LobbyEvent} instances to notify registered listeners or take
 * further actions based on the lobby state changes.
 */
public interface LobbyListener {
     /**
      * Sends a {@link LobbyEvent} to handle or notify about a specific event related
      * to the game lobby. Implementing this method allows for the communication and
      * management of changes in the lobby, such as player actions or lobby updates.
      *
      * @param event the {@link LobbyEvent} containing details about the event,
      *              including game ID, level, maximum players, and the list of players.
      */
     void sendEvent(LobbyEvent event);
}
