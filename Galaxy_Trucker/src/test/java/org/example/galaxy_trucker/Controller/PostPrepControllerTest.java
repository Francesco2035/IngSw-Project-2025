package org.example.galaxy_trucker.Controller;

import org.example.galaxy_trucker.ClientServer.Client;
import org.example.galaxy_trucker.ClientServer.RMI.RMIClient;
import org.example.galaxy_trucker.Commands.AddCrewCommand;
import org.example.galaxy_trucker.Commands.DebugShip;
import org.example.galaxy_trucker.Model.Boards.GameBoard;
import org.example.galaxy_trucker.Model.Game;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.AddCrewState;
import org.example.galaxy_trucker.Model.PlayerStates.BaseState;
import org.example.galaxy_trucker.Model.PlayerStates.BuildingShip;
import org.example.galaxy_trucker.TestSetupHelper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class PostPrepControllerTest {


    static Game game;
    static GameBoard Gboard;
    static GameController gc;
    static Player p1;
    static VirtualView vv;
    PostPrepController c1;

    @Test
    public void testPostPrepController() throws IOException {


        game = new Game(2, "testPostPrepController");
        gc = new GameController(game.getGameID(), game, new GamesHandler(), game.getLv(),4);


        p1 = new Player();
        p1.setId("passos");
        game.NewPlayer(p1);
        p1.setBoards(game.getGameBoard());
        vv = new VirtualView(p1.GetID(), game.getGameID(), new RMIClient(new Client()), null);
        vv.setDisconnected(true);
        assertTrue(p1.getmyPlayerBoard().checkValidity());
        Gboard = game.getGameBoard();
        c1 = new PostPrepController(p1, game.getGameID(), false);
        gc.getControllerMap().put(p1.GetID(), c1);

        p1.setPhaseListener(vv);
        p1.getmyPlayerBoard().setListener(vv);
        p1.setHandListener(vv);
        p1.getCommonBoard().getTilesSets().setListeners(vv);
        p1.setCardListner(vv);
        gc.getVirtualViewMap().put(p1.GetID(), vv);

        p1.setMyPlance(TestSetupHelper.createInitializedBoard1());

        p1.setState(new AddCrewState());
        c1.action(new AddCrewCommand(2, false, false, new IntegerPair(4, 5), game.getGameID(), p1.GetID(), game.getLv(),"POPULATE", null), gc);
        c1.action(new AddCrewCommand(2, true, false, new IntegerPair(4, 5), game.getGameID(), p1.GetID(), game.getLv(),"POPULATE", null), gc);
        c1.action(new AddCrewCommand(0, true, true, new IntegerPair(4, 5), game.getGameID(), p1.GetID(), game.getLv(),"POPULATE", null), gc);
        c1.action(new AddCrewCommand(-2, false, false, new IntegerPair(4, 5), game.getGameID(), p1.GetID(), game.getLv(),"POPULATE", null), gc);
        c1.action(new AddCrewCommand(2000, false, false, new IntegerPair(4, 5), game.getGameID(), p1.GetID(), game.getLv(),"POPULATE", null), gc);
        c1.action(new AddCrewCommand(2, false, false, new IntegerPair(4, 5), game.getGameID(), p1.GetID(), game.getLv(),"POPULATE", null), gc);
        c1.action(new AddCrewCommand(0, true, false, new IntegerPair(4, 5), game.getGameID(), p1.GetID(), game.getLv(),"POPULATE", null), gc);
        c1.action(new AddCrewCommand(0, false, false, new IntegerPair(4, 5), game.getGameID(), p1.GetID(), game.getLv(),"POPULATE", null), gc);
        c1.action(new AddCrewCommand(2, false, false, new IntegerPair(4, 5), game.getGameID(), p1.GetID(), game.getLv(),"POPULATE", null), gc);
        c1.action(new AddCrewCommand(3, false, false, new IntegerPair(4, 5), game.getGameID(), p1.GetID(), game.getLv(),"POPULATE", null), gc);
        c1.action(new AddCrewCommand(-50000, false, false, new IntegerPair(4, 5), game.getGameID(), p1.GetID(), game.getLv(),"POPULATE", null), gc);
        c1.action(new AddCrewCommand(90000, false, false, new IntegerPair(4, 5), game.getGameID(), p1.GetID(), game.getLv(),"POPULATE", null), gc);
        c1.action(new AddCrewCommand(0, true, false, new IntegerPair(4, 5), game.getGameID(), p1.GetID(), game.getLv(),"POPULATE", null), gc);
        c1.action(new AddCrewCommand(0, false, true, new IntegerPair(4, 5), game.getGameID(), p1.GetID(), game.getLv(),"POPULATE", null), gc);
        c1.action(new AddCrewCommand(0, false, false, new IntegerPair(4, 5), game.getGameID(), p1.GetID(), game.getLv(),"POPULATE", null), gc);


        vv.setDisconnected(false);
        c1.nextState(gc);

        vv.setDisconnected(true);
        c1.nextState(gc);

    }

}