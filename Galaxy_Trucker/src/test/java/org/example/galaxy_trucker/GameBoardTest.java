package org.example.galaxy_trucker;

import org.example.galaxy_trucker.Model.Boards.GameBoard;
import org.example.galaxy_trucker.Model.GAGen;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.Tiles.TileSets;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class GameBoardTest {

    @Test
    void addPlayer() throws IOException {
        GAGen gag = new GAGen();
        TileSets tileSets = new TileSets(gag);
        GameBoard gb = new GameBoard(tileSets, 2, null);
        Player p1 = new Player();
        p1.setId("Player1");
        gb.addPlayer(p1);

        assertEquals(1, gb.getPlayers().size());
        for(Player p : gb.getPositions()){
            assertNull(p);      //tests that player array is correctly initialize with null
        }
    }

    @Test
    void setStartingPositions() throws IOException {
        GAGen gag = new GAGen();
        TileSets tileSets = new TileSets(gag);
        GameBoard gb = new GameBoard(tileSets, 2, null);
        Player p1 = new Player();
        p1.setId("Player1");
        Player p2 = new Player();
        p2.setId("Player2");


        gb.addPlayer(p1);
        gb.addPlayer(p2);

        gb.SetStartingPosition(p1);
        gb.SetStartingPosition(p2);

        assertEquals("Player1", gb.getPositions()[6].GetID());
        assertEquals("Player2", gb.getPositions()[3].GetID());

        assertNull(gb.getPositions()[4]);
        assertNull(gb.getPositions()[0]);


    }

    @Test
    void movePlayer() throws IOException {
        GAGen gag = new GAGen();
        TileSets tileSets = new TileSets(gag);
        GameBoard gb = new GameBoard(tileSets, 1, null);
        Player p1 = new Player();
        p1.setId("Player1");
        Player p2 = new Player();
        p2.setId("Player2");
        Player p3 = new Player();
        p3.setId("Player3");


        gb.addPlayer(p1);
        gb.addPlayer(p2);
        gb.addPlayer(p3);

        p1.EndConstruction();
        p2.EndConstruction();
        p3.EndConstruction();


        assertEquals("Player1", gb.getPositions()[4].GetID());
        assertEquals("Player2", gb.getPositions()[2].GetID());
        assertEquals("Player3", gb.getPositions()[1].GetID());


        gb.movePlayer("Player3", 2);
        assertEquals("Player2", gb.getPositions()[2].GetID());
        assertEquals("Player3", gb.getPositions()[5].GetID());
        assertNull(gb.getPositions()[1]);

        gb.movePlayer("Player3", -2);
        assertEquals("Player3", gb.getPositions()[1].GetID());
        assertNull(gb.getPositions()[5]);

        gb.movePlayer("Player3", 5);

        assertEquals("Player1", gb.getPositions()[4].GetID());
        assertEquals("Player3", gb.getPositions()[8].GetID());
        assertNull(gb.getPositions()[1]);

    }

    @Test
    void movePlayerTest2() throws IOException {
        GAGen gag = new GAGen();
        TileSets tileSets = new TileSets(gag);
        GameBoard gb = new GameBoard(tileSets, 2, null);
        Player p1 = new Player();
        p1.setId("Player1");
        Player p2 = new Player();
        p2.setId("Player2");
        Player p3 = new Player();
        p3.setId("Player3");


        gb.addPlayer(p1);
        gb.addPlayer(p2);
        gb.addPlayer(p3);

        p1.EndConstruction(1);
        p2.EndConstruction(2);
        p3.EndConstruction(3);

        assertEquals("Player1", gb.getPositions()[6].GetID());
        assertEquals("Player2", gb.getPositions()[3].GetID());
        assertEquals("Player3", gb.getPositions()[1].GetID());


        gb.movePlayer("Player3", -5);
        assertNull(gb.getPositions()[1]);
//        gb.movePlayer("Player1", 16);
    }


    @Test
    void RemoveAndShiftTest() throws IOException {

        GAGen gag = new GAGen();
        TileSets tileSets = new TileSets(gag);
        GameBoard gb = new GameBoard(tileSets, 2, null);

        Player p1 = new Player();
        p1.setId("Player1");
        Player p2 = new Player();
        p2.setId("Player2");
        Player p3 = new Player();
        p3.setId("Player3");
        Player p4 = new Player();
        p4.setId("Player4");

        gb.addPlayer(p1);
        gb.addPlayer(p2);
        gb.addPlayer(p3);
        gb.addPlayer(p4);

        p1.EndConstruction(1);
        p2.EndConstruction(2);
        p3.EndConstruction(3);
        p4.EndConstruction(4);

        assertEquals(p1, gb.getPositions()[6]);
        assertEquals(p2, gb.getPositions()[3]);
        assertEquals(p3, gb.getPositions()[1]);
        assertEquals(p4, gb.getPositions()[0]);


        gb.removePlayerAndShift(p3);
        assertEquals(p1, gb.getPositions()[6]);
        assertEquals(p2, gb.getPositions()[3]);
        assertEquals(p4, gb.getPositions()[1]);
        assertNull(gb.getPositions()[0]);

        gb.removePlayerAndShift(p4);
        assertEquals(p1, gb.getPositions()[6]);
        assertEquals(p2, gb.getPositions()[3]);
        assertNull(gb.getPositions()[1]);

        p3.EndConstruction(4);
        assertEquals(p1, gb.getPositions()[6]);
        assertEquals(p2, gb.getPositions()[3]);
        assertEquals(p3, gb.getPositions()[0]);

        gb.removePlayerAndShift(p1);
        assertEquals(p2, gb.getPositions()[6]);
        assertEquals(p3, gb.getPositions()[3]);
        assertNull(gb.getPositions()[0]);

        gb.removePlayerAndShift(p2);
        assertEquals(p3, gb.getPositions()[6]);
        assertNull(gb.getPositions()[3]);

    }
}