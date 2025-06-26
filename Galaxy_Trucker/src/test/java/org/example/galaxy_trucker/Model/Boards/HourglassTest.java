package org.example.galaxy_trucker.Model.Boards;

import org.example.galaxy_trucker.ClientServer.Client;
import org.example.galaxy_trucker.ClientServer.RMI.RMIClient;
import org.example.galaxy_trucker.Commands.FinishBuildingCommand;
import org.example.galaxy_trucker.Controller.FlightController;
import org.example.galaxy_trucker.Controller.GameController;
import org.example.galaxy_trucker.Controller.GamesHandler;
import org.example.galaxy_trucker.Controller.VirtualView;
import org.example.galaxy_trucker.Model.Connectors.UNIVERSAL;
import org.example.galaxy_trucker.Model.Game;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.Tiles.MainCockpitComp;
import org.example.galaxy_trucker.Model.Tiles.Tile;
import org.example.galaxy_trucker.NewTestSetupHelper;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.*;

class HourglassTest {

    @Test
    void testHourGlass() throws IOException, InterruptedException {

        Hourglass hg = new Hourglass();
        hg.stopHourglass();

        Hourglass hg2 = new Hourglass(2);
        hg2.startHourglass();

//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------


        Game game = new Game(2, "g1");
        GameController gc = new GameController(game.getGameID(), game, new GamesHandler(), game.getLv(), 4);
        NewTestSetupHelper helper = new NewTestSetupHelper();

        Player p1 = new Player();
        p1.setId("passos");
        Player p2 = new Player();
        p2.setId("passos2");
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


//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------


        p1.StartTimer();
        sleep(100000);
        p1.StartTimer();
        sleep(100000);

        p1.EndConstruction(1);
        FinishBuildingCommand fbc = new FinishBuildingCommand(1, game.getID(), p1.GetID(), game.getLv(), "FinishBuilding1", null);
        try{
            fbc.execute(p1);
        } catch (Exception e){
            assertTrue(e.getMessage().contains("already"));
        }
        try {
            p1.StartTimer();
        } catch (RuntimeException e) {
            assertEquals("You need to finish your ship before using the hourglass", e.getMessage());
        }

//        hg2.setUsages(0);
//        p1.EndConstruction(1);
//        FinishBuildingCommand fbc2 = new FinishBuildingCommand(0, game.getID(), p1.GetID(), game.getLv(), "FinishBuilding1", null);
//        fbc2.execute(p1);
//        p1.StartTimer();
//
//
//


    }

}