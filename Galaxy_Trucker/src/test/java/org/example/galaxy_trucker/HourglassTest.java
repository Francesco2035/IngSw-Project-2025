package org.example.galaxy_trucker;

import org.example.galaxy_trucker.Model.Boards.Hourglass;
import org.junit.jupiter.api.Test;

class HourglassTest {


    @Test
    void startTimer() throws InterruptedException {
        Hourglass hrg = new Hourglass(1);

        hrg.StartTimer();
        hrg.StartTimer();
        //assertEquals(0, hrg.getUsages());

    }
}