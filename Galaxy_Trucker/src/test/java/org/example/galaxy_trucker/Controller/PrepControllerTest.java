package org.example.galaxy_trucker.Controller;

import org.example.galaxy_trucker.ClientServer.Client;
import org.example.galaxy_trucker.ClientServer.RMI.RMIClient;
import org.example.galaxy_trucker.Commands.BuildingCommand;
import org.example.galaxy_trucker.Commands.ReadyCommand;
import org.example.galaxy_trucker.Controller.Listeners.PhaseListener;
import org.example.galaxy_trucker.Controller.Messages.PhaseEvent;
import org.example.galaxy_trucker.Exceptions.InvalidInput;
import org.example.galaxy_trucker.Model.Boards.GameBoard;
import org.example.galaxy_trucker.Model.Cards.CardStacks;
import org.example.galaxy_trucker.Model.GAGen;
import org.example.galaxy_trucker.Model.Game;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.BuildingShip;
import org.example.galaxy_trucker.Model.Tiles.TileSets;
import org.example.galaxy_trucker.TestSetupHelper;
import org.example.galaxy_trucker.View.ClientModel.States.BuildingClient;
import org.example.galaxy_trucker.View.ClientModel.States.PlayerStateClient;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.rmi.RemoteException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


class PrepControllerTest {

    static Game game;
    static GameBoard Gboard;

    static {
        try {
            game = new Game(2, "testPrepController");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static GameController gc = new GameController(game.getGameID(), game, new GamesHandler());
    static Player p1 = new Player();
    PrepController c1 = new PrepController(p1, game.getGameID(), gc, false);

    @BeforeAll
    public static void init() throws IOException {

        Game game = new Game(2, "testPrepController");
        p1 = new Player();
        p1.setId("passos");
        game.NewPlayer(p1);
        p1.setBoards(game.getGameBoard());
        p1.setMyPlance(TestSetupHelper.createInitializedBoard1());
        assertTrue(p1.getmyPlayerBoard().checkValidity());
        TestSetupHelper.HumansSetter1(p1.getmyPlayerBoard());
        Gboard = game.getGameBoard();
        Gboard.SetStartingPosition(p1);

    }

    @Test
    public void testPrepController() throws RemoteException {

        p1.setState(new BuildingShip());
        p1.setPhaseListener(new VirtualView(p1.GetID(), game.getGameID(), new RMIClient(new Client()), null));


        BuildingCommand bc1 = new BuildingCommand(0, 0, 0, 0, game.getGameID(), p1.GetID(), 2, "SeeDeck", null);
        c1.action(bc1, gc);

        BuildingCommand bc2 = new BuildingCommand(100, 100, 900, -1, game.getGameID(), p1.GetID(), 2, "PickTile", null);
        c1.action(bc2, gc);

        System.out.println("Tile trovata: " + p1.getCurrentTile().getId());
        int id = p1.getCurrentTile().getId();

        BuildingCommand bc3 = new BuildingCommand(0, 0, 0, 0, game.getGameID(), p1.GetID(), 2, "DiscardTile", null);

        c1.action(bc3, gc);

        assertEquals(id, p1.getCommonBoard().getTilesSets().getUncoveredTiles().getFirst().getId());
    }
}