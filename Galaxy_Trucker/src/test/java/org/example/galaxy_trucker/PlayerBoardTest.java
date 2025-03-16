package org.example.galaxy_trucker;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;



public class PlayerBoardTest {

    @Test
    public void testPlayerBoardInitialization() {

        PlayerBoard playerBoard = new PlayerBoard(1);

        int[][] Board = playerBoard.getValidPlayerBoard();
        assertNotNull(Board, "La matrice Board non dovrebbe essere null");

        // Controlliamo celle di ValidBoard
        int[][] validBoard = {
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

        playerBoard.insertTile(new Tile(new IntegerPair(6, 7), new sewerPipes(), Connector.SINGLE, Connector.UNIVERSAL,
                Connector.NONE, Connector.UNIVERSAL), 6, 7);
        playerBoard.insertTile(new Tile(new IntegerPair(6, 5), new sewerPipes(), Connector.SINGLE, Connector.UNIVERSAL,
                Connector.SINGLE, Connector.DOUBLE), 6, 5);
        playerBoard.insertTile(new Tile(new IntegerPair(7, 5), new sewerPipes(), Connector.NONE, Connector.SINGLE,
                Connector.DOUBLE, Connector.DOUBLE), 7, 5);
        playerBoard.insertTile(new Tile(new IntegerPair(6, 4), new sewerPipes(), Connector.NONE, Connector.SINGLE,
                Connector.UNIVERSAL, Connector.DOUBLE), 6, 4);
        playerBoard.insertTile(new Tile(new IntegerPair(4, 5), new sewerPipes(), Connector.NONE, Connector.SINGLE,
                Connector.DOUBLE, Connector.DOUBLE), 4, 5);




//        for (int x = 0; x < 10; x++) {
//            for (int y = 0; y < 10; y++) {
//                if (validBoard[x][y] != Board[x][y] ) {
//                    assertEquals(validBoard[x][y], Board[x][y], "ValidBoard dovrebbe avere in posizione " + x + ", " + y + " " + validBoard[x][y]);
//
//                }
//            }
//        }


    }

    @Test
    public void testPath() {
        PlayerBoard playerBoard = new PlayerBoard(2);



        playerBoard.insertTile(new Tile(new IntegerPair(6, 7), new sewerPipes(), Connector.SINGLE, Connector.UNIVERSAL,
                Connector.NONE, Connector.SINGLE), 6, 7);
        playerBoard.insertTile(new Tile(new IntegerPair(7, 7), new sewerPipes(), Connector.UNIVERSAL, Connector.SINGLE,
                Connector.UNIVERSAL, Connector.UNIVERSAL), 7, 7);
        playerBoard.insertTile(new Tile(new IntegerPair(7, 8), new sewerPipes(), Connector.SINGLE, Connector.UNIVERSAL,
                Connector.DOUBLE, Connector.DOUBLE), 7, 8);
        playerBoard.insertTile(new Tile(new IntegerPair(6, 8), new plasmaDrill(), Connector.UNIVERSAL, Connector.UNIVERSAL,
                Connector.CANNON, Connector.UNIVERSAL), 6, 8);

        playerBoard.insertTile(new Tile(new IntegerPair(5, 5), new sewerPipes(), Connector.SINGLE, Connector.UNIVERSAL,
                Connector.DOUBLE, Connector.DOUBLE), 5, 5);

        playerBoard.insertTile(new Tile(new IntegerPair(4, 5), new sewerPipes(), Connector.NONE, Connector.SINGLE,
                Connector.DOUBLE, Connector.DOUBLE), 4, 5);

        playerBoard.insertTile(new Tile(new IntegerPair(6, 5), new hotWaterHeater(), Connector.UNIVERSAL, Connector.UNIVERSAL,
                Connector.UNIVERSAL, Connector.MOTOR), 6, 5);

        playerBoard.insertTile(new Tile(new IntegerPair(7, 6), new hotWaterHeater(), Connector.UNIVERSAL, Connector.UNIVERSAL,
                Connector.MOTOR, Connector.UNIVERSAL), 7, 6);

//        playerBoard.insertTile(new Tile(new IntegerPair(7, 5), new hotWaterHeater(), Connector.UNIVERSAL, Connector.UNIVERSAL,
//                Connector.UNIVERSAL, Connector.MOTOR), 7, 5);

        if (playerBoard.checkValidity()) {
            System.out.println("Path test passed.");
        }
        else {
            System.out.println("Path test failed.");
        }



    }
}

