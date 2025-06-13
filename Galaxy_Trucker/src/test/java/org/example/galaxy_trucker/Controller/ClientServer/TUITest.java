package org.example.galaxy_trucker.Controller.ClientServer;

import org.example.galaxy_trucker.Controller.Messages.GameBoardEvent;
import org.example.galaxy_trucker.View.ClientModel.States.LoginClient;
import org.example.galaxy_trucker.View.TUI.TUI;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class TUITest {

    @Test
    void test() throws IOException {
        TUI tui = new TUI(new LoginClient());
        tui.setGameboard(2);
        //tui.printGameboard();

        tui.updateGameboard(new GameBoardEvent(0, "paluGay"));
        tui.updateGameboard(new GameBoardEvent(1, "paluMorto"));
        tui.updateGameboard(new GameBoardEvent(2    , "paluSuperMorto"));


    }

}