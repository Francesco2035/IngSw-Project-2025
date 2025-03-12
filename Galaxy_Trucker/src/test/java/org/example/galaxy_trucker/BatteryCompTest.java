package org.example.galaxy_trucker;

import javafx.util.Pair;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BatteryCompTest {


    @Test
    void getAbility() {
        TileSets tileSets = new TileSets();
        GameBoard board = new GameBoard(tileSets, 2);

        Player me = new Player("prova", board);
        Component batt = new BatteryComp(3);
        assertEquals(3, batt.getAbility());

    }

    @Test
    void setAbility() {
        TileSets tileSets = new TileSets();
        GameBoard board = new GameBoard(tileSets, 2);


        Player me = new Player("prova", board);
        Component batt = new BatteryComp(3);
        assertEquals(2, batt.setAbility());

    }
}
