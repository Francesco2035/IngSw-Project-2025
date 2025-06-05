package org.example.galaxy_trucker.Controller;

import org.example.galaxy_trucker.ClientServer.Client;
import org.example.galaxy_trucker.ClientServer.RMI.RMIClient;
import org.example.galaxy_trucker.Commands.HandleCargoCommand;
import org.example.galaxy_trucker.Model.Boards.GameBoard;
import org.example.galaxy_trucker.Model.Game;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.HandleCargo;
import org.example.galaxy_trucker.TestSetupHelper;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class FlightControllerTest {



    static Game game;
    static GameBoard Gboard;
    static GameController gc;
    static Player p1;
    static VirtualView vv;
    FlightController c1;

    @Test
    public void testFlightController() throws IOException {

        game = new Game(2, "testFlightController");
        gc = new GameController(game.getGameID(), game, new GamesHandler(), game.getLv(), 4);

        p1 = new Player();
        p1.setId("passos");
        game.NewPlayer(p1);
        p1.setBoards(game.getGameBoard());
        vv = new VirtualView(p1.GetID(), game.getGameID(), new RMIClient(new Client()), null);
        vv.setDisconnected(true);
        assertTrue(p1.getmyPlayerBoard().checkValidity());
        Gboard = game.getGameBoard();
        c1 = new FlightController(p1, game.getGameID(), gc, false);
        gc.getControllerMap().put(p1.GetID(), c1);

        p1.setPhaseListener(vv);
        p1.getmyPlayerBoard().setListener(vv);
        p1.setHandListener(vv);
        p1.getCommonBoard().getTilesSets().setListeners(vv);
        p1.setCardListner(vv);
        gc.getVirtualViewMap().put(p1.GetID(), vv);

        p1.setMyPlance(TestSetupHelper.createInitializedBoard1());


        p1.setState(new HandleCargo());
//        c1.action(new HandleCargoCommand());





    }
    
}