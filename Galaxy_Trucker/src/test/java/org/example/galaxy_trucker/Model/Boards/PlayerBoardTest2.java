package org.example.galaxy_trucker.Model.Boards;

import org.example.galaxy_trucker.ClientServer.Client;
import org.example.galaxy_trucker.ClientServer.RMI.RMIClient;
import org.example.galaxy_trucker.Controller.FlightController;
import org.example.galaxy_trucker.Controller.GameController;
import org.example.galaxy_trucker.Controller.GamesHandler;
import org.example.galaxy_trucker.Controller.VirtualView;
import org.example.galaxy_trucker.Model.Connectors.UNIVERSAL;
import org.example.galaxy_trucker.Model.Game;
import org.example.galaxy_trucker.Model.Goods.BLUE;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.Tiles.MainCockpitComp;
import org.example.galaxy_trucker.Model.Tiles.Tile;
import org.example.galaxy_trucker.NewTestSetupHelper;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class PlayerBoardTest2 {

    @Test
    void testPlayerBoard() throws IOException {


        Game game = new Game(2, "g1");
        GameController gc = new GameController(game.getGameID(), game, new GamesHandler(), game.getLv(), 4);
        NewTestSetupHelper helper = new NewTestSetupHelper();

        Player p1 = new Player();
        p1.setId("passos");
        game.NewPlayer(p1);
        p1.setBoards(game.getGameBoard());
        VirtualView vv = new VirtualView(p1.GetID(), game.getGameID(), new RMIClient(new Client()), null);
        vv.setDisconnected(true);
        assertTrue(p1.getmyPlayerBoard().checkValidity());
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


        PlayerBoard pb1 = p1.getmyPlayerBoard();
        pb1.setBufferGoods(new ArrayList<>());
        pb1.setHousingUnits(null);
        pb1.setHotWaterHeaters(null);
        pb1.setPlasmaDrills(null);
        pb1.setStorages(null);
        pb1.setAlienAddons(null);
        pb1.setShieldGenerators(null);
        pb1.setPowerCenters(null);
        pb1.AddGoodInBuffer(null);
        pb1.getBuffer();


        try {
            pb1.pullFromBufferGoods(0);
        }catch (Exception e){
            assertEquals("BufferGoods is empty", e.getMessage());
        }


        pb1.AddGoodInBuffer(new BLUE());

        try {
            pb1.pullFromBufferGoods(10);
        }catch (Exception e){
            assertEquals("This position in the BufferGoods does not exist", e.getMessage());
        }


        assertEquals(1, pb1.pullFromBufferGoods(0).getValue());

        assertFalse(pb1.getBroken());
        assertTrue(pb1.getValid());


        pb1.getToRemovePB();

    }

}