package org.example.galaxy_trucker.Model.Cards;

import org.example.galaxy_trucker.ClientServer.Client;
import org.example.galaxy_trucker.ClientServer.RMI.RMIClient;
import org.example.galaxy_trucker.Controller.FlightController;
import org.example.galaxy_trucker.Controller.GameController;
import org.example.galaxy_trucker.Controller.GamesHandler;
import org.example.galaxy_trucker.Controller.VirtualView;
import org.example.galaxy_trucker.Model.Boards.GameBoard;
import org.example.galaxy_trucker.Model.Connectors.UNIVERSAL;
import org.example.galaxy_trucker.Model.Game;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.Tiles.MainCockpitComp;
import org.example.galaxy_trucker.Model.Tiles.Tile;
import org.example.galaxy_trucker.NewTestSetupHelper;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class CardTest {

    @Test
    void testCard() throws IOException {

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



//-------------------------------------------------------------------------------------------------------------------------------

        AbandonedShip abandonedShip = new AbandonedShip(1, 3, 1, 1, game.getGameBoard());
        abandonedShip.sendTypeLog();
        abandonedShip.getTotHumans();
        abandonedShip.getCurrentPlayer();
        abandonedShip.getRequirement();
        abandonedShip.setRequirement(1);
        abandonedShip.getReward();
        abandonedShip.setReward(2);

//-------------------------------------------------------------------------------------------------------------------------------

        AbandonedStation abandonedStation = new AbandonedStation();






    }

}