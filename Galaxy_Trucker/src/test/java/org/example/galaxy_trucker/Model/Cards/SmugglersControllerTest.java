package org.example.galaxy_trucker.Model.Cards;

import org.example.galaxy_trucker.Commands.ConsumeEnergyCommand;
import org.example.galaxy_trucker.Commands.GiveAttackCommand;
import org.example.galaxy_trucker.Controller.CardsController;
import org.example.galaxy_trucker.Controller.Listeners.HandListener;
import org.example.galaxy_trucker.Controller.Listeners.PhaseListener;
import org.example.galaxy_trucker.Controller.Messages.HandEvent;
import org.example.galaxy_trucker.Controller.Messages.PhaseEvent;
import org.example.galaxy_trucker.Model.Boards.GameBoard;
import org.example.galaxy_trucker.Model.GAGen;
import org.example.galaxy_trucker.Model.Game;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.*;
import org.example.galaxy_trucker.TestSetupHelper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SmugglersControllerTest {

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
    //static Player p2 = new Player();

    CardsController c1 = new CardsController(p1, game.getGameID(), false);
   // CardsController c2 = new CardsController(p2, game.getGameID(), false);


    @BeforeAll
    public static void init() throws IOException {
        Game game = new Game(2, "testCarteController");


        p1 = new Player();
        p1.setId("pietro");
//        p2 = new Player();
//        p2.setId("FRA");
        game.NewPlayer(p1);
//        game.NewPlayer(p2);
        p1.setMyPlance(TestSetupHelper.createInitializedBoard1());
        System.out.println("\n");
//        p2.setMyPlance(TestSetupHelper.createInitializedBoard2());

        assertEquals(true, p1.getmyPlayerBoard().checkValidity());
        System.out.println("sksk");
//        assertEquals(true, p2.getmyPlayerBoard().checkValidity());

        TestSetupHelper.HumansSetter1(p1.getmyPlayerBoard());
//        TestSetupHelper.HumansSetter1(p2.getmyPlayerBoard());
        Gboard = game.getGameBoard();

        Gboard.SetStartingPosition(p1);
//        Gboard.SetStartingPosition(p2);


        // CardsController c1= new CardsController(p1,game.getGameID(),false);
// CardsController c2= new CardsController(p2,game.getGameID(),false);

    }

    @Test
    public void testStealEnergy() throws IOException, InterruptedException {
        game.setGameBoard(Gboard);
        GAGen gag = new GAGen();
        ArrayList<Card> cards = gag.getCardsDeck();
        Card CurrentCard = cards.get(3);

        for (Player p : game.getGameBoard().getPlayers()) {
            p.setCard(CurrentCard);
        }

        CurrentCard.setBoard(Gboard);

        System.out.println("Id Card: " + CurrentCard.getId() + " " + CurrentCard.getClass().getName());

        PhaseListener phaseListener = new PhaseListener() {
            @Override
            public void PhaseChanged(PhaseEvent event) {
                System.out.println(event);
            }
        };

        HandListener handListener = new HandListener() {

            @Override
            public void handChanged(HandEvent event) {
                System.out.println("handChanged");
            }
        };

        for(Player p: game.getGameBoard().getPlayers()) {
            p.setPhaseListener(phaseListener);
            p.setHandListener(handListener);
//            p.setState(new BuildingShip());

        }
        CurrentCard.CardEffect();


        assertEquals(p1.getPlayerState().getClass(), GiveAttack.class);
//        assertEquals(p2.getPlayerState().getClass(), Waiting.class);
        ArrayList<IntegerPair> coordinates = new ArrayList<>();
        coordinates.clear();
        GiveAttackCommand fight = new GiveAttackCommand(coordinates,game.getID(),p1.GetID(),game.getLv(),"GiveAttackCommand","SK");
        fight.execute(p1);
        assertEquals(p1.getPlayerState().getClass(), ConsumingEnergy.class);


        coordinates.add(new IntegerPair(6,9));
        coordinates.add(new IntegerPair(6,9));
        coordinates.add(new IntegerPair(6,9));
        ConsumeEnergyCommand consume = new ConsumeEnergyCommand(coordinates,game.getID(),p1.GetID(),game.getLv(),"GiveAttackCommand","SK");
        consume.execute(p1);
    }


}
