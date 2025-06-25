package org.example.galaxy_trucker.Controller.Listeners;

import org.example.galaxy_trucker.Controller.Messages.PBInfoEvent;
import org.example.galaxy_trucker.Controller.Messages.PlayerBoardEvents.TileEvent;

/**
 * This interface defines the methods for listening to changes in a player's board state
 * and associated information during gameplay.
 *
 * Implementations of this interface should handle the events triggered when there are
 * updates to the player's board or player board information.
 */
public interface PlayerBoardListener {

    /**
     * Handles the event triggered when a player's board state changes.
     * This method is invoked with a TileEvent instance that provides detailed
     * information about the specific change, including tile coordinates, cargo,
     * alien presence, batteries, and other tile-specific attributes.
     *
     * @param event the TileEvent containing details about the player's board update,
     *              including tile ID, cargo, connectors, rotation, and other
     *              related attributes.
     */
    void playerBoardChanged(TileEvent event);

    /**
     * Notifies the listener that a change has occurred in the player's board-related information.
     *
     * @param event the PBInfoEvent containing updated details about the player's board state,
     *              such as credits, damage, energy levels, shield status, alien presence,
     *              and other associated information.
     */
    void PBInfoChanged(PBInfoEvent event);
}
