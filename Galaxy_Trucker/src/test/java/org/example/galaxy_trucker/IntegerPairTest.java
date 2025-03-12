package org.example.galaxy_trucker;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class IntegerPairTest {

    @Test
    void getFirst() {
        IntegerPair paired = new IntegerPair(8,11);
        assertEquals(8, paired.getFirst());
    }

    @Test
    void getSecond() {
        IntegerPair paired = new IntegerPair(8,11);
        assertEquals(11, paired.getSecond());
    }
}