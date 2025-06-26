package org.example.galaxy_trucker.Model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**t
 * tests the integer pair method
 */
class IntegerPairTest {
    /**
     * tests the return values of the getter and setter methods
     */
    @Test
    void testIntegerPair() {

        IntegerPair integerPair = new IntegerPair();
        integerPair.setFirst(5);
        integerPair.setSecond(10);
        assertEquals(5, integerPair.getFirst());
        assertEquals(10, integerPair.getSecond());

        assertEquals("IntegerPair{x=5, y=10}" , integerPair.toString());

        assertTrue(integerPair.equals(integerPair));
        assertTrue(integerPair.equals(new IntegerPair(5, 10)));
        assertFalse(integerPair.equals(new IntegerPair(5, 9)));

        integerPair.setValue(1, 2);

        integerPair.hashCode();



    }

}