package org.example.galaxy_trucker.Controller;

import org.example.galaxy_trucker.ClientServer.Client;
import org.example.galaxy_trucker.ClientServer.RMI.RMIClient;
import org.example.galaxy_trucker.Commands.AcceptCommand;
import org.example.galaxy_trucker.Commands.ReadyCommand;
import org.example.galaxy_trucker.Model.Game;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.NewTestSetupHelper;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class GameControllerTest2 {


    @Test
    void startGame() throws IOException {

        GamesHandler gamesHandler = new GamesHandler();
        Game game = new Game(2, "g1");
        GameController gc = new GameController(game.getGameID(), game, gamesHandler, game.getLv(), 4);
        NewTestSetupHelper helper = new NewTestSetupHelper();
        Player p1 = new Player();
        p1.setId("passos");
        VirtualView vv1 = new VirtualView(p1.GetID(), game.getGameID(), new RMIClient(new Client()), null);
        gc.setLobbyListener(gamesHandler);
        gc.NewPlayer(p1, vv1, "provaToken"); //possibile incremento ulteriore delle linee coperte
        p1.setReadyListener(gc);

        p1.SetReady(true);
        gc.changeState();
        gc.addCommand(new AcceptCommand(game.getID(), p1.GetID(), game.getLv(), "accept", true, null));

        assertTrue(gc.isStarted());


        Player p2 = new Player();
        p2.setId("passos2");
        VirtualView vv2 = new VirtualView(p1.GetID(), game.getGameID(), new RMIClient(new Client()), null);
        gc.NewPlayer(p2, vv2, "provaToken2"); //possibile incremento ulteriore delle linee coperte
        p2.setReadyListener(gc);
        p2.SetReady(true);
        gc.changeState();

        gc.removePlayer("provaToken", new ReadyCommand(game.getID(), p1.GetID(), game.getLv(), "Quit", true,"provaToken2"));


        gc.startFlightMode();

        gc.checkGameOver();

        gc.setGameOver();

        gc.stopAllPlayerThreads();

        gc.setFlightCount(0);



        Player p3 = new Player();
        p3.setId("passos3");
        VirtualView vv3 = new VirtualView(p1.GetID(), game.getGameID(), new RMIClient(new Client()), null);
        gc.NewPlayer(p3, vv3, "provaToken3"); //possibile incremento ulteriore delle linee coperte
        p3.setReadyListener(gc);
        p3.SetReady(true);
        gc.changeState();

        gc.stopPlayer("provaToken3");





        Player p4 = new Player();
        p4.setId("passos4");
        VirtualView vv4 = new VirtualView(p1.GetID(), game.getGameID(), new RMIClient(new Client()), null);
        gc.NewPlayer(p4, vv4, "provaToken4"); //possibile incremento ulteriore delle linee coperte
        p4.setReadyListener(gc);
        p4.SetReady(true);
        gc.changeState();



        gc.startPlayer("provaToken4");



        gc.getlv();

        gc.check(new ReadyCommand(game.getID(), p1.GetID(), game.getLv(), "Quit", true,"provaToken2"));

        gc.getControllerMap();

        gc.getTokenToPlayerId();

        gc.stopGame();


    }
}