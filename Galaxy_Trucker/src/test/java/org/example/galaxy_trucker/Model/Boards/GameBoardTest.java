package org.example.galaxy_trucker.Model.Boards;

import org.example.galaxy_trucker.ClientServer.Client;
import org.example.galaxy_trucker.ClientServer.RMI.RMIClient;
import org.example.galaxy_trucker.Controller.FlightController;
import org.example.galaxy_trucker.Controller.GameController;
import org.example.galaxy_trucker.Controller.GamesHandler;
import org.example.galaxy_trucker.Controller.Messages.GameBoardEvent;
import org.example.galaxy_trucker.Controller.VirtualView;
import org.example.galaxy_trucker.Model.Connectors.UNIVERSAL;
import org.example.galaxy_trucker.Model.Game;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.Tiles.MainCockpitComp;
import org.example.galaxy_trucker.Model.Tiles.Tile;
import org.example.galaxy_trucker.NewTestSetupHelper;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class GameBoardTest {

    @Test
    void testGameBoard() throws IOException {

//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------


        Game game = new Game(1, "g1");
        GameController gc = new GameController(game.getGameID(), game, new GamesHandler(), game.getLv(), 4);
        NewTestSetupHelper helper = new NewTestSetupHelper();

        Player p1 = new Player();
        p1.setId("passos");
        game.NewPlayer(p1);
        p1.setBoards(game.getGameBoard());
        VirtualView vv = new VirtualView(p1.GetID(), game.getGameID(), new RMIClient(new Client()), null);
        vv.setDisconnected(true);
        assertTrue(p1.getmyPlayerBoard().checkValidity());
        GameBoard Gboard = game.getGameBoard();
        FlightController c1 = new FlightController(p1, game.getGameID(), gc, false);
        gc.getControllerMap().put(p1.GetID(), c1);

        p1.setPhaseListener(vv);
        p1.setReadyListener(gc);
        p1.getmyPlayerBoard().setListener(vv);
        p1.setHandListener(vv);
        p1.getCommonBoard().getTilesSets().setListeners(vv);
        p1.setCardListner(vv);
        gc.getVirtualViewMap().put(p1.GetID(), vv);

        p1.setMyPlance(helper.createInitializedBoard1());
        p1.getmyPlayerBoard().insertTile(new Tile(new MainCockpitComp(), UNIVERSAL.INSTANCE, UNIVERSAL.INSTANCE, UNIVERSAL.INSTANCE, UNIVERSAL.INSTANCE), 6 ,6, false);


//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------


        try {
            Gboard.StartHourglass();
        } catch (Exception e) {
            assertEquals("Cannot use Hourglass in a level 1 game!", e.getMessage());
        }



//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------


        Game game2 = new Game(2, "g1");
        GameController gc2 = new GameController(game2.getGameID(), game2, new GamesHandler(), game2.getLv(), 4);
        NewTestSetupHelper helper2 = new NewTestSetupHelper();

        Player p2 = new Player();
        p2.setId("passos2");
        game2.NewPlayer(p2);
        p2.setBoards(game2.getGameBoard());
        VirtualView vv2 = new VirtualView(p2.GetID(), game2.getGameID(), new RMIClient(new Client()), null);
        vv2.setDisconnected(true);
        assertTrue(p2.getmyPlayerBoard().checkValidity());
        GameBoard Gboard2 = game2.getGameBoard();
        FlightController c2 = new FlightController(p2, game2.getGameID(), gc2, false);
        gc2.getControllerMap().put(p2.GetID(), c2);

        p2.setPhaseListener(vv2);
        p2.setReadyListener(gc2);
        p2.getmyPlayerBoard().setListener(vv2);
        p2.setHandListener(vv2);
        p2.getCommonBoard().getTilesSets().setListeners(vv2);
        p2.setCardListner(vv2);
        gc2.getVirtualViewMap().put(p2.GetID(), vv2);

        p2.setMyPlance(helper2.createInitializedBoard1());
        p2.getmyPlayerBoard().insertTile(new Tile(new MainCockpitComp(), UNIVERSAL.INSTANCE, UNIVERSAL.INSTANCE, UNIVERSAL.INSTANCE, UNIVERSAL.INSTANCE), 6 ,6, false);
        p2.setFinishListener(gc2);




        Player p3 = new Player();
        p3.setId("passos3");
        game2.NewPlayer(p3);
        p3.setBoards(game2.getGameBoard());
        VirtualView vv3 = new VirtualView(p3.GetID(), game2.getGameID(), new RMIClient(new Client()), null);
        vv2.setDisconnected(true);
        assertTrue(p3.getmyPlayerBoard().checkValidity());
        FlightController c3 = new FlightController(p3, game2.getGameID(), gc2, false);
        gc2.getControllerMap().put(p3.GetID(), c2);

        p3.setPhaseListener(vv2);
        p3.setReadyListener(gc2);
        p3.getmyPlayerBoard().setListener(vv2);
        p3.setHandListener(vv2);
        p3.getCommonBoard().getTilesSets().setListeners(vv2);
        p3.setCardListner(vv2);
        gc2.getVirtualViewMap().put(p3.GetID(), vv2);

        p3.setMyPlance(helper2.createInitializedBoard1());
        p3.getmyPlayerBoard().insertTile(new Tile(new MainCockpitComp(), UNIVERSAL.INSTANCE, UNIVERSAL.INSTANCE, UNIVERSAL.INSTANCE, UNIVERSAL.INSTANCE), 6 ,6, false);
        p3.setFinishListener(gc2);




        Player p4 = new Player();
        p4.setId("passos4");
        game2.NewPlayer(p4);
        p4.setBoards(game2.getGameBoard());
        VirtualView vv4 = new VirtualView(p4.GetID(), game2.getGameID(), new RMIClient(new Client()), null);
        vv2.setDisconnected(true);
        assertTrue(p4.getmyPlayerBoard().checkValidity());
        FlightController c4 = new FlightController(p4, game2.getGameID(), gc2, false);
        gc2.getControllerMap().put(p4.GetID(), c2);
        p4.setPhaseListener(vv2);
        p4.setReadyListener(gc2);
        p4.getmyPlayerBoard().setListener(vv2);
        p4.setHandListener(vv2);
        p4.getCommonBoard().getTilesSets().setListeners(vv2);
        p4.setCardListner(vv2);
        gc2.getVirtualViewMap().put(p4.GetID(), vv2);

        p4.setMyPlance(helper2.createInitializedBoard1());
        p4.getmyPlayerBoard().insertTile(new Tile(new MainCockpitComp(), UNIVERSAL.INSTANCE, UNIVERSAL.INSTANCE, UNIVERSAL.INSTANCE, UNIVERSAL.INSTANCE), 6 ,6, false);
        p4.setFinishListener(gc2);

//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------



        Gboard2.getHourglass().setUsages(-1);
        try {
            Gboard2.StartHourglass();
        } catch (Exception e) {
            assertEquals("No Hourglass usages left.", e.getMessage());
        }

        Gboard2.getHourglass().setUsages(1);
        p2.SetReady(false);
        try {
            Gboard2.callHourglass(p2);
        } catch (RuntimeException e){
            assertEquals("You need to finish your ship before using the hourglass", e.getMessage());
        }

        Gboard2.removePlayer(p3);
        Gboard2.abandonRace(p4, "lost",true);
        try {
            Gboard2.abandonRace(null, "lost",true);
        } catch (RuntimeException e){
            assertEquals("No value present", e.getMessage());
        }


        Gboard2.finishGame();
        Gboard2.setListeners(vv2);
        Gboard2.sendUpdates(new GameBoardEvent(2, p2.GetID()));












    }

}