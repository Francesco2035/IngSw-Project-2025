package org.example.galaxy_trucker.Model.Tiles;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class hotWaterHeaterTest {
    /**
     * test the getability method
     * @throws IOException
     */
    @Test
    void getAbility() throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        File TilesJSON = new File("src/main/resources/org/example/galaxy_trucker/Tiles.JSON");  //add file json
        ArrayList<Tile> Tiles = mapper.readValue(TilesJSON, new TypeReference<ArrayList<Tile>>() {});

        Component comp = Tiles.get(66).getComponent();
//        assertEquals(1, comp.getAbility());


        comp = Tiles.get(87).getComponent();
//        assertEquals(2, comp.getAbility());

    }
    /**
     * test the initType method
     * @throws IOException
     */

    @Test
    void initType() throws IOException {


        ObjectMapper mapper = new ObjectMapper();
        File TilesJSON = new File("src/main/resources/org/example/galaxy_trucker/Tiles.JSON");  //add file json
        ArrayList<Tile> Tiles = mapper.readValue(TilesJSON, new TypeReference<ArrayList<Tile>>() {});


        HotWaterHeater engine = ((HotWaterHeater) Tiles.get(66).getComponent());

        HotWaterHeater engine2 = ((HotWaterHeater) Tiles.get(87).getComponent());
    }
}