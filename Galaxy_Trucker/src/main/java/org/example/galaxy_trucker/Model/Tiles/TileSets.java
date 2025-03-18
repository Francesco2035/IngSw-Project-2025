package org.example.galaxy_trucker.Model.Tiles;

import org.example.galaxy_trucker.Model.GAGen;

import java.util.ArrayList;
import java.util.Set;
import java.util.Random;

public class TileSets {

    private Set<Tile> CoveredTiles;
    private ArrayList<Tile> UncoveredTiles;
    public TileSets(GAGen gag) {
        CoveredTiles = gag.TileListToSet();
        UncoveredTiles = new ArrayList<>();
    }

    public Tile getNewTile(){
        Random r = new Random();
        int index = r.nextInt(CoveredTiles.size());

        Tile SelectedTile =  CoveredTiles.stream()
                                         .skip(index)
                                         .findFirst()
                                         .orElseThrow();

        CoveredTiles.remove(SelectedTile);

        return SelectedTile;
    }


    public Tile getNewTile(int index){
        Tile SelectedTile = UncoveredTiles.get(index);

        UncoveredTiles.remove(SelectedTile);

        return SelectedTile;
    }

    public void AddUncoveredTile(Tile tile){
        UncoveredTiles.add(tile);
    }

    public ArrayList<Tile> getUncoveredTiles(){return UncoveredTiles;}

}

