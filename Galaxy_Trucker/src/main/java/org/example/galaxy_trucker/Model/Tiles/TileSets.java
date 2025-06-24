package org.example.galaxy_trucker.Model.Tiles;

import org.example.galaxy_trucker.Controller.Listeners.TileSestListener;
import org.example.galaxy_trucker.Messages.TileSets.CoveredTileSetEvent;
import org.example.galaxy_trucker.Messages.TileSets.UncoverdTileSetEvent;
import org.example.galaxy_trucker.Model.GAGen;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Set;
import java.util.Random;

public class TileSets {

    private Set<Tile> CoveredTiles;
    private ArrayList<Tile> UncoveredTiles;
    private ArrayList<TileSestListener> listeners = new ArrayList<>();
    public TileSets(GAGen gag) {
        CoveredTiles = gag.TileListToSet();
        UncoveredTiles = new ArrayList<>();
    }

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


    public void setListeners(TileSestListener listener) {
        listeners.add(listener);
    }

    public void removeListeners(TileSestListener listener) {
        listeners.remove(listener);
    }

    public ArrayList<Tile> getUncoveredTiles(){return UncoveredTiles;}

}

