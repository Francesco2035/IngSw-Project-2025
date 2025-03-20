package org.example.galaxy_trucker.Model.Boards;

import org.example.galaxy_trucker.Model.GAGen;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class PlayerBoardTest {

    @Test
    void classifyTle() throws IOException {


        GAGen g = new GAGen();
        PlayerBoard prova = new PlayerBoard(2);


        for (int i = 0; i < 151; i++) {
            prova.classifyTle(g.getTilesDeck().get(i));
        }
    }
}