package org.example.galaxy_trucker;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;



public class PlayerPlanceTest {

    @Test
    public void testPlayerPlanceInitialization() {

        PlayerPlance playerPlance = new PlayerPlance(1);

        int[][] plance = playerPlance.getValidPlance();
        assertNotNull(plance, "La matrice Plance non dovrebbe essere null");

        // Controlliamo celle di ValidPlance
        int[][] validPlance = {
                {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
                {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
                {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
                {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
                {-1, -1, -1, -1, -1, 0, -1, 0, -1, -1},
                {-1, -1, -1, -1, 0, 0, 0, 0, 0, -1},
                {-1, -1, -1, 0, 0, 0, 1, 0, 0, 0},
                {-1, -1, -1, 0, 0, 0, 0, 0, 0, 0},
                {-1, -1, -1, 0, 0, 0, -1, 0, 0, 0},
                {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1}
        };


        int[][] testPath = {
                {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
                {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
                {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
                {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
                {-1, -1, -1, -1, -1, 1, -1, 0, -1, -1},
                {-1, -1, -1, -1, 0, 0, 0, 0, 0, -1},
                {-1, -1, -1, 0, 1, 1, 1, 1, 0, 0},
                {-1, -1, -1, 0, 0, 1, 1, 0, 0, 0},
                {-1, -1, -1, 0, 0, 0, -1, 0, 0, 0},
                {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1}
        };

        playerPlance.insertTile(new Tile(new IntegerPair(6, 7), new sewerPipes(), Connector.SINGLE, Connector.UNIVERSAL,
                Connector.NONE, Connector.UNIVERSAL), 6, 7);
        playerPlance.insertTile(new Tile(new IntegerPair(6, 5), new sewerPipes(), Connector.SINGLE, Connector.UNIVERSAL,
                Connector.SINGLE, Connector.DOUBLE), 6, 5);
        playerPlance.insertTile(new Tile(new IntegerPair(7, 5), new sewerPipes(), Connector.NONE, Connector.SINGLE,
                Connector.DOUBLE, Connector.DOUBLE), 7, 5);
        playerPlance.insertTile(new Tile(new IntegerPair(6, 4), new sewerPipes(), Connector.NONE, Connector.SINGLE,
                Connector.UNIVERSAL, Connector.DOUBLE), 6, 4);
        playerPlance.insertTile(new Tile(new IntegerPair(4, 5), new sewerPipes(), Connector.NONE, Connector.SINGLE,
                Connector.DOUBLE, Connector.DOUBLE), 4, 5);




//        for (int x = 0; x < 10; x++) {
//            for (int y = 0; y < 10; y++) {
//                if (validPlance[x][y] != plance[x][y] ) {
//                    assertEquals(validPlance[x][y], plance[x][y], "ValidPlance dovrebbe avere in posizione " + x + ", " + y + " " + validPlance[x][y]);
//
//                }
//            }
//        }


    }

    @Test
    public void testPath() {
        PlayerPlance playerPlance = new PlayerPlance(1);



        playerPlance.insertTile(new Tile(new IntegerPair(6, 7), new sewerPipes(), Connector.SINGLE, Connector.UNIVERSAL,
                Connector.NONE, Connector.SINGLE), 6, 7);
        playerPlance.insertTile(new Tile(new IntegerPair(7, 7), new sewerPipes(), Connector.UNIVERSAL, Connector.SINGLE,
                Connector.UNIVERSAL, Connector.UNIVERSAL), 7, 7);
        playerPlance.insertTile(new Tile(new IntegerPair(7, 8), new sewerPipes(), Connector.SINGLE, Connector.UNIVERSAL,
                Connector.DOUBLE, Connector.DOUBLE), 7, 8);
        playerPlance.insertTile(new Tile(new IntegerPair(6, 8), new plasmaDrill(), Connector.UNIVERSAL, Connector.UNIVERSAL,
                Connector.CANNON, Connector.UNIVERSAL), 6, 8);

        playerPlance.insertTile(new Tile(new IntegerPair(5, 5), new sewerPipes(), Connector.SINGLE, Connector.UNIVERSAL,
                Connector.DOUBLE, Connector.DOUBLE), 5, 5);

        playerPlance.insertTile(new Tile(new IntegerPair(4, 5), new sewerPipes(), Connector.NONE, Connector.SINGLE,
                Connector.DOUBLE, Connector.DOUBLE), 4, 5);

        playerPlance.insertTile(new Tile(new IntegerPair(6, 5), new hotWaterHeater(), Connector.UNIVERSAL, Connector.UNIVERSAL,
                Connector.UNIVERSAL, Connector.MOTOR), 6, 5);

        playerPlance.insertTile(new Tile(new IntegerPair(7, 6), new hotWaterHeater(), Connector.UNIVERSAL, Connector.UNIVERSAL,
                Connector.MOTOR, Connector.UNIVERSAL), 7, 6);

//        playerPlance.insertTile(new Tile(new IntegerPair(7, 5), new hotWaterHeater(), Connector.UNIVERSAL, Connector.UNIVERSAL,
//                Connector.UNIVERSAL, Connector.MOTOR), 7, 5);

        if (playerPlance.checkValidity()) {
            System.out.println("Path test passed.");
        }
        else {
            System.out.println("Path test failed.");
        }



    }
}

