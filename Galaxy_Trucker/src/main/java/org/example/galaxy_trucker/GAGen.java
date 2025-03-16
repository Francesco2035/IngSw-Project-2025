package org.example.galaxy_trucker;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GAGen {

    private ObjectMapper mapper = new ObjectMapper();
    private File TilesJSON = new File("src/main/resources/org/example/galaxy_trucker/Tiles.JSON");
    private ArrayList<Tile> deck;



    //parsa json per connectors e component e chiama il builder di tile ripetuto fino a EOF
    public GAGen() throws IOException {
        this.deck = mapper.readValue(TilesJSON, new TypeReference<ArrayList<Tile>>() {});
    }


    public ArrayList<Tile> getDeck() {
        return deck;
    }

    public void setDeck(ArrayList<Tile> deck) {this.deck = deck;}


}