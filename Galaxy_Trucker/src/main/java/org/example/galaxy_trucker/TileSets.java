package org.example.galaxy_trucker;

import java.util.ArrayList;
import java.util.Set;
import java.util.Random;

public class TileSets {

    private Set<Tile> CoveredTiles;
    private ArrayList<Tile> UncoveredTiles;
    public TileSets() {
        
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

