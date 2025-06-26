package org.example.galaxy_trucker.Model.Boards;

import org.example.galaxy_trucker.ClientServer.Client;
import org.example.galaxy_trucker.ClientServer.RMI.RMIClient;
import org.example.galaxy_trucker.Controller.GameController;
import org.example.galaxy_trucker.ClientServer.GamesHandler;
import org.example.galaxy_trucker.Controller.VirtualView;
import org.example.galaxy_trucker.Model.Connectors.UNIVERSAL;
import org.example.galaxy_trucker.Model.Game;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.Tiles.MainCockpitComp;
import org.example.galaxy_trucker.Model.Tiles.Tile;
import org.example.galaxy_trucker.NewTestSetupHelper;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.PrintWriter;

import static org.junit.jupiter.api.Assertions.*;

/**
 * test the playerBoard again for betetr coverge
 */
class GameBoardTest2 {


    /**
     * tests another board creation
     * @throws IOException
     */
    @Test
    public void positioning() throws IOException {


        GamesHandler gh = new GamesHandler();
        Game game = new Game(2, "g1");
        GameController gc = new GameController(game.getGameID(), game, gh, game.getLv(), 4);
        NewTestSetupHelper helper = new NewTestSetupHelper();

        Player p1 = new Player();
        p1.setId("passos");
        game.NewPlayer(p1);
        p1.setBoards(game.getGameBoard());
        Player p2 = new Player();
        p2.setId("passos2");
        game.NewPlayer(p2);
        p2.setBoards(game.getGameBoard());
        VirtualView vv1 = new VirtualView(p1.GetID(), game.getGameID(), new RMIClient(new Client()), new PrintWriter(System.out));
        VirtualView vv2 = new VirtualView(p1.GetID(), game.getGameID(), new RMIClient(new Client()), null);
        vv1.setDisconnected(true);
        vv2.setDisconnected(true);
        assertTrue(p1.getmyPlayerBoard().checkValidity());
        assertTrue(p2.getmyPlayerBoard().checkValidity());
        GameBoard gb = game.getGameBoard();

        p1.setPhaseListener(vv1);
        p1.setReadyListener(gc);
        p1.getmyPlayerBoard().setListener(vv1);
        p1.setHandListener(vv1);
        p1.getCommonBoard().getTilesSets().setListeners(vv1);
        p1.setCardListner(vv1);
        gc.getVirtualViewMap().put(p1.GetID(), vv1);
        gh.getGameControllerMap().put(p1.GetID(), gc);

        p2.setPhaseListener(vv1);
        p2.setReadyListener(gc);
        p2.getmyPlayerBoard().setListener(vv1);
        p2.setHandListener(vv1);
        p2.getCommonBoard().getTilesSets().setListeners(vv1);
        p2.setCardListner(vv1);
        gc.getVirtualViewMap().put(p2.GetID(), vv1);
        gh.getGameControllerMap().put(p2.GetID(), gc);

        gc.setLobbyListener(gh);

        p1.setMyPlance(helper.createInitializedBoard1());
        p1.getmyPlayerBoard().insertTile(new Tile(new MainCockpitComp(), UNIVERSAL.INSTANCE, UNIVERSAL.INSTANCE, UNIVERSAL.INSTANCE, UNIVERSAL.INSTANCE), 6 ,6, false);
        p1.setFinishListener(gc);
        assertTrue(p1.getmyPlayerBoard().checkValidity());

        p2.setMyPlance(helper.createInitializedBoard1());
        p2.getmyPlayerBoard().insertTile(new Tile(new MainCockpitComp(), UNIVERSAL.INSTANCE, UNIVERSAL.INSTANCE, UNIVERSAL.INSTANCE, UNIVERSAL.INSTANCE), 6 ,6, false);
        p2.setFinishListener(gc);
        assertTrue(p2.getmyPlayerBoard().checkValidity());



//________________________________________________________________________________________________________________________________________________________________________________________



        gb.SetStartingPosition(p1,1);
        gb.SetStartingPosition(p2,2);






















    }
}