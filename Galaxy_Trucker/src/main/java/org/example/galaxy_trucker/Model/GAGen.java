package org.example.galaxy_trucker.Model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.galaxy_trucker.Model.Cards.Card;
import org.example.galaxy_trucker.Model.Tiles.Tile;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class GAGen implements Serializable {

    private final ObjectMapper mapper = new ObjectMapper();
    private ArrayList<Tile> tilesDeck;
    private ArrayList<Card> cardsDeck;

    public GAGen() throws IOException {
        loadDecks();
    }

    private void loadDecks(){
        try {
            InputStream tilesStream = getClass().getClassLoader().getResourceAsStream("Tiles.JSON");
            InputStream cardsStream = getClass().getClassLoader().getResourceAsStream("Cards.JSON");
            tilesDeck = mapper.readValue(tilesStream, new TypeReference<>() {});
            cardsDeck = mapper.readValue(cardsStream, new TypeReference<>() {});
        }
        catch (IOException e) {
            System.out.println("Couldn't load decks: error "+e.getMessage());
        }
    }
    public Set<Tile> TileListToSet() {
        return new HashSet<>(tilesDeck);
    }

    public Set<Card> CardListToSet() {
        return new HashSet<>(cardsDeck);
    }

    public ArrayList<Tile> getTilesDeck() {
        return tilesDeck;
    }

    public ArrayList<Card> getCardsDeck() {
        return cardsDeck;
    }
}
