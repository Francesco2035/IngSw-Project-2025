package org.example.galaxy_trucker.Controller;

import org.example.galaxy_trucker.ClientServer.Client;
import org.example.galaxy_trucker.ClientServer.GamesHandler;
import org.example.galaxy_trucker.ClientServer.RMI.RMIClient;
import org.example.galaxy_trucker.Messages.*;
import org.example.galaxy_trucker.Messages.PlayerBoardEvents.PlayerTileEvent;
import org.example.galaxy_trucker.Messages.PlayerBoardEvents.RewardsEvent;
import org.example.galaxy_trucker.Messages.PlayerBoardEvents.TileEvent;
import org.example.galaxy_trucker.Messages.TileSets.*;
import org.example.galaxy_trucker.Model.Boards.GameBoard;
import org.example.galaxy_trucker.Model.Connectors.NONE;
import org.example.galaxy_trucker.Model.Connectors.UNIVERSAL;
import org.example.galaxy_trucker.Model.Game;
import org.example.galaxy_trucker.Model.Goods.BLUE;
import org.example.galaxy_trucker.Model.Goods.GREEN;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.Tiles.MainCockpitComp;
import org.example.galaxy_trucker.Model.Tiles.Tile;
import org.example.galaxy_trucker.NewTestSetupHelper;
import org.example.galaxy_trucker.View.ClientModel.States.BaseStateClient;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class VirtualViewTest {


    @Test
    void vvTest() throws IOException {

        GamesHandler gh = new GamesHandler();
        Game game = new Game(2, "g1");
        GameController gc = new GameController(game.getGameID(), game, gh, game.getLv(), 4);
        NewTestSetupHelper helper = new NewTestSetupHelper();

        Player p1 = new Player();
        p1.setId("passos");
        game.NewPlayer(p1);
        p1.setBoards(game.getGameBoard());
        VirtualView vv = new VirtualView(p1.GetID(), game.getGameID(), new RMIClient(new Client()), new PrintWriter(System.out));
        VirtualView vv2 = new VirtualView(p1.GetID(), game.getGameID(), new RMIClient(new Client()), null);
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
        gh.getGameControllerMap().put(p1.GetID(), gc);

        p1.setMyPlance(helper.createInitializedBoard1());
        p1.getmyPlayerBoard().insertTile(new Tile(new MainCockpitComp(), UNIVERSAL.INSTANCE, UNIVERSAL.INSTANCE, UNIVERSAL.INSTANCE, UNIVERSAL.INSTANCE), 6 ,6, false);
        p1.setFinishListener(gc);
        gc.setLobbyListener(gh);



//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------



        vv.setLv(2);

        vv.setEventMatrix(2);
        vv.setEventMatrix(1);


//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------


        vv.setDisconnected(false);
        vv.sendEvent(new HandEvent(3, new ArrayList<>(Arrays.asList(NONE.INSTANCE,NONE.INSTANCE,NONE.INSTANCE,NONE.INSTANCE))));

        vv.setDisconnected(true);
        vv.sendEvent(new HandEvent(3, new ArrayList<>(Arrays.asList(NONE.INSTANCE,NONE.INSTANCE,NONE.INSTANCE,NONE.INSTANCE))));


        vv2.setDisconnected(false);
        vv2.sendEvent(new HandEvent(3, new ArrayList<>(Arrays.asList(NONE.INSTANCE,NONE.INSTANCE,NONE.INSTANCE,NONE.INSTANCE))));

        vv2.setDisconnected(true);
        vv2.sendEvent(new HandEvent(3, new ArrayList<>(Arrays.asList(NONE.INSTANCE,NONE.INSTANCE,NONE.INSTANCE,NONE.INSTANCE))));


//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------

        vv.setDisconnected(false);
        vv.sendEvent(((PlayerTileEvent) null));

        vv.setDisconnected(false);
        vv.sendEvent(((PlayerTileEvent) null));

        vv.setDisconnected(true);
        vv.sendEvent(((PlayerTileEvent) null));


        vv2.setDisconnected(false);
        vv2.sendEvent(((PlayerTileEvent) null));

        vv2.setDisconnected(true);
        vv2.sendEvent(((PlayerTileEvent) null));


//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------


        vv.setDisconnected(false);
        vv.sendEvent(((TileEvent) null));

        vv.setDisconnected(true);
        vv.sendEvent(((TileEvent) null));


        vv2.setDisconnected(false);
        vv2.sendEvent(((TileEvent) null));

        vv2.setDisconnected(true);
        vv2.sendEvent(((TileEvent) null));


//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------


        vv.playerBoardChanged(new TileEvent(3, 7, 6, new ArrayList<>(Arrays.asList(new BLUE(), new GREEN())), 0, false, false, 0, 0, new ArrayList<>(Arrays.asList(NONE.INSTANCE,NONE.INSTANCE,NONE.INSTANCE,NONE.INSTANCE))));

        vv.PBInfoChanged(new PBInfoEvent());

        vv.handChanged(new HandEvent());


//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------



        vv.setDisconnected(false);
        vv.tilesSetChanged(new CoveredTileSetEvent(80));

        vv.setDisconnected(true);
        vv.tilesSetChanged(new CoveredTileSetEvent(80));


        vv2.setDisconnected(false);
        vv2.tilesSetChanged(new CoveredTileSetEvent(80));

        vv2.setDisconnected(true);
        vv2.tilesSetChanged(new CoveredTileSetEvent(80));


//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------



        vv.setDisconnected(false);
        vv.tilesSetChanged(new UncoverdTileSetEvent(80, new ArrayList<>(Arrays.asList(NONE.INSTANCE,NONE.INSTANCE,NONE.INSTANCE,NONE.INSTANCE))));

        vv.setDisconnected(true);
        vv.tilesSetChanged(new UncoverdTileSetEvent(80, new ArrayList<>(Arrays.asList(NONE.INSTANCE,NONE.INSTANCE,NONE.INSTANCE,NONE.INSTANCE))));


        vv2.setDisconnected(false);
        vv2.tilesSetChanged(new UncoverdTileSetEvent(80, new ArrayList<>(Arrays.asList(NONE.INSTANCE,NONE.INSTANCE,NONE.INSTANCE,NONE.INSTANCE))));

        vv2.setDisconnected(true);
        vv2.tilesSetChanged(new UncoverdTileSetEvent(80, new ArrayList<>(Arrays.asList(NONE.INSTANCE,NONE.INSTANCE,NONE.INSTANCE,NONE.INSTANCE))));


//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------


        vv.setDisconnected(false);
        vv.seeDeck(new DeckEvent(new ArrayList<>(Arrays.asList(1, 2, 3, 4))));

        vv.setDisconnected(true);
        vv.seeDeck(new DeckEvent(new ArrayList<>(Arrays.asList(1, 2, 3, 4))));


        vv2.setDisconnected(false);
        vv2.seeDeck(new DeckEvent(new ArrayList<>(Arrays.asList(1, 2, 3, 4))));

        vv2.setDisconnected(true);
        vv2.seeDeck(new DeckEvent(new ArrayList<>(Arrays.asList(1, 2, 3, 4))));


//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------


        vv.setDisconnected(false);
        vv.newCard(new CardEvent(1));

        vv.setDisconnected(true);
        vv.newCard(new CardEvent(1));


        vv2.setDisconnected(false);
        vv2.newCard(new CardEvent(1));

        vv2.setDisconnected(true);
        vv2.newCard(new CardEvent(1));



//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------


        vv.setDisconnected(false);
        vv.gameBoardChanged(new GameBoardEvent(1, p1.GetID()));

        vv.setDisconnected(true);
        vv.gameBoardChanged(new GameBoardEvent(1, p1.GetID()));



        vv2.setDisconnected(false);
        vv2.gameBoardChanged(new GameBoardEvent(1, p1.GetID()));


        vv2.setDisconnected(true);
        vv2.gameBoardChanged(new GameBoardEvent(1, p1.GetID()));


//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------

        assertTrue(vv.getDisconnected());

//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------

        vv.reconnect();

//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------


        vv.setToken("provaToken");
        assertEquals("provaToken", vv.getToken());

        vv.setPrintWriter(new PrintWriter(System.out));
        vv.setClient(new RMIClient(new Client()));



//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------


        vv.setDisconnected(false);
        vv.GameLobbyChanged(new GameLobbyEvent(new ArrayList<>(), new ArrayList<>(Arrays.asList(true))));

        vv.setDisconnected(true);
        vv.GameLobbyChanged(new GameLobbyEvent(new ArrayList<>(), new ArrayList<>(Arrays.asList(true))));



        vv2.setDisconnected(false);
        vv2.GameLobbyChanged(new GameLobbyEvent(new ArrayList<>(), new ArrayList<>(Arrays.asList(true))));

        vv2.setDisconnected(true);
        vv2.GameLobbyChanged(new GameLobbyEvent(new ArrayList<>(), new ArrayList<>(Arrays.asList(true))));


//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------


        vv.PhaseChanged(new PhaseEvent(new BaseStateClient()));

        vv.sendHourglass(new HourglassEvent("suca", true));



//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------


        vv.setDisconnected(false);
        vv.sendEvent(new GameLobbyEvent(new ArrayList<>(), new ArrayList<>(Arrays.asList(true))));

        vv.setDisconnected(true);
        vv.sendEvent(new GameLobbyEvent(new ArrayList<>(), new ArrayList<>(Arrays.asList(true))));



        vv2.setDisconnected(false);
        vv2.sendEvent(new GameLobbyEvent(new ArrayList<>(), new ArrayList<>(Arrays.asList(true))));

        vv2.setDisconnected(true);
        vv2.sendEvent(new GameLobbyEvent(new ArrayList<>(), new ArrayList<>(Arrays.asList(true))));


//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------


        vv.rewardsChanged(new RewardsEvent(new ArrayList<>(Arrays.asList(new BLUE(), new GREEN()))));

        vv.setPlayersPBListeners(vv);
        vv.setPlayersPBListeners(vv2);

        vv.exceptionOccured(new ExceptionEvent("exception"));
        vv.getPBlistener();


        assertEquals(p1.GetID(), vv.getPlayerName());

        vv.sendLogEvent(new LogEvent("effect", 4 , 5, 2 ,0));



//--------------------------------------------------------------------------------------------------------------------


        vv.Effect(new LogEvent("effect", 4 , 5, 2 ,0));



//--------------------------------------------------------------------------------------------------------------------

        vv.removeListener(vv2);

        vv2.removeListeners();
        vv.removeListeners();

    }

}