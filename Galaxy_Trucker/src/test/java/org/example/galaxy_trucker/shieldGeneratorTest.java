package org.example.galaxy_trucker;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class shieldGeneratorTest {

    @Test
    void initType() throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        File TilesJSON = new File("src/main/resources/org/example/galaxy_trucker/Tiles.JSON");  //add file json
        ArrayList<Tile> Tiles = mapper.readValue(TilesJSON, new TypeReference<ArrayList<Tile>>() {});

        Tile tile = Tiles.get(145);
        tile.getComponent().initType();

        assertEquals(0, tile.getComponent().getAbility(0).getFirst());

    }

    @Test
    void getAbility() {
    }

    @Test
    void setAbility() throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        File TilesJSON = new File("src/main/resources/org/example/galaxy_trucker/Tiles.JSON");  //add file json
        ArrayList<Tile> Tiles = mapper.readValue(TilesJSON, new TypeReference<ArrayList<Tile>>() {});

        Tile tile = Tiles.get(145);
        tile.getComponent().initType();


        tile.RotateDx();
        assertEquals(0, tile.getComponent().getAbility(0).getFirst());

        tile.RotateDx();
        assertEquals(1, tile.getComponent().getAbility(0).getFirst());

        tile.RotateSx();
        assertEquals(0, tile.getComponent().getAbility(0).getFirst());

    }
}