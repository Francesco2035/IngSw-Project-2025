package org.example.galaxy_trucker.Model.Goods;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * tests a good instance
 */
class GREENTest {

    /**
     * tests if the value of goods is correct
     */
    @Test
    void GREENTest() {
        GREEN green = new GREEN();
        assertEquals(2, green.getValue());
    }

}