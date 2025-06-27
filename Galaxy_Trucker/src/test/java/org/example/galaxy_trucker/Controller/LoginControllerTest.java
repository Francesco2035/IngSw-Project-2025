package org.example.galaxy_trucker.Controller;

import org.example.galaxy_trucker.ClientServer.Client;
import org.example.galaxy_trucker.ClientServer.GamesHandler;
import org.example.galaxy_trucker.ClientServer.RMI.RMIClient;
import org.example.galaxy_trucker.Commands.ReadyCommand;
import org.example.galaxy_trucker.Model.Boards.GameBoard;
import org.example.galaxy_trucker.Model.Connectors.UNIVERSAL;
import org.example.galaxy_trucker.Model.Game;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.BaseState;
import org.example.galaxy_trucker.Model.Tiles.MainCockpitComp;
import org.example.galaxy_trucker.Model.Tiles.Tile;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/**
 *tests the logincontroller
 */
class LoginControllerTest {


    static Game game;
    static GameBoard Gboard;
    static GameController gc;
    static Player p1;
    static VirtualView vv;
    LoginController c1;


    /**
     * test the excepions calledand commands used by the login controller
     * @throws IOException
     */

    @Test
    public void testLoginController() throws IOException {

        game = new Game(2, "testLoginController");
        gc = new GameController(game.getGameID(), game, new GamesHandler(), game.getLv(), 4);

        p1 = new Player();
        p1.setId("passos");
        game.NewPlayer(p1);
        p1.setBoards(game.getGameBoard());
        vv = new VirtualView(p1.GetID(), game.getGameID(), new RMIClient(new Client()), null);
        vv.setDisconnected(true);
        assertTrue(p1.getmyPlayerBoard().checkValidity());
        Gboard = game.getGameBoard();
        c1 = new LoginController(p1, game.getGameID());
        gc.getControllerMap().put(p1.GetID(), c1);

        p1.setPhaseListener(vv);
        p1.setReadyListener(gc);
        p1.getmyPlayerBoard().setListener(vv);
        p1.setHandListener(vv);
        p1.getCommonBoard().getTilesSets().setListeners(vv);
        p1.setCardListner(vv);
        gc.getVirtualViewMap().put(p1.GetID(), vv);

        gc.getVirtualViewMap().put(p1.GetID(),vv);
        p1.getmyPlayerBoard().insertTile(new Tile(new MainCockpitComp(), UNIVERSAL.INSTANCE, UNIVERSAL.INSTANCE, UNIVERSAL.INSTANCE, UNIVERSAL.INSTANCE), 6 ,6, false);


//------------------------------------------------------------------------------------------------------------------------------
//
//        ReadyCommand cmd1 = new ReadyCommand("testLoginController", "passos", 2, "Ready", true, null);
//        cmd1.execute(p1);
//        assertTrue(p1.GetReady());
//
////        ReadyCommand cmd2 = new ReadyCommand("testLoginController", "passos", 2, "Quit", true, null);
////        cmd2.execute(p1);
////        assertTrue(p1.GetReady()); // da aggiungersi il fatto che quittando il player viene eliminato interamente e con lui anche questo attributo
//
//        ReadyCommand cmd3 = new ReadyCommand("testLoginController", "passos", 2, "Ready", true, null);
//        cmd3.execute(p1);
//        assertTrue(p1.GetReady());
//        ReadyCommand cmd4 = new ReadyCommand("testLoginController", "passos", 2, "Ready", false, null);
//        cmd4.execute(p1);
//        assertFalse(p1.GetReady());
//        ReadyCommand cmd5 = new ReadyCommand("testLoginController", "passos", 2, "Ready", true, null);
//        cmd5.execute(p1);
//        assertTrue(p1.GetReady());
//        ReadyCommand cmd6 = new ReadyCommand("testLoginController", "passos", 2, "Ready", true, null);
//        cmd6.execute(p1);
//        assertTrue(p1.GetReady());
//        // non ha molto senso debuggare debugship in quanto è solo un comando per lo sviluppatore


//--------------------------------------------------------------------------------------------------------------------------------

        p1.setState(new BaseState());

        c1.action(new ReadyCommand(game.getGameID(), p1.GetID(), game.getLv(), "Ready", false, null), gc);
        c1.action(new ReadyCommand(game.getGameID(), p1.GetID(), game.getLv(), "Ready", false, null), gc);
        assertFalse(p1.GetReady());

        c1.action(new ReadyCommand(game.getGameID(), p1.GetID(), game.getLv(), "Ready", true, null), gc);
        assertFalse(p1.GetReady());



//--------------------------------------------------------------------------------------------------------------------------------

//
//        vv.setDisconnected(false);
//        c1.nextState(gc);
//
//        vv.setDisconnected(true);
//        c1.nextState(gc);

    }
}