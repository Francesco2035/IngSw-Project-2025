package org.example.galaxy_trucker.Controller.Listeners;

import org.example.galaxy_trucker.ClientServer.Messages.PlayerBoardEvents.PlayerTileEvent;

/**
 * Interface representing a listener for personal best (PB) updates related to players.
 * Classes implementing this interface should define how to handle updates related to
 * a player's performance or achievements.
 */
public interface PlayersPBListener {


    /**
     * Handles the update event related to a player's personal best (PB).
     * This method is invoked when there are updates or changes to a player's
     * tile event, which might include updates to player attributes, positions, or achievements.
     *
     * @param event the PlayerTileEvent containing details about the player and their updated state,
     *              including attributes such as player name, tile position (x, y), cargo,
     *              and other related information.
     */
    void receivePBupdate(PlayerTileEvent event);
}
