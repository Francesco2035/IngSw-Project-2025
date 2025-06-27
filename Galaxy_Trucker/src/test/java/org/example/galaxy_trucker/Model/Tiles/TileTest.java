package org.example.galaxy_trucker.Model.Tiles;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.galaxy_trucker.Model.Connectors.Connectors;
import org.example.galaxy_trucker.Model.GAGen;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * tests the tile
 */
class TileTest {
    /**
     * tests if fou can see the validity of adjacent tiles
     * @throws IOException
     */
    @Test
    void checkAdjacent() throws IOException {

        GAGen g = new GAGen();
        ArrayList<Tile> tiles = g.getTilesDeck();
        Tile t1 = tiles.get(134); //"SINGLE", "DOUBLE", "SINGLE", "NONE addons, br factos
        Tile t2 = tiles.get(102); //none, cannon, single, universal, plasmadrill ruota sx due volte factos
        Tile t3 = tiles.get(128); //double cannon, none, universal. plasmadrill  factos
        Tile t4 = tiles.get(150); //universal,k none, double, double shield none, double, double, universal ruota a sx factos
        Tile specialStorage = tiles.get(57);
        Tile t8 = tiles.get(56);  // universal, universal, double, double. sewerpips factos
        Tile t9 = tiles.get(32); //SINGLE, "NONE", "NONE", "UNIVERSAL housingjfoaihj factos
        Tile t10 = tiles.get(33); //SINGLE, "SINGLE", "DOUBLE", "SINGLE moudsajdahoyusingunit ruota dx factos
        Tile t11 = tiles.get(73); //SINGLE", "DOUBLE", "NONE", "MOTOR" un motore factos
        Tile t12 = tiles.get(146);

        t3.RotateDx();
        t3.RotateDx();

//        assertTrue(t1.checkAdjacent(t3, 0));

    }

}