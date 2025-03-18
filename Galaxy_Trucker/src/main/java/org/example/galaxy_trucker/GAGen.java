package org.example.galaxy_trucker;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class GAGen {

    private ObjectMapper mapper = new ObjectMapper();
    private File TilesJSON = new File("src/main/resources/org/example/galaxy_trucker/Tiles.JSON");
    private File CardsJSON = new File("src/main/resources/org/example/galaxy_trucker/Cards.JSON");  //add file json
    private ArrayList<Tile> tilesDeck;
    private ArrayList<Tile> cardsDeck;


    //parsa json per connectors e component e chiama il builder di tile ripetuto fino a EOF
    public GAGen() throws IOException {
        this.tilesDeck = mapper.readValue(TilesJSON, new TypeReference<ArrayList<Tile>>() {});
        this.cardsDeck = mapper.readValue(CardsJSON, new TypeReference<ArrayList<Tile>>() {});
    }


    //getters for tilesdeck and cardsdeck
    public ArrayList<Tile> getTilesDeck() {
        return tilesDeck;
    }
    public ArrayList<Tile> getCardsDeck() {return cardsDeck;}

}
