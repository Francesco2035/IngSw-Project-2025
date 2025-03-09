package org.example.galaxy_trucker;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;



public class PlayerPlanceTest {

    @Test
    public void testPlayerPlanceInitialization() {

        PlayerPlance playerPlance = new PlayerPlance(1, null);

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


        // Facciamo un controllo più ampio per alcune celle note
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                if (validPlance[x][y] != plance[x][y] ) {
                    assertEquals(validPlance[x][y], plance[x][y], "ValidPlance dovrebbe avere in posizione " + x + ", " + y + " " + validPlance[x][y]);

                }
            }
        }
    }
}

