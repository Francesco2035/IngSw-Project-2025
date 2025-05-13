package org.example.galaxy_trucker.Controller.ClientServer;

import org.example.galaxy_trucker.Controller.Messages.GameBoardEvent;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class TUITest {

    @Test
    void test() throws IOException {
        TUI tui = new TUI();
        tui.setGameboard(2);
        tui.printGameboard();

        tui.updateGameboard(new GameBoardEvent(0, "paluGay"));
        tui.updateGameboard(new GameBoardEvent(1, "paluMorto"));
        tui.updateGameboard(new GameBoardEvent(2    , "paluSuperMorto"));


    }

}