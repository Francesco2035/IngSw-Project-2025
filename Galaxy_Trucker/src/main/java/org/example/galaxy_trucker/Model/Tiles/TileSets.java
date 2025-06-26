package org.example.galaxy_trucker.Model.Tiles;

import org.example.galaxy_trucker.Controller.Listeners.TileSestListener;
import org.example.galaxy_trucker.ClientServer.Messages.TileSets.CoveredTileSetEvent;
import org.example.galaxy_trucker.ClientServer.Messages.TileSets.UncoverdTileSetEvent;
import org.example.galaxy_trucker.Model.GAGen;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Random;

/**
 * The TileSets class manages two sets of tiles: covered tiles and uncovered tiles.
 * It provides methods to access, add, and manipulate these sets, while enabling
 * listener-based notifications when changes occur in the tile sets.
 * This class is thread-safe for operations on the covered and uncovered tile sets.
 */
public class TileSets {

    /**
     * Represents a collection of tiles that are currently covered.
     * This set is used to manage and track tiles that are not yet exposed or processed.
     */
    private Set<Tile> CoveredTiles;
    /**
     * Represents a collection of tiles that have been uncovered for use in the current tile set.
     * This list is maintained to track tiles that are no longer part of the covered set and
     * may be utilized elsewhere within the tile management logic.
     */
    private ArrayList<Tile> UncoveredTiles;
    /**
     * A collection of listeners observing changes in tile sets.
     * This list stores instances of TileSestListener, which are notified
     * whenever tile set updates occur. The listeners can be added or removed
     * through the associated methods in the TileSets class.
     */
    private ArrayList<TileSestListener> listeners = new ArrayList<>();
    /**
     * Initializes a new instance of the TileSets class.
     * This constructor sets up the covered tiles by utilizing the TileListToSet method from the GAGen object,
     * and initializes an empty list for uncovered tiles.
     *
     * @param gag an instance of GAGen used to retrieve the list of tiles and convert them into a set for the covered tiles.
     */
    public TileSets(GAGen gag) {
        CoveredTiles = gag.TileListToSet();
        UncoveredTiles = new ArrayList<>();
    }

    /**
     * Retrieves a random Tile object from the set of covered tiles, removes it from the set,
     * and notifies listeners about the change in the covered tile set.
     *
     * @return a randomly selected Tile object from the set of covered tiles.
     * @throws NoSuchElementException if the set of covered tiles is empty.
     */
    public Tile getNewTile() {
        synchronized (CoveredTiles) {
            Random r = new Random();
            int index = r.nextInt(CoveredTiles.size());

            Tile SelectedTile =  CoveredTiles.stream()
                    .skip(index)
                    .findFirst()
                    .orElseThrow();

            CoveredTiles.remove(SelectedTile);
            for (TileSestListener listener : listeners) {
                listener.tilesSetChanged(new CoveredTileSetEvent(CoveredTiles.size()));
            }

            return SelectedTile;
        }
    }


    /**
     * Retrieves and removes a tile from the list of uncovered tiles at the specified index.
     * Notifies listeners about the change in the tile set.
     *
     * @param index the index of the tile in the uncovered tiles list to be retrieved
     * @return the tile at the specified index, or null if the index is out of bounds
     */
    public Tile getNewTile(int index){

        Tile SelectedTile;
        synchronized (UncoveredTiles) {
            try {
                SelectedTile = UncoveredTiles.remove(index);

                for (TileSestListener listener : listeners){
                    listener.tilesSetChanged(new UncoverdTileSetEvent(SelectedTile.getId(),null));
                }

            }catch (IndexOutOfBoundsException e){
                System.out.println("No valid tile selected!");
                return null;
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
            return SelectedTile;

        }

//        if (SelectedTile.isAvailable()){
//            SelectedTile.setAvailable(false);
//            return SelectedTile;
//        }
//
//        throw new RuntimeException("Tile not available, someone else took it!");
    }

    /**
     * Adds a tile to the collection of uncovered tiles if it is not already present.
     * Notifies all registered listeners about the change in the uncovered tile set.
     *
     * @param tile The tile to be added to the collection of uncovered tiles.
     * @throws RemoteException If a communication-specific error occurs.
     */
    public void AddUncoveredTile(Tile tile) throws RemoteException{
        synchronized (UncoveredTiles) {
            if(!UncoveredTiles.contains(tile))
                UncoveredTiles.add(tile);


            for (TileSestListener listener : listeners){
                listener.tilesSetChanged(new UncoverdTileSetEvent(tile.getId(), tile.getConnectors()));
            }
        }
    }
        //tile.setAvailable(true);


    /**
     * Registers a new TileSestListener to the list of listeners.
     * This listener will be notified of changes or updates in the tile set.
     *
     * @param listener the TileSestListener to be added to the list of listeners
     */
    public void setListeners(TileSestListener listener) {
        listeners.add(listener);
    }

    /**
     * Removes a specified listener from the collection of registered TileSestListener objects.
     * Once removed, the listener will no longer receive notifications for tile set changes.
     *
     * @param listener the TileSestListener to be removed from the list of listeners
     */
    public void removeListeners(TileSestListener listener) {
        listeners.remove(listener);
    }

    /**
     * Retrieves the list of uncovered tiles.
     *
     * @return an ArrayList containing the uncovered tiles.
     */
    public ArrayList<Tile> getUncoveredTiles(){return UncoveredTiles;}

}

