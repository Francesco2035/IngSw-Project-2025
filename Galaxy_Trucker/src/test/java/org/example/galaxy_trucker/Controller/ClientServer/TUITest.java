package org.example.galaxy_trucker.Controller.ClientServer;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class TUITest {

    @Test
    void test() throws IOException {
        TUI tui = new TUI();
        tui.setGameboard(2);
        tui.printGameboard();
    }

}