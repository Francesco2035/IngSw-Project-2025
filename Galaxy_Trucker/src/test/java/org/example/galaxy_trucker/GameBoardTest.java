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
        GameBoard gb = new GameBoard(tileSets, 2);
        Player p1 = new Player("Player1", gb);
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
        GameBoard gb = new GameBoard(tileSets, 2);
        Player p1 = new Player("Player1", gb);
        Player p2 = new Player("Player2", gb);

        gb.addPlayer(p1);
        gb.addPlayer(p2);

        gb.SetStartingPosition("Player1");
        gb.SetStartingPosition("Player2");

        assertEquals("Player1", gb.getPositions()[6].GetID());
        assertEquals("Player2", gb.getPositions()[3].GetID());

        assertNull(gb.getPositions()[4]);
        assertNull(gb.getPositions()[0]);


    }

    @Test
    void movePlayer() throws IOException {
        GAGen gag = new GAGen();
        TileSets tileSets = new TileSets(gag);
        GameBoard gb = new GameBoard(tileSets, 2);
        Player p1 = new Player("Player1", gb);
        Player p2 = new Player("Player2", gb);
        Player p3 = new Player("Player3", gb);


        gb.addPlayer(p1);
        gb.addPlayer(p2);
        gb.addPlayer(p3);

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

    @Test
    void movePlayerTest2() throws IOException {
        GAGen gag = new GAGen();
        TileSets tileSets = new TileSets(gag);
        GameBoard gb = new GameBoard(tileSets, 2);
        Player p1 = new Player("Player1", gb);
        Player p2 = new Player("Player2", gb);
        Player p3 = new Player("Player3", gb);


        gb.addPlayer(p1);
        gb.addPlayer(p2);
        gb.addPlayer(p3);

        gb.SetStartingPosition("Player1");
        gb.SetStartingPosition("Player2");
        gb.SetStartingPosition("Player3");

        assertEquals("Player1", gb.getPositions()[6].GetID());
        assertEquals("Player2", gb.getPositions()[3].GetID());
        assertEquals("Player3", gb.getPositions()[1].GetID());


        gb.movePlayer("Player3", -5);
        assertNull(gb.getPositions()[1]);
        gb.movePlayer("Player1", 16);
    }

}