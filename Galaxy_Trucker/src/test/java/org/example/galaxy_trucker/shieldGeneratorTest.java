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


//        Tile tile = new Tile(new IntegerPair(0, 0), new shieldGenerator(), Connector.SINGLE, Connector.DOUBLE,  Connector.UNIVERSAL, Connector.NONE);
//        tile.getComponent().initType();
//        assertEquals(0, tile.getComponent().getAbility());


        ObjectMapper mapper = new ObjectMapper();
        File TilesJSON = new File("src/main/resources/org/example/galaxy_trucker/Tiles.JSON");  //add file json
        ArrayList<Tile> Tiles = mapper.readValue(TilesJSON, new TypeReference<ArrayList<Tile>>() {});

        Tile tile = Tiles.get(145);
        tile.getComponent().initType();

        tile.RotateDx();
        tile.RotateDx();
        tile.RotateDx();
        tile.RotateDx();
        tile.RotateDx();
        tile.RotateDx();
        tile.RotateDx();
        tile.RotateDx();
        tile.RotateSx();
        tile.RotateSx();
        tile.RotateSx();
        tile.RotateSx();
        tile.RotateSx();
        tile.RotateSx();




    }
}