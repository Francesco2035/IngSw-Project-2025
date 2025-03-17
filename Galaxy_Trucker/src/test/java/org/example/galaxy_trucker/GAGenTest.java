package org.example.galaxy_trucker;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javafx.util.Pair;

import static org.junit.jupiter.api.Assertions.*;

class GAGenTest {

    @Test
    void getDeck() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        File TilesJSON = new File("src/main/resources/org/example/galaxy_trucker/Tiles.JSON");  //add file json
        File CardsJSON = new File("src/main/resources/org/example/galaxy_trucker/Cards.JSON");  //add file json
        mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);

        ArrayList<Tile> Tiles = mapper.readValue(TilesJSON, new TypeReference<ArrayList<Tile>>() {});
        ArrayList<Card> Cards = mapper.readValue(CardsJSON, new TypeReference<ArrayList<Card>>() {});


        assertEquals(0, Tiles.getFirst().getComponent().getAbility()); //testo se il primo powerCenter ha privEnergy=2 dopo l'operazione di initType
    }
}