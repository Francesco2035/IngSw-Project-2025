package org.example.galaxy_trucker.Model;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    @Test
    void gameTest() throws IOException {

        Game game = new Game(2, "prova1");


        Player p1 = new Player(); p1.setId("p1");
        Player p2 = new Player(); p2.setId("p2");
        Player p3 = new Player(); p3.setId("p3");
        Player p4 = new Player(); p4.setId("p4");
        Player p5 = new Player(); p5.setId("p5");
        Player p6 = new Player(); p6.setId("p3");



        game.NewPlayer(p1);
        game.NewPlayer(p2);
        game.NewPlayer(p3);
        try {
            game.NewPlayer(p6);
        } catch (IllegalArgumentException e) {
            assertEquals("Player already exists", e.getMessage());
        }
        game.NewPlayer(p4);
        try {
            game.NewPlayer(p5);
        } catch (IndexOutOfBoundsException e) {
            assertEquals("Game is full", e.getMessage());
        }


        game.setGameID("MHANZ");
        game.setGameID("game1");

    }

}