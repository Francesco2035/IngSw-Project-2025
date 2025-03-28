package org.example.galaxy_trucker.Model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.galaxy_trucker.Model.Cards.Card;
import org.example.galaxy_trucker.Model.Tiles.Tile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class GAGen {

    private ObjectMapper mapper = new ObjectMapper();
    private File TilesJSON = new File("src/main/resources/org/example/galaxy_trucker/Tiles.JSON");
    private File CardsJSON = new File("src/main/resources/org/example/galaxy_trucker/Cards.JSON");  //add file json
    private ArrayList<Tile> tilesDeck;
    private ArrayList<Card> cardsDeck;


    //parsa json per connectors e component e chiama il builder di tile ripetuto fino a EOF
    public GAGen() throws IOException {
        this.tilesDeck = mapper.readValue(TilesJSON, new TypeReference<ArrayList<Tile>>() {});
        this.cardsDeck = mapper.readValue(CardsJSON, new TypeReference<ArrayList<Card>>() {});
    }



    public Set<Tile> TileListToSet() {
        return new HashSet<>(tilesDeck);
    }

    public Set<Card> CardListToSet() {
        return new HashSet<>(cardsDeck);
    }


    //getters for tilesdeck and cardsdeck
    public ArrayList<Tile> getTilesDeck() {
        return tilesDeck;
    }
    public ArrayList<Card> getCardsDeck() {return cardsDeck;}

}
