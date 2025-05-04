package org.example.galaxy_trucker;

import org.example.galaxy_trucker.Model.Boards.GameBoard;
import org.example.galaxy_trucker.Model.Boards.Hourglass;
import org.example.galaxy_trucker.Model.Game;
import org.example.galaxy_trucker.Model.Player;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;


class HourglassTest {


    @Test
    void startTimer() throws IOException {

        Player p1  = new Player();
        p1.setId("bubu");

        Game g = new Game(2, "gameid");
        g.NewPlayer(p1);
        GameBoard gb = g.getGameBoard();
        Hourglass h = gb.getHourglass();

        p1.StartTimer();
        while(!h.isStartable()){
//            System.out.println("hrg 1");
        }

        p1.StartTimer();
        try {
            p1.StartTimer();
        }catch (RuntimeException e){
            System.out.println(e.getMessage());
        }

        while(!h.isStartable()){
//            System.out.println("hrg 2");
        }

        try {
            p1.StartTimer();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        assertEquals(1, h.getUsages());

        p1.SetReady(true);
        p1.StartTimer();

        while(h.getUsages() > 0){
//            System.out.println("hrg 3");
        }

        try {
            p1.StartTimer();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        assertEquals(0, h.getUsages());
    }
}