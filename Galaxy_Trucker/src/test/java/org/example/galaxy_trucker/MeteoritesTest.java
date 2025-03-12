package org.example.galaxy_trucker;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class MeteoritesTest {

    @Test
    void cardEffect() {
        TileSets tileSets = new TileSets();
        GameBoard board = new GameBoard(tileSets, 2);
        IntegerPair Pair1 = new IntegerPair(3,2);
        IntegerPair Pair2 = new IntegerPair(2,1);
        IntegerPair Pair3 = new IntegerPair(2,0);
        IntegerPair Pair4 = new IntegerPair(4,2);
        IntegerPair Pair5 = new IntegerPair(3,2);


        ArrayList<IntegerPair> attacks = new ArrayList<>();
        attacks.add(Pair1);
        attacks.add(Pair2);
        attacks.add(Pair3);
        attacks.add(Pair4);
        attacks.add(Pair5);
         Meteorites TMeteorites = new Meteorites(1,0,board,attacks);
         assertEquals( 3, attacks.get(0).getFirst());
         assertEquals(2, attacks.get(1).getFirst());
         assertEquals(2, attacks.get(2).getFirst());
         assertEquals(1,attacks.get(1).getSecond());
         assertEquals(0, attacks.get(2).getSecond());
    }
}