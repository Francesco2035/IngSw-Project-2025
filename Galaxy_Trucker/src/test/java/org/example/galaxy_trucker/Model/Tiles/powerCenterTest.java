package org.example.galaxy_trucker.Model.Tiles;

import org.example.galaxy_trucker.Model.Boards.GameBoard;
import org.example.galaxy_trucker.Model.GAGen;
import org.example.galaxy_trucker.Model.Player;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class powerCenterTest {

    @Test
    void getPrivEnergy() {
    }

    @Test
    void setPrivEnergy() {
    }

    @Test
    void getAbility() throws IOException {
        GAGen gag = new GAGen();
        TileSets tileSets = new TileSets(gag);
        GameBoard board = new GameBoard(tileSets, 2, null);
        Player me = new Player();


    }

    @Test
    void setAbility() throws IOException {
        GAGen gag = new GAGen();
        TileSets tileSets = new TileSets(gag);
        GameBoard board = new GameBoard(tileSets, 2, null);
        Player me = new Player();
        me.setId("prova");

    }

    @Test
    void initType() throws IOException {
        GAGen gen = new GAGen();
        ArrayList<Tile> deck = gen.getTilesDeck();
//        assertEquals(2, deck.getFirst().getComponent().getAbility());
    }
}