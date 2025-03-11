package org.example.galaxy_trucker;

import javafx.util.Pair;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    @Test
    void consumeEnergyFrom() {

        GameBoard board = new GameBoard();

        Player meplayer = new Player("codiscePersona_255", 2, board);
        PlayerPlance provaPlance = new PlayerPlance(2);
//        meplayer.setMyPlance(provaPlance);
        BatteryComp batt = new BatteryComp(3);
        ArrayList<Tile> batteryTiles = new ArrayList<>();
        Pair<Integer, Integer> coords = new Pair<>(3, 4);
        Tile battTile = new Tile(coords, batt, Connector.SINGLE, Connector.DOUBLE, Connector.NONE, Connector.NONE);
        batteryTiles.add(battTile);
        provaPlance.insertTile(battTile, 3, 4);

//        batt.setPrivEnergy(10);
        meplayer.consumeEnergyFrom(3, 4);

//        assertEquals(9, batt.getPrivEnergy());
    }

    @Test
    void fireCannon() {
    }

    @Test
    void startEngine() {
    }
}