package org.example.galaxy_trucker.Controller.Listeners;

import org.example.galaxy_trucker.Controller.Messages.TileSets.CoveredTileSetEvent;
import org.example.galaxy_trucker.Controller.Messages.TileSets.UncoverdTileSetEvent;

import java.rmi.RemoteException;

/**
 * The TileSestListener interface provides a mechanism for receiving notifications
 * related to changes in tile sets. Implementers of this interface can define the
 * behavior to be executed when the associated tile sets are updated or modified.
 */
public interface TileSestListener {

    /**
     * Notifies the implementing listener that a change has occurred in the covered tile set.
     * This method is triggered when an event related to covered tile sets is detected, providing
     * details of the event through the given {@code CoveredTileSetEvent}.
     *
     * @param event the CoveredTileSetEvent containing information about the changes in
     *              the covered tile set, such as its size or other relevant details.
     */
    void tilesSetChanged(CoveredTileSetEvent event);

    /**
     * Handles the event triggered when a tile set is updated or modified. This method is invoked
     * when an {@code UncoverdTileSetEvent} occurs, providing information about the changes made to
     * an uncovered tile set. Implementing this method allows for the processing of tile set updates
     * to reflect the appropriate state adjustments or actions.
     *
     * @param event the {@code UncoverdTileSetEvent} containing details of the tile set changes,
     *              including its unique identifier and associated connectors.
     * @throws RemoteException if a remote communication exception occurs during the execution of the method.
     */
    void tilesSetChanged(UncoverdTileSetEvent event) throws RemoteException;

}
