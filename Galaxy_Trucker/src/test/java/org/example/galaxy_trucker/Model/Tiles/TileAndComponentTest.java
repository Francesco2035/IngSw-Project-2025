package org.example.galaxy_trucker.Model.Tiles;

import org.example.galaxy_trucker.ClientServer.Client;
import org.example.galaxy_trucker.ClientServer.RMI.RMIClient;
import org.example.galaxy_trucker.Controller.FlightController;
import org.example.galaxy_trucker.Controller.GameController;
import org.example.galaxy_trucker.Controller.GamesHandler;
import org.example.galaxy_trucker.Controller.VirtualView;
import org.example.galaxy_trucker.Model.Boards.Actions.GetEnginePower;
import org.example.galaxy_trucker.Model.Boards.GameBoard;
import org.example.galaxy_trucker.Model.Connectors.UNIVERSAL;
import org.example.galaxy_trucker.Model.Game;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.GiveSpeed;
import org.example.galaxy_trucker.NewTestSetupHelper;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class TileAndComponentTest {



    @Test
    public void ComponentTest() throws IOException {



        Game game = new Game(2, "g1");
        GameController gc = new GameController(game.getGameID(), game, new GamesHandler(), game.getLv(), 4);
        NewTestSetupHelper helper = new NewTestSetupHelper();

        Player p1 = new Player();
        p1.setId("passos");
        game.NewPlayer(p1);
        p1.setBoards(game.getGameBoard());
        VirtualView vv = new VirtualView(p1.GetID(), game.getGameID(), new RMIClient(new Client()), null);
        vv.setDisconnected(true);
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
        assertTrue(p1.getmyPlayerBoard().checkValidity());



        //hotwaterheater

        //type 2
        p1.getmyPlayerBoard().insertTile(game.getGag().getTilesDeck().get(95), 6, 7, false);
        HotWaterHeater hotWaterHeater = (HotWaterHeater) game.getGag().getTilesDeck().get(95).getComponent();
        hotWaterHeater.getEnginePower();
        hotWaterHeater.rotate(true);
        hotWaterHeater.rotate(false);
        p1.getmyPlayerBoard().removeTile(6, 7);
        hotWaterHeater.clone(p1.getmyPlayerBoard());
        hotWaterHeater.controlValidity(p1.getmyPlayerBoard(), 0, 0);
        hotWaterHeater.accept(new GetEnginePower(1), new GiveSpeed());
        assertEquals(2, hotWaterHeater.getType());



        //type 1
        p1.getmyPlayerBoard().insertTile(game.getGag().getTilesDeck().get(85), 6, 7, false);
        HotWaterHeater hotWaterHeater2 = (HotWaterHeater) game.getGag().getTilesDeck().get(85).getComponent();
        hotWaterHeater2.getEnginePower();
        hotWaterHeater2.rotate(true);
        hotWaterHeater2.rotate(false);
        p1.getmyPlayerBoard().removeTile(6, 7);
        hotWaterHeater2.clone(p1.getmyPlayerBoard());
        hotWaterHeater2.controlValidity(p1.getmyPlayerBoard(), 0, 0);
        hotWaterHeater2.accept(new GetEnginePower(1), new GiveSpeed());
        assertEquals(1, hotWaterHeater2.getType());





        //alienaddon1


        //type 0
        p1.getmyPlayerBoard().insertTile(game.getGag().getTilesDeck().get(132), 6, 7, false);
        AlienAddons alienaddon = (AlienAddons) game.getGag().getTilesDeck().get(132).getComponent();
        alienaddon.rotate(true);
        alienaddon.rotate(false);
        p1.getmyPlayerBoard().removeTile(6, 7);
        alienaddon.clone(p1.getmyPlayerBoard());
        alienaddon.controlValidity(p1.getmyPlayerBoard(), 0, 0);
        assertEquals(0, alienaddon.getType());



        //type 1
        p1.getmyPlayerBoard().insertTile(game.getGag().getTilesDeck().get(139), 6, 7, false);
        AlienAddons alienaddon1 = (AlienAddons) game.getGag().getTilesDeck().get(139).getComponent();
        alienaddon1.rotate(true);
        alienaddon1.rotate(false);
        p1.getmyPlayerBoard().removeTile(6, 7);
        alienaddon1.clone(p1.getmyPlayerBoard());
        alienaddon1.controlValidity(p1.getmyPlayerBoard(), 0, 0);
        assertEquals(1, alienaddon1.getType());





    }


}