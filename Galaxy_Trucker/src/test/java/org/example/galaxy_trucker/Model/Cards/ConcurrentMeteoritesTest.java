package org.example.galaxy_trucker.Model.Cards;

import org.example.galaxy_trucker.Controller.CardsController;
import org.example.galaxy_trucker.Controller.Listeners.PhaseListener;
import org.example.galaxy_trucker.Controller.Messages.ConcurrentCardListener;
import org.example.galaxy_trucker.Controller.Messages.PhaseEvent;
import org.example.galaxy_trucker.Model.Boards.GameBoard;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.GAGen;
import org.example.galaxy_trucker.Model.Game;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.DefendingFromLarge;
import org.example.galaxy_trucker.Model.PlayerStates.DefendingFromSmall;
import org.example.galaxy_trucker.TestSetupHelper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ConcurrentMeteoritesTest
{

    static Game game;
    static GameBoard Gboard;

    static {
        try {
            game = new Game(2, "testCarteController");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static Player p1 = new Player();
    static Player p2 = new Player();

    CardsController c1 = new CardsController(p1, game.getGameID(), false);
    CardsController c2 = new CardsController(p2, game.getGameID(), false);


    @BeforeAll
    public static void init() throws IOException {
        Game game = new Game(2, "testCarteController");


        p1 = new Player();
        p1.setId("pietro");
        p2 = new Player();
        p2.setId("FRA");
        game.NewPlayer(p1);
        game.NewPlayer(p2);
        p1.setMyPlance(TestSetupHelper.createInitializedBoard1());
        System.out.println("\n");
        p2.setMyPlance(TestSetupHelper.createInitializedBoard2());

        assertEquals(true, p1.getmyPlayerBoard().checkValidity());
        System.out.println("sksk");
        assertEquals(true, p2.getmyPlayerBoard().checkValidity());

        TestSetupHelper.HumansSetter1(p1.getmyPlayerBoard());
        TestSetupHelper.HumansSetter1(p2.getmyPlayerBoard());
        Gboard = game.getGameBoard();

        Gboard.SetStartingPosition(p1);
        Gboard.SetStartingPosition(p2);


        // CardsController c1= new CardsController(p1,game.getGameID(),false);
// CardsController c2= new CardsController(p2,game.getGameID(),false);

    }

    @RepeatedTest(50)
    void CardEffect () throws IOException, InterruptedException {
        game.setGameBoard(Gboard);
        GAGen gag = new GAGen();
        ArrayList<Card> cards = gag.getCardsDeck();
        Card CurrentCard = cards.get(14);

        for (Player p : game.getGameBoard().getPlayers()) {
            p.setCard(CurrentCard);
        }

        CurrentCard.setBoard(Gboard);

        System.out.println("Id Card: " + CurrentCard.getId() + " " + CurrentCard.getClass().getName());
         PhaseListener phaseListener = new PhaseListener() {
             @Override
             public void PhaseChanged(PhaseEvent event) {
                 System.out.println("PhaseChanged");
             }
         };
         for (Player p : game.getGameBoard().getPlayers()) {
             p.setPhaseListener(phaseListener);
         }

        ConcurrentCardListener ConcurrentCardListener= new ConcurrentCardListener() {
            @Override
            public void onConcurrentCard(boolean phase) {
                System.out.println("onConcurrentCard phase is " + phase);
            }
        };

         CurrentCard.setConcurrentCardListener(ConcurrentCardListener);
         assertEquals(CurrentCard.getConcurrentCardListener(),ConcurrentCardListener);
         System.out.println("dovrebbe andare?");





        CurrentCard.CardEffect();
        for (Player p : game.getGameBoard().getPlayers()) {
            if(p.getPlayerState().getClass().equals(DefendingFromSmall.class)) {
                System.out.println(p.GetID() +" is defending from small");
                CurrentCard.DefendFromSmall(null,p);
            }
            else if(p.getPlayerState().getClass().equals(DefendingFromLarge.class)) {
                System.out.println(p.GetID() +" is defending from large");
                CurrentCard.DefendFromLarge(null,null,p);
            }
        }
    }


}
