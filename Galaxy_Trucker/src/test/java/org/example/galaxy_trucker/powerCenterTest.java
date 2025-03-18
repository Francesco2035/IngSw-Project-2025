package org.example.galaxy_trucker;

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
        GameBoard board = new GameBoard(tileSets, 2);
        Player me = new Player("prova", board);
        Component batt = new powerCenter(3);
        assertEquals(3, batt.getAbility());

    }

    @Test
    void setAbility() throws IOException {
        GAGen gag = new GAGen();
        TileSets tileSets = new TileSets(gag);
        GameBoard board = new GameBoard(tileSets, 2);
        Player me = new Player("prova", board);
        Component batt = new powerCenter(3);
        assertEquals(2, batt.setAbility());

    }

    @Test
    void initType() throws IOException {
        GAGen gen = new GAGen();
        ArrayList<Tile> deck = gen.getDeck();
        deck.getFirst().getComponent().initType();
        assertEquals(2, deck.getFirst().getComponent().getAbility());
    }
}
