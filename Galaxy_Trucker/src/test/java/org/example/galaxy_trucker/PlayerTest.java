package org.example.galaxy_trucker;

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
import org.junit.jupiter.api.Test;

import java.io.IOException;

class PlayerTest {

    @Test
    void PickCardTest() throws IOException {



        Game game;
        GameBoard Gboard;
        GameController gc;
        Player p1;
        VirtualView vv;
        FlightController c1;
        GamesHandler gh;

        game = new Game(2, "player test");
        gc = new GameController(game.getGameID(), game, new GamesHandler(), game.getLv(), 4);
        gh = new GamesHandler();

        p1 = new Player();
        p1.setId("passos");
        game.NewPlayer(p1);
        p1.setBoards(game.getGameBoard());
        vv = new VirtualView(p1.GetID(), game.getGameID(), new RMIClient(new Client()), null);
        vv.setDisconnected(true);
        Gboard = game.getGameBoard();
        c1 = new FlightController(p1, game.getGameID(), gc, false);
        gc.getControllerMap().put(p1.GetID(), c1);

        p1.setPhaseListener(vv);
        p1.setReadyListener(gc);
        gc.setLobbyListener(gh);
        p1.getmyPlayerBoard().setListener(vv);
        p1.setHandListener(vv);
        p1.getCommonBoard().getTilesSets().setListeners(vv);
        p1.setCardListner(vv);
        gc.getVirtualViewMap().put(p1.GetID(), vv);
        p1.setFinishListener(gc);


        p1.setMyPlance(TestSetupHelper.createInitializedBoard1());
        p1.getmyPlayerBoard().insertTile(new Tile(new MainCockpitComp(), UNIVERSAL.INSTANCE, UNIVERSAL.INSTANCE, UNIVERSAL.INSTANCE, UNIVERSAL.INSTANCE), 6 ,6, false);





//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------





        p1.RollDice();
        p1.GetHasActed();






        p1.getmyPlayerBoard().setCredits(2);

        p1.getmyPlayerBoard().finishRace(true);
        p1.finishRace(4, "fine");

    }


}