package org.example.galaxy_trucker;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameBoardTest {

    @Test
    void addPlayer() {
        TileSets tileSets = new TileSets();
        GameBoard gb = new GameBoard(tileSets, 2);
        gb.addPlayer("player1");

        assertEquals(1, gb.getPlayers().size());
        for(Player p : gb.getPositions()){
            assertNull(p);      //tests that player array is correctly initialize with null
        }
    }

    @Test
    void setStartingPositions() {
        TileSets tileSets = new TileSets();
        GameBoard gb = new GameBoard(tileSets, 2);

        gb.addPlayer("Player1");
        gb.addPlayer("Player2");

        gb.SetStartingPosition("Player1");
        gb.SetStartingPosition("Player2");

        assertEquals("Player1", gb.getPositions()[6].GetID());
        assertEquals("Player2", gb.getPositions()[3].GetID());

        assertNull(gb.getPositions()[4]);
        assertNull(gb.getPositions()[0]);


    }

    @Test
    void movePlayer() {
        TileSets tileSets = new TileSets();
        GameBoard gb = new GameBoard(tileSets, 2);

        gb.addPlayer("Player1");
        gb.addPlayer("Player2");
        gb.addPlayer("Player3");

        gb.SetStartingPosition("Player1");
        gb.SetStartingPosition("Player2");
        gb.SetStartingPosition("Player3");


        assertEquals("Player1", gb.getPositions()[6].GetID());
        assertEquals("Player2", gb.getPositions()[3].GetID());
        assertEquals("Player3", gb.getPositions()[1].GetID());


        gb.movePlayer("Player3", 2);
        assertEquals("Player2", gb.getPositions()[3].GetID());
        assertEquals("Player3", gb.getPositions()[4].GetID());
        assertNull(gb.getPositions()[1]);

        gb.movePlayer("Player3", -2);
        assertEquals("Player3", gb.getPositions()[1].GetID());
        assertNull(gb.getPositions()[4]);

        gb.movePlayer("Player3", 5);

        assertEquals("Player1", gb.getPositions()[6].GetID());
        assertEquals("Player3", gb.getPositions()[8].GetID());
        assertNull(gb.getPositions()[4]);

    }
}