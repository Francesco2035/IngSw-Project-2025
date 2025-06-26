package org.example.galaxy_trucker.Controller;

import org.example.galaxy_trucker.ClientServer.Client;
import org.example.galaxy_trucker.ClientServer.GamesHandler;
import org.example.galaxy_trucker.ClientServer.RMI.RMIClient;
import org.example.galaxy_trucker.Commands.BuildingCommand;
import org.example.galaxy_trucker.Commands.ReadyCommand;
import org.example.galaxy_trucker.Model.Boards.GameBoard;
import org.example.galaxy_trucker.Model.Connectors.UNIVERSAL;
import org.example.galaxy_trucker.Model.Game;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.BuildingShip;
import org.example.galaxy_trucker.Model.Tiles.MainCockpitComp;
import org.example.galaxy_trucker.Model.Tiles.Tile;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * test the prep controller
 */

class PrepControllerTest {

    static Game game;
    static GameBoard Gboard;
    static GameController gc;
    static Player p1;
    static VirtualView vv;
    PrepController c1;

    /**
     * tests the commands that can be called by the prep controller and soe possible exceptions
     * @throws IOException
     */

    @Test
    public void testPrepController() throws IOException {

        game = new Game(2, "testPrepController");
        gc = new GameController(game.getGameID(), game, new GamesHandler(),game.getLv(),4);
        p1 = new Player();
        p1.setId("passos");
        vv = new VirtualView(p1.GetID(), game.getGameID(), new RMIClient(new Client()), null);
        vv.setDisconnected(true);
////        assertTrue(gc.getGame().getPlayers().values().stream().filter(p -> p.GetID().equals(p1.GetID())).findFirst().orElseThrow().getmyPlayerBoard().checkValidity());
        game.NewPlayer(p1);
        p1.setBoards(game.getGameBoard());
        Gboard = game.getGameBoard();
        c1 = new PrepController(p1, game.getGameID(), gc, false);
        gc.getControllerMap().put(p1.GetID(), c1);

        p1.setPhaseListener(vv);
        p1.setReadyListener(gc);
        p1.getmyPlayerBoard().setListener(vv);
        p1.setHandListener(vv);
        p1.getCommonBoard().getTilesSets().setListeners(vv);
        p1.setCardListner(vv);
        gc.getVirtualViewMap().put(p1.GetID(),vv);

        gc.NewPlayer(p1, vv, new UUID(0, 0).toString());

        p1.getmyPlayerBoard().insertTile(new Tile(new MainCockpitComp(), UNIVERSAL.INSTANCE, UNIVERSAL.INSTANCE, UNIVERSAL.INSTANCE, UNIVERSAL.INSTANCE), 6 ,6, false);



//--------------------------------------------------------------------------------------------------------------------------------

//
//        Game g1 = new Game(2, "g2");
//        GameController gc1 = new GameController(g1.getGameID(), g1, new GamesHandler(), g1.getLv(),4);
//        Player p2 = new Player();
//        p2.setId("passos1");
//        ClientInterface ci = new RMIClient(new Client());
//
//
//        VirtualView vv1 = new VirtualView(p2.GetID(), g1.getGameID(), ci, null);
//        UUID tk = new UUID(45, 3456);
//
//        gc1.NewPlayer(p2, vv1, tk);
//
//        gc1.getGame().getPlayers().values().stream().findFirst().ifPresent(Player::GetID);
//
////        gc1.getGame().getPlayers().values().stream().findFirst().ifPresent(Player::);
//
//
//        gc1.getControllerMap().values().stream().findFirst().orElseThrow().action(new ReadyCommand(game.getGameID(), p1.GetID(), game.getLv(), "Ready", false, null), gc);
//
//        gc1.changeState();
//        gc1.changeState();
//        gc1.changeState();
//        gc1.changeState();



















//--------------------------------------------------------------------------------------------------------------------------------

        p1.setState(new BuildingShip());

        BuildingCommand bc1 = new BuildingCommand(1, 0, 0, 0, game.getGameID(), p1.GetID(), 2, "SEEDECK", null);
        c1.action(bc1, gc);

        BuildingCommand bc2 = new BuildingCommand(100, 100, 900, -1, game.getGameID(), p1.GetID(), 2, "PICKTILE", null);
        c1.action(bc2, gc);

        int id = p1.getCurrentTile().getId();
        System.out.println("Tile trovata: " + id);

        BuildingCommand bc3 = new BuildingCommand(0, 0, 0, 0, game.getGameID(), p1.GetID(), 2, "DISCARD", null);
        c1.action(bc3, gc);
        assertEquals(id, p1.getCommonBoard().getTilesSets().getUncoveredTiles().getFirst().getId());

        BuildingCommand bc4 = new BuildingCommand(100, 100, 900, -1, game.getGameID(), p1.GetID(), 2, "PICKTILE", null);
        c1.action(bc4, gc);

        BuildingCommand bc5 = new BuildingCommand(100, 100, 900, -1, game.getGameID(), p1.GetID(), 2, "PICKTILE", null);
        c1.action(bc5, gc);

        c1.action(new BuildingCommand(0, 0, 0, 0, game.getGameID(), p1.GetID(), 2, "DISCARD", null), gc);

        c1.action(new BuildingCommand(0, 0, 0, 0, game.getGameID(), p1.GetID(), 2, "FROMBUFFER", null), gc);

        c1.action(new BuildingCommand(0, 0, 0, 0, game.getGameID(), p1.GetID(), 2, "PICKTILE", null), gc);

        c1.action(new BuildingCommand(0, 0, 0, 0, game.getGameID(), p1.GetID(), 2, "INSERTTILE", null), gc);
//        c1.action(new BuildingCommand(6, 6, 0, 0, game.getGameID(), p1.GetID(), 2, "INSERTTILE", null), gc);
        c1.action(new BuildingCommand(5, 5, 0, 0, game.getGameID(), p1.GetID(), 2, "INSERTTILE", null), gc);
        c1.action(new BuildingCommand(5, 5, 100, 0, game.getGameID(), p1.GetID(), 2, "INSERTTILE", null), gc);
        c1.action(new BuildingCommand(100, 5, 0, 0, game.getGameID(), p1.GetID(), 2, "INSERTTILE", null), gc);
        c1.action(new BuildingCommand(999, -999, -90, 0, game.getGameID(), p1.GetID(), 2, "INSERTTILE", null), gc);



        c1.action(new BuildingCommand(6, 6, 0, 0, game.getGameID(), p1.GetID(), 2, "TOBUFFER", null), gc);
        c1.action(new BuildingCommand(5, 5, 0, 0, game.getGameID(), p1.GetID(), 2, "TOBUFFER", null), gc);
        c1.action(new BuildingCommand(5, 5, 0, 0, game.getGameID(), p1.GetID(), 2, "FROMBUFFER", null), gc);

        c1.action(new BuildingCommand(5, 5, 0, 0, game.getGameID(), p1.GetID(), 2, "PICKTILE", null), gc);
        c1.action(new BuildingCommand(5, 5, 0, 0, game.getGameID(), p1.GetID(), 2, "TOBUFFER", null), gc);
        c1.action(new BuildingCommand(5, 5, 0, 0, game.getGameID(), p1.GetID(), 2, "FROMBUFFER", null), gc);
        c1.action(new BuildingCommand(999, -999, -90, 0, game.getGameID(), p1.GetID(), 2, "INSERTTILE", null), gc);
        c1.action(new BuildingCommand(4, 4, -90, 0, game.getGameID(), p1.GetID(), 2, "INSERTTILE", null), gc);
        c1.action(new BuildingCommand(4, 4, 90, 0, game.getGameID(), p1.GetID(), 2, "INSERTTILE", null), gc);
        c1.action(new BuildingCommand(7, 7, 90, 0, game.getGameID(), p1.GetID(), 2, "INSERTTILE", null), gc);
//
////aggiungere gagen per scegliere una tile apposita per testare la inserttile funzionante

        c1.action(new BuildingCommand(4, 4, 90, 0, game.getGameID(), p1.GetID(), 2, "HOURGLASS", null), gc);


        c1.action(new ReadyCommand(game.getGameID(), p1.GetID(), game.getLv(), "Ready", false, null), gc);
        c1.action(new ReadyCommand(game.getGameID(), p1.GetID(), game.getLv(), "Ready", false, null), gc);
////        c1.action(new ReadyCommand(game.getGameID(), p1.GetID(), game.getLv(), "Ready", true, null), gc);
//
        System.out.println(p1.getPlayerState().toString());
        c1.action(new ReadyCommand(game.getGameID(), p1.GetID(), game.getLv(), "Ready", false, null), gc);
        System.out.println(p1.getPlayerState().toString());

////        c1.action(new FinishBuildingCommand(10, game.getGameID(), p1.GetID(), game.getLv(), "FINISH", null), gc);
////        c1.action(new FinishBuildingCommand(4, game.getGameID(), p1.GetID(), game.getLv(), "FINISH", null), gc);
//
        c1.action(new ReadyCommand(game.getGameID(), p1.GetID(), game.getLv(), "Ready", true, null), gc);

////        c1.action(new FinishBuildingCommand(1, game.getGameID(), p1.GetID(), game.getLv(), "FINISH", null), gc);
////        c1.action(new FinishBuildingCommand(1, game.getGameID(), p1.GetID(), game.getLv(), "HEHEHEHA", null), gc);
////        c1.action(new FinishBuildingCommand(1, game.getGameID(), p1.GetID(), game.getLv(), "FINISH", null), gc);

//        c1.action(new ReadyCommand(game.getGameID(), p1.GetID(), game.getLv(), "Quit", false, null), gc);
//
//        vv.setDisconnected(false);
//        System.out.println(p1.getPlayerState().toString());
//        c1.DefaultAction(gc);


//        vv.setDisconnected(true);
//        System.out.println(p1.getPlayerState().toString());
//        c1.DefaultAction(gc);
////loop infinito

////--------------------------------------------------------------------------------------------

//        p1.setState(new ChoosePosition());

//        vv.setDisconnected(false);
//        c1.DefaultAction(gc);
//
//        vv.setDisconnected(true);
//        c1.DefaultAction(gc);

////        c1.action(new FinishBuildingCommand(1, game.getGameID(), p1.GetID(), game.getLv(), "FINISH", null), gc);


////--------------------------------------------------------------------------------------------------------------------------------

        vv.setDisconnected(false);
        c1.nextState(gc);

        vv.setDisconnected(true);
        c1.nextState(gc);

    }
}