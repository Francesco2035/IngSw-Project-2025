package org.example.galaxy_trucker.Controller;

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
        p1.setBoards(new GameBoard(new TileSets(new GAGen()), 2, new CardStacks(new GAGen(), 2)));
        p1.setMyPlance(TestSetupHelper.createInitializedBoard1());
        assertTrue(p1.getmyPlayerBoard().checkValidity());
        TestSetupHelper.HumansSetter1(p1.getmyPlayerBoard());
        Gboard = game.getGameBoard();
        Gboard.SetStartingPosition(p1);

    }

    @Test
    public void testPrepController() {

        p1.setState(new BuildingShip());
        BuildingCommand bc1 = new BuildingCommand(0, 0, 0, 0, game.getGameID(), p1.GetID(), 2, "SeeDeck", null);
        c1.action(bc1, gc);

        p1.setState(new BuildingShip());
        BuildingCommand bc2 = new BuildingCommand(3, 1, 90, 0, game.getGameID(), p1.GetID(), 2, "PickTile", null);
        c1.action(bc2, gc);


    }
}