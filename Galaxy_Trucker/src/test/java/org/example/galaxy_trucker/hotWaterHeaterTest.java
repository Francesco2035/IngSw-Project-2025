package org.example.galaxy_trucker;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class hotWaterHeaterTest {

    @Test
    void getAbility() throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        File TilesJSON = new File("src/main/resources/org/example/galaxy_trucker/Tiles.JSON");  //add file json
        ArrayList<Tile> Tiles = mapper.readValue(TilesJSON, new TypeReference<ArrayList<Tile>>() {});

        Component comp = Tiles.get(66).getComponent();
        comp.initType();
        assertEquals(1, comp.getAbility());


        comp = Tiles.get(87).getComponent();
        comp.initType();
        assertEquals(2, comp.getAbility());

    }

    @Test
    void initType() throws IOException {


        ObjectMapper mapper = new ObjectMapper();
        File TilesJSON = new File("src/main/resources/org/example/galaxy_trucker/Tiles.JSON");  //add file json
        ArrayList<Tile> Tiles = mapper.readValue(TilesJSON, new TypeReference<ArrayList<Tile>>() {});


        hotWaterHeater engine = ((hotWaterHeater) Tiles.get(66).getComponent());
        engine.initType();
        assertFalse(engine.isDouble());

        hotWaterHeater engine2 = ((hotWaterHeater) Tiles.get(87).getComponent());
        engine2.initType();
        assertTrue(engine2.isDouble());
    }
}