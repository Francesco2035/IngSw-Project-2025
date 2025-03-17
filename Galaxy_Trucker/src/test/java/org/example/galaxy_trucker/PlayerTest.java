package org.example.galaxy_trucker;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    @Test
    void consumeEnergyFrom() {

        GameBoard board = new GameBoard(null, 2);


        Player meplayer = new Player("codiscePersona_255", board);
        PlayerBoard provaPlance = new PlayerBoard(2);
//        meplayer.setMyPlance(provaPlance);
        BatteryComp batt = new BatteryComp(3);
        ArrayList<Tile> batteryTiles = new ArrayList<>();
        IntegerPair coords = new IntegerPair(3, 4);
        Tile battTile = new Tile(coords, batt, Connector.SINGLE, Connector.DOUBLE, Connector.NONE, Connector.NONE);
        batteryTiles.add(battTile);
        provaPlance.insertTile(battTile, 3, 4);

//        batt.setPrivEnergy(10);
        //meplayer.consumeEnergyFrom(coords);

//        assertEquals(9, batt.getPrivEnergy());
    }

    @Test
    void fireCannon() {
    }

    @Test
    void startEngine() {
    }

    @Test
    void rollDice() {
        int cur, min , max;
        double avg = 0;
        GameBoard board = new GameBoard(null, 2);

        Player me = new Player("codiscePersona_255", board);

        cur = me.RollDice();
        min = cur;
        max = cur;
        for(int i=0; i<=99999; i++){
            cur = me.RollDice();
            avg += cur;
            if(cur<min) min = cur;
            else if(cur>max) max = cur;
        }
        avg = avg/100000;

        if(avg>7) avg = Math.floor(avg);
        if(avg<7) avg = Math.ceil(avg);
        assertEquals(7, avg);
        assertEquals(2, min);
        assertEquals(12, max);

        //System.out.println("Avg: " + avg +"\nMin:" +min + "\nMax:" +max);

    }
}