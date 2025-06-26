package org.example.galaxy_trucker.View.TUI;

import org.example.galaxy_trucker.View.ClientModel.States.LoginClient;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;

/**
 * test the aascii art
 */
class ASCII_ARTTest {
    //QUESTO TEST NON SERVE A NIENTE, ERA GIUSTO PER VISUALIZZARE VELOCEMENTE LE SCRITTE

    /**
     * tests the acsi art of the TUI names
     */
    @Test
    public void test() {
        System.out.println(ASCII_ART.AbandonedStation);

        System.out.println(ASCII_ART.largeMeteor);
    }

    /**
     *tests the ascii art of the TUI scoreboard
     * @throws IOException
     */
    @Test
    public void testScoreBoard() throws IOException {
        TUI tui = new TUI(new LoginClient());
        HashMap<String, Integer> score = new HashMap<>();
        score.put("P1", -1);
        score.put("P2", 12);
        score.put("P3", 120);
        score.put("P4", 12);
        System.out.println(tui.formatScoreboard(score));

    }

}