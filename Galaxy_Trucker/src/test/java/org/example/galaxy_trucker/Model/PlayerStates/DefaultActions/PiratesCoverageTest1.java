package org.example.galaxy_trucker.Model.PlayerStates.DefaultActions;

import org.example.galaxy_trucker.Controller.CardsController;
import org.example.galaxy_trucker.Controller.Messages.ConcurrentCardListener;
import org.example.galaxy_trucker.Model.Boards.GameBoard;
import org.example.galaxy_trucker.Model.Cards.Card;
import org.example.galaxy_trucker.Model.Cards.Pirates;
import org.example.galaxy_trucker.Model.GAGen;
import org.example.galaxy_trucker.Model.Game;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.NewTestSetupHelper;
import org.junit.jupiter.api.RepeatedTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PiratesCoverageTest1 {







    static Game game;

    static {
        try {
            game = new Game(2,"pippo");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static GameBoard Gboard =game.getGameBoard();

    static Player p1;
    static Player p2;

    // static CardsController c1 = new CardsController(p1,game.getGameID(),false);
    //static CardsController c2= new CardsController(p2,game.getGameID(),false);;


    @RepeatedTest(10)
    public void DefaultMeteorites() throws IOException, InterruptedException {
        try{
            Game game = new Game(2, "testCarteController");
            NewTestSetupHelper helper = new NewTestSetupHelper();


            p1 = new Player();
            p1.setId("pietro");
            p2 = new Player();
            p2.setId("FRA");
            game.NewPlayer(p1);
            game.NewPlayer(p2);
            p1.setMyPlance(helper.createInitializedBoard1());
            System.out.println("\n");
            p2.setMyPlance(helper.createInitializedBoard2());

            assertTrue(p1.getmyPlayerBoard().checkValidity());
            System.out.println("sksk");
            assertTrue(p2.getmyPlayerBoard().checkValidity());

            helper.HumansSetter1(p1.getmyPlayerBoard());
            helper.HumansSetter1(p2.getmyPlayerBoard());
            Gboard = game.getGameBoard();

            Gboard.SetStartingPosition(p1);
            Gboard.SetStartingPosition(p2);

            game.setGameBoard(Gboard);
            GAGen gag = new GAGen();
            ArrayList<Card> cards = gag.getCardsDeck();

            ConcurrentCardListener conc = new ConcurrentCardListener() {
                @Override
                public void onConcurrentCard(boolean phase) {
                    System.out.println("LISTENER CONCORRENTE");
                }
            };

            ArrayList<Integer> attacks = new ArrayList<>(Arrays.asList(0, 0, 0, 1, 1, 0, 1, 1, 2, 0, 2, 1, 3, 0, 3, 1));

            Card CurrentCard = new Pirates(game.getLv(), 3, Gboard, 3, 10, attacks);

            CurrentCard.setConcurrentCardListener(conc);

            for (Player p : game.getGameBoard().getPlayers()) {
                p.setCard(CurrentCard);
            }

            CurrentCard.setBoard(Gboard);

            System.out.println("Id Card: " + CurrentCard.getId() + " " + CurrentCard.getClass().getName());

            //  c1.DefaultAction(null);
            CardsController c1 = new CardsController(p1, game.getGameID(), false);
            CardsController c2 = new CardsController(p2, game.getGameID(), false);


            System.out.println("\n\n\n\n\n");
            CurrentCard.CardEffect();


            /// fine setup


            while (!CurrentCard.isFinished()) {
                System.out.println("\n\n p1: " + p1.getPlayerState().getClass() + p1.GetHasActed() + "\n p2: " + p2.getPlayerState().getClass() + p2.GetHasActed());


                if (!p1.GetHasActed()) {

                    c1.DefaultAction(null);
                }
                System.out.println("\n snuu \n");
                if (!p2.GetHasActed() && !CurrentCard.isFinished()) {
                    c2.DefaultAction(null);
                }

            }

            // chiedere a fra la roba della board ma handle destruction ipoteticamente ok


        } catch (Exception e){
            assertEquals("Cannot invoke \"org.example.galaxy_trucker.Messages.FinishListener.onEndGame(boolean, String, String)\" because \"this.finishListener\" is null", e.getMessage());
        }
    }
}
