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

/**
 * GAGen class is responsible for loading and managing decks of tiles and cards
 * used in the application. It provides functionality to convert the decks into sets
 * and access the original lists.
 *
 * This class makes use of the Jackson ObjectMapper to load JSON data into Java objects.
 * The decks data is expected to be stored in external JSON files.
 */
public class GAGen implements Serializable {

    /**
     * The ObjectMapper instance used for JSON parsing and serialization operations
     * in the GAGen class. This mapper is utilized to convert JSON data into Java
     * objects such as Tile and Card, as well as for any necessary serialization
     * back to JSON.
     *
     * It serves as the core utility for handling JSON data in the class, making use
     * of Jackson's functionality to process external files containing deck information.
     * The JSON files are expected to follow appropriate formats that map to the structure
     * of the respective Java objects.
     */
    private final ObjectMapper mapper = new ObjectMapper();
    /**
     * Represents a deck of Tile objects loaded from an external JSON file.
     * This variable is used to store and manage the list of tiles available in the system.
     * The tilesDeck is populated during the initialization of the GAGen instance by reading
     * and deserializing JSON data.
     */
    private ArrayList<Tile> tilesDeck;
    /**
     * Represents a collection of Card objects that constitute the deck used in the application.
     * This deck is initialized by deserializing JSON data from an external file and is managed by
     * the GAGen class. The cardsDeck variable contains all the Card objects in their current state
     * and can be manipulated or accessed as needed.
     */
    private ArrayList<Card> cardsDeck;

    /**
     * Constructs a new instance of the GAGen class and initializes the tiles and cards decks
     * by loading data from external JSON files.
     *
     * @throws IOException if an error occurs while loading the decks from the JSON files.
     */
    public GAGen() throws IOException {
        loadDecks();
    }

    /**
     * Loads the decks of tiles and cards from external JSON files and initializes
     * the respective data structures. The method expects two JSON files, "Tiles.JSON"
     * and "Cards.JSON", to be available in the application's classpath. The data is
     * deserialized using the Jackson ObjectMapper instance and stored in the
     * corresponding ArrayList fields.
     *
     * In case of an I/O error during the loading process, an error message is printed
     * to the standard output without halting the execution.
     */
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
    /**
     * Converts the list of tiles (`tilesDeck`) into a set.
     * This method ensures that duplicate tiles, if any, are removed
     * when generating the set from the list.
     *
     * @return a Set containing the unique elements of `tilesDeck`.
     */
    public Set<Tile> TileListToSet() {
        return new HashSet<>(tilesDeck);
    }

    /**
     * Converts the list of cards (`cardsDeck`) into a set to eliminate duplicates and return a unique collection of cards.
     *
     * @return a set of unique Card objects created from the `cardsDeck` list.
     */
    public Set<Card> CardListToSet() {
        return new HashSet<>(cardsDeck);
    }

    /**
     * Retrieves the current list of tiles in the tiles deck.
     *
     * @return an ArrayList of Tile objects representing the tiles deck.
     */
    public ArrayList<Tile> getTilesDeck() {
        return tilesDeck;
    }

    /**
     * Retrieves the list of cards in the deck.
     *
     * @return An ArrayList containing Card objects representing the deck of cards.
     */
    public ArrayList<Card> getCardsDeck() {
        return cardsDeck;
    }
}
