package org.example.galaxy_trucker;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HourglassTest {

    //very silly test
    @Test
    void startTimer() throws InterruptedException {
        Hourglass hrg = new Hourglass(1);

        hrg.StartTimer();
        assertEquals(true, hrg.StartTimer());

    }
}