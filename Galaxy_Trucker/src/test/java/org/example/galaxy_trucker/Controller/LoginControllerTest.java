package org.example.galaxy_trucker.Controller;

import org.example.galaxy_trucker.Commands.ReadyCommand;
import org.example.galaxy_trucker.Model.Boards.GameBoard;
import org.example.galaxy_trucker.Model.Game;
import org.example.galaxy_trucker.Model.Player;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class LoginControllerTest {


    static Game game;
    static GameBoard Gboard;

    static {
        try {
            game = new Game(2, "testLoginController");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static Player p1 = new Player();

    LoginController c1 = new LoginController(p1, game.getGameID());

    @BeforeAll
    public static void init() throws IOException {
        Game game = new Game(2, "testLoginController");

        p1 = new Player();
        p1.setId("passos");
        game.NewPlayer(p1);
//        p1.setMyPlance(TestSetupHelper.createInitializedBoard1());

//        assertTrue(p1.getmyPlayerBoard().checkValidity());

//        TestSetupHelper.HumansSetter1(p1.getmyPlayerBoard());
        Gboard = game.getGameBoard();

        Gboard.SetStartingPosition(p1);

    }




    @Test
    public void testLoginController() {

        ReadyCommand cmd1 = new ReadyCommand("testLoginController", "passos", 2, "Ready", true, null);
        cmd1.execute(p1);
        assertTrue(p1.GetReady());

        ReadyCommand cmd2 = new ReadyCommand("testLoginController", "passos", 2, "Quit", true, null);
        cmd2.execute(p1);
        assertTrue(p1.GetReady()); // da aggiungersi il fatto che quittando il player viene eliminato interamente e con lui anche questo attributo

        ReadyCommand cmd3 = new ReadyCommand("testLoginController", "passos", 2, "Ready", true, null);
        cmd3.execute(p1);
        assertTrue(p1.GetReady());
        ReadyCommand cmd4 = new ReadyCommand("testLoginController", "passos", 2, "Ready", false, null);
        cmd4.execute(p1);
        assertFalse(p1.GetReady());
////////////////////////////////////////////////////////////////////////
        ReadyCommand cmd5 = new ReadyCommand("testLoginController", "passos", 2, "Ready", true, null);
        cmd5.execute(p1);
        assertTrue(p1.GetReady());
        ReadyCommand cmd6 = new ReadyCommand("testLoginController", "passos", 2, "Ready", true, null);
        cmd6.execute(p1);
        assertTrue(p1.GetReady());
////////////////////////////////////////////////////////////////////////
        // non ha molto senso debuggare debugship in quanto è solo un comando per lo sviluppatore
    }

}