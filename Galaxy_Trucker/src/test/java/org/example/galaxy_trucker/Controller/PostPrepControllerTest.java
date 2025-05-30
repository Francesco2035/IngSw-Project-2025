package org.example.galaxy_trucker.Controller;

import org.example.galaxy_trucker.ClientServer.Client;
import org.example.galaxy_trucker.ClientServer.RMI.RMIClient;
import org.example.galaxy_trucker.Model.Boards.GameBoard;
import org.example.galaxy_trucker.Model.Game;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.TestSetupHelper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class PostPrepControllerTest {


    static Game game;
    static GameBoard Gboard;

    static {
        try {
            game = new Game(2, "testPostPrepController");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static GameController gc = new GameController(game.getGameID(), game, new GamesHandler());
    static Player p1 = new Player();
    static VirtualView vv;
    PrepController c1 = new PrepController(p1, game.getGameID(), gc, false);

    @BeforeAll
    public static void init() throws IOException {

        Game game = new Game(2, "testPostPrepController");
        p1 = new Player();
        p1.setId("passos");
        game.NewPlayer(p1);
        p1.setBoards(game.getGameBoard());
        p1.setMyPlance(TestSetupHelper.createInitializedBoard1());
        vv = new VirtualView(p1.GetID(), game.getGameID(), new RMIClient(new Client()), null);
        vv.setDisconnected(true);
        assertTrue(p1.getmyPlayerBoard().checkValidity());
        TestSetupHelper.HumansSetter1(p1.getmyPlayerBoard());
        Gboard = game.getGameBoard();

        p1.setPhaseListener(vv);
        p1.getmyPlayerBoard().setListener(vv);
        p1.setHandListener(vv);
        p1.getCommonBoard().getTilesSets().setListeners(vv);
        p1.setCardListner(vv);


    }

    @Test
    public void testPostPrepController() {

    }

    @Test
    void nextState() {
    }
}