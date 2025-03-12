package org.example.galaxy_trucker;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HourglassTest {


    @Test
    void startTimer() throws InterruptedException {
        Hourglass hrg = new Hourglass(1);

        hrg.StartTimer();
        hrg.StartTimer();
        //assertEquals(0, hrg.getUsages());

    }
}