package org.example.galaxy_trucker;

import javafx.util.Pair;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BatteryCompTest {


    @Test
    void getAbility() {
        GameBoard board = new GameBoard();

        Player me = new Player("prova", 2, board);
        Component batt = new BatteryComp(3);
        assertEquals(3, batt.getAbility());

    }

    @Test
    void setAbility() {
        GameBoard board = new GameBoard();


        Player me = new Player("prova", 2, board);
        Component batt = new BatteryComp(3);
        assertEquals(2, batt.setAbility());

    }
}
