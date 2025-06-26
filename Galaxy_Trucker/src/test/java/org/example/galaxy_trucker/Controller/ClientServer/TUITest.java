package org.example.galaxy_trucker.Controller.ClientServer;

import org.example.galaxy_trucker.ClientServer.Messages.GameBoardEvent;
import org.example.galaxy_trucker.View.ClientModel.States.LoginClient;
import org.example.galaxy_trucker.View.TUI.TUI;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/**
 * Tests to see if the TUI prints  the view correctly
 */

class TUITest {

    /**
     *tests to see if the TUI correctly creates and updates the view of multiple players
     * @throws IOException
     */

    @Test
    void test() throws IOException {
        TUI tui = new TUI(new LoginClient());
        tui.setGameboard(2);
        //tui.printGameBoard();

        tui.updateGameboard(new GameBoardEvent(0, "paluGay"));
        tui.updateGameboard(new GameBoardEvent(1, "paluMorto"));
        tui.updateGameboard(new GameBoardEvent(2    , "paluSuperMorto"));


    }

}