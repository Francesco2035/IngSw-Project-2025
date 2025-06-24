package org.example.galaxy_trucker.Controller;

import org.example.galaxy_trucker.ClientServer.Client;
import org.example.galaxy_trucker.ClientServer.GamesHandler;
import org.example.galaxy_trucker.ClientServer.RMI.RMIClient;
import org.example.galaxy_trucker.Commands.*;
import org.example.galaxy_trucker.Model.Boards.GameBoard;
import org.example.galaxy_trucker.Model.Connectors.UNIVERSAL;
import org.example.galaxy_trucker.Model.Game;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.BaseState;
import org.example.galaxy_trucker.Model.PlayerStates.ChoosePosition;
import org.example.galaxy_trucker.Model.Tiles.MainCockpitComp;
import org.example.galaxy_trucker.Model.Tiles.Tile;
import org.example.galaxy_trucker.NewTestSetupHelper;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ControllerTest {



    @Test
    void contrTest() throws IOException {


        GamesHandler gamesHandler = new GamesHandler();
        Game game = new Game(2, "g1");
        GameController gc = new GameController(game.getGameID(), game, gamesHandler, game.getLv(), 4);
        NewTestSetupHelper helper = new NewTestSetupHelper();

        Player p1 = new Player();
        p1.setId("passos");
        game.NewPlayer(p1);
        p1.setBoards(game.getGameBoard());
        VirtualView vv = new VirtualView(p1.GetID(), game.getGameID(), new RMIClient(new Client()), null);
        vv.setDisconnected(true);
        assertTrue(p1.getmyPlayerBoard().checkValidity());
        GameBoard Gboard = game.getGameBoard();

        p1.setPhaseListener(vv);
        p1.setReadyListener(gc);
        p1.getmyPlayerBoard().setListener(vv);
        p1.setHandListener(vv);
        p1.getCommonBoard().getTilesSets().setListeners(vv);
        p1.setCardListner(vv);
        gc.getVirtualViewMap().put(p1.GetID(), vv);
        gamesHandler.getGameControllerMap().put(p1.GetID(), gc);

        p1.setMyPlance(helper.createInitializedBoard1());
        p1.getmyPlayerBoard().insertTile(new Tile(new MainCockpitComp(), UNIVERSAL.INSTANCE, UNIVERSAL.INSTANCE, UNIVERSAL.INSTANCE, UNIVERSAL.INSTANCE), 6 ,6, false);
        p1.setFinishListener(gc);
        gc.setLobbyListener(gamesHandler);

//        FlightController c1 = new FlightController(p1, game.getGameID(), gc, false);
//        gc.getControllerMap().put(p1.GetID(), c1);



//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------



        //costruttore
        CheckValidityController c2 = new CheckValidityController(p1, game.getGameID(), true);
        p1.setState(new BaseState());
        c2.action(new AcceptCommand(game.getID(), p1.GetID(), game.getLv(), "mhanz", true, null), gc);

        p1.setState(new BaseState());
        p1.setReadyListener(gc);
        c2.action(new ReadyCommand(game.getID(), p1.GetID(), game.getLv(), "Ready", true, null), gc);

        c2.DefaultAction(gc);


        p1.getmyPlayerBoard().insertTile(game.getGag().getTilesDeck().get(17), 8 ,7, false);
        c2.nextState(gc);


        p1.getmyPlayerBoard().removeTile(8 ,7);



//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------



        ChoosePositionController c3 = new ChoosePositionController(p1, game.getGameID(), true);
        p1.setState(new ChoosePosition());
        c3.action(new LoginCommand(game.getID(), p1.GetID(), game.getLv(), "login",3), gc);

        c3.action(new FinishBuildingCommand(0, game.getID(), p1.GetID(), game.getLv(), "mhanz", null), gc);
        c3.action(new FinishBuildingCommand(1, game.getID(), p1.GetID(), game.getLv(), "mhanz", null), gc);


        c3.DefaultAction(gc); // verificare qui come prima quale può essere la IOException da causare per ottenere questa branch


//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------


        Controller c4 = new Controller() { // come impostare il curplayer?
            @Override
            public void nextState(GameController gc) {

            }
        };

        p1.setState(new BaseState());
//        c4.action(new ReadyCommand(game.getID(), p1.GetID(), game.getLv(), "mhanz", true, null), gc); //già testato in teoria
//        c4.DefaultAction(gc); //Cannot invoke "org.example.galaxy_trucker.Model.Player.getPlayerState()" because "this.curPlayer" is null

        c4.setExceptionListener(vv);
        c4.sendException(new IOException("test"));
        c4.removeExceptionListener();
        c4.setDisconnected(true);



//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------


        DisconnectedClient dc = new DisconnectedClient(new UUID(32,0), new RMIClient(new Client()));


//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------


        FlightController c6 = new FlightController(p1, game.getID(), gc, true);

        c6.action(new AcceptCommand(game.getID(), p1.GetID(), game.getLv(), "mhanz", true, null), gc);

        vv.setDisconnected(false);
        c6.nextState(gc);
        vv.setDisconnected(true);
        c6.nextState(gc);


//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------


        LoginController c7 = new LoginController(p1, game.getID());
        c7.nextState(gc);


//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------



        PrepController c8 = new PrepController(p1, game.getID(), gc, true);


        p1.getmyPlayerBoard().insertTile(game.getGag().getTilesDeck().get(17), 8 ,7, false);
        c8.nextState(gc);

        p1.getmyPlayerBoard().removeTile(8 ,7);
        c8.nextState(gc);


        c8.onFinish();




//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------


        PostPrepController c9 = new PostPrepController(p1, game.getID(), true);
        c9.DefaultAction(gc);






//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------

//TESTARLO COME ULTIMO IN QUANTO MANDA IL GAMEOVER
        //gameover
        CardsController c1 = new CardsController(p1, game.getGameID(), false);
        gc.getControllerMap().put(p1.GetID(), c1);

        vv.setDisconnected(false);
        c1.nextState(gc);

// fixare la fulladventure





//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------




    }

}