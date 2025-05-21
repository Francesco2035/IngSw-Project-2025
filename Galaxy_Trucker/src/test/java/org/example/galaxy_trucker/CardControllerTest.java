package org.example.galaxy_trucker;

import org.example.galaxy_trucker.Commands.*;
import org.example.galaxy_trucker.Controller.CardsController;
import org.example.galaxy_trucker.Model.Boards.GameBoard;
import org.example.galaxy_trucker.Model.Boards.Player_IntegerPair;
import org.example.galaxy_trucker.Model.Cards.AbandonedShip;
import org.example.galaxy_trucker.Model.Cards.AbandonedStation;
import org.example.galaxy_trucker.Model.Cards.Card;
import org.example.galaxy_trucker.Model.GAGen;
import org.example.galaxy_trucker.Model.Game;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;
import java.nio.channels.AcceptPendingException;
import java.util.ArrayList;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CardControllerTest {

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

        assertTrue(p1.getmyPlayerBoard().checkValidity());
        System.out.println("sksk");
        assertTrue(p2.getmyPlayerBoard().checkValidity());

        TestSetupHelper.HumansSetter1(p1.getmyPlayerBoard());
        TestSetupHelper.HumansSetter1(p2.getmyPlayerBoard());
        Gboard = game.getGameBoard();

        Gboard.SetStartingPosition(p1);
        Gboard.SetStartingPosition(p2);


        // CardsController c1= new CardsController(p1,game.getGameID(),false);
// CardsController c2= new CardsController(p2,game.getGameID(),false);

    }

    @Test
    public void testAbandonedShipCard() throws IOException {
        game.setGameBoard(Gboard);
        GAGen gag = new GAGen();
        ArrayList<Card> cards = gag.getCardsDeck();
        Card CurrentCard = cards.get(7);

        for (Player p : game.getGameBoard().getPlayers()) {
            p.setCard(CurrentCard);
        }

        CurrentCard.setBoard(Gboard);

        System.out.println("Id Card: " + CurrentCard.getId() + " " + CurrentCard.getClass().getName());
        CurrentCard.CardEffect();


        assertEquals(Accepting.class, p1.getPlayerState().getClass());
        assertEquals(Waiting.class, p2.getPlayerState().getClass());
        AcceptCommand acceptCommand = new AcceptCommand(game.getID(), p1.GetID(), Gboard.getLevel(), "AcceptCommand", false, "placeholder");

        acceptCommand.execute(p1);
        assertEquals(Accepting.class, p2.getPlayerState().getClass());
        assertEquals(Waiting.class, p1.getPlayerState().getClass());
        acceptCommand = new AcceptCommand(game.getID(), p2.GetID(), Gboard.getLevel(), "AcceptCommand", false, "placeholder");
        acceptCommand.execute(p2);

//    assertEquals(p2.getPlayerState().getClass(), Killing.class);
//    assertEquals(p1.getPlayerState().getClass(), Waiting.class);
//
//    ArrayList<IntegerPair> coords = new ArrayList<>();
//
//    coords.add(new IntegerPair(6,6));
//    coords.add(new IntegerPair(6,6));
//    coords.add(new  IntegerPair(5,6));
//
//
//    KillCommand kill = new KillCommand(coords,game.getID(),p2.GetID(),Gboard.getLevel(),"killcommand","placeholder");
//
//    kill.execute(p2);

        assertEquals(p1.getPlayerState().getClass(), BaseState.class);
        assertEquals(p2.getPlayerState().getClass(), BaseState.class);


    }

    @Test
    public void testAbandonedStationCard() throws IOException {
        game.setGameBoard(Gboard);
        GAGen gag = new GAGen();
        ArrayList<Card> cards = gag.getCardsDeck();
        Card CurrentCard = cards.get(11);

        for (Player p : game.getGameBoard().getPlayers()) {
            p.setCard(CurrentCard);
        }

        CurrentCard.setBoard(Gboard);


        System.out.println("Id Card: " + CurrentCard.getId() + " " + CurrentCard.getClass().getName());
        CurrentCard.CardEffect();


        assertEquals(p1.getPlayerState().getClass(), Accepting.class);
        assertEquals(false, p1.GetHasActed());
        assertEquals(p2.getPlayerState().getClass(), Waiting.class);
        assertEquals(true, p2.GetHasActed());

        System.out.println(p1.GetID() + " is in the accepting state");
        AcceptCommand acceptCommand = new AcceptCommand(game.getID(), p1.GetID(), Gboard.getLevel(), "AcceptCommand", false, "placeholder");
        acceptCommand.execute(p1);


        /// settando l'accept a true fa quest
//    assertEquals(p1.getPlayerState().getClass(), HandleCargo.class);
//    assertEquals(p2.getPlayerState().getClass(), Waiting.class);
//
//    HandleCargoCommand handleCargoCommand = new HandleCargoCommand(0,null,game.getID(),p1.GetID(),Gboard.getLevel(),"Finish","placeholder");
//    handleCargoCommand.execute(p1);
//
//    assertEquals(p1.getPlayerState().getClass(), BaseState.class);
//    assertEquals(p2.getPlayerState().getClass(), BaseState.class);

        assertEquals(p2.getPlayerState().getClass(), Accepting.class);
        assertEquals(false, p2.GetHasActed());
        assertEquals(p1.getPlayerState().getClass(), Waiting.class);
        assertEquals(true, p1.GetHasActed());

        acceptCommand = new AcceptCommand(game.getID(), p2.GetID(), Gboard.getLevel(), "AcceptCommand", false, "placeholder");
        acceptCommand.execute(p1);

        assertEquals(p1.getPlayerState().getClass(), BaseState.class);
        assertEquals(p2.getPlayerState().getClass(), BaseState.class);


        AbandonedStation testTroppiUmani = new AbandonedStation(12, null, 2, 10, Gboard);

        testTroppiUmani.CardEffect();

    }


    //meteoriti ha memoria?
    @Test
    public void testMeteoritesCard() throws IOException {
        game.setGameBoard(Gboard);
        GAGen gag = new GAGen();
        ArrayList<Card> cards = gag.getCardsDeck();
        Card CurrentCard = cards.get(14);


        int j=0;
        for (Player p : game.getGameBoard().getPlayers()) {
            System.out.println(j);
            p.setCard(CurrentCard);
            j++;
        }

        assertEquals(p1.getCurrentCard(), CurrentCard);

        CurrentCard.setBoard(Gboard);


        System.out.println("Id Card: " + CurrentCard.getId() + " " + CurrentCard.getClass().getName());
        CurrentCard.CardEffect();
        DefendFromSmallCommand defendFromSmallCommand;
        DefendFromLargeCommand defendFromLargeCommand;
        int i=0;
        while (!CurrentCard.isFinished() && i<15){
            System.out.println("Instance: "+i);
            if(p1.getPlayerState().getClass().equals(DefendingFromSmall.class)) {
                System.out.println("p1 is defending from small");
                assertEquals(CurrentCard, p1.getCurrentCard());
                defendFromSmallCommand= new DefendFromSmallCommand(null,game.getID(),p1.GetID(),Gboard.getLevel(),"boh?","Placeholder");
                defendFromSmallCommand.execute(p1);
            }
            else if(p2.getPlayerState().getClass().equals(DefendingFromSmall.class)) {
                System.out.println("p2 is defending from small");
                assertEquals(CurrentCard, p2.getCurrentCard());
                defendFromSmallCommand= new DefendFromSmallCommand(null,game.getID(),p2.GetID(),Gboard.getLevel(),"boh?","Placeholder");
                defendFromSmallCommand.execute(p2);
            }
            else if(p1.getPlayerState().getClass().equals(DefendingFromLarge.class)) {
                System.out.println("p1 is defending from large");
                assertEquals(CurrentCard, p1.getCurrentCard());
                defendFromLargeCommand =new DefendFromLargeCommand(null,null,game.getID(),p1.GetID(),Gboard.getLevel(),"boh?","Placeholder");
                defendFromLargeCommand.execute(p1);
            }
            else if(p2.getPlayerState().getClass().equals(DefendingFromLarge.class)) {
                assertEquals(CurrentCard, p1.getCurrentCard());
                System.out.println("p2 is defending from large");
                defendFromLargeCommand =new DefendFromLargeCommand(null,null,game.getID(),p2.GetID(),Gboard.getLevel(),"boh?","Placeholder");

                defendFromLargeCommand.execute(p2);
            }
            if (p1.getPlayerState().getClass().equals(HandleDestruction.class)) {
                System.out.println("HandleDestruction p1 per ora skippo :)");
                CurrentCard.setFinished(true);
            }
            else if(p2.getPlayerState().getClass().equals(HandleDestruction.class)) {
                System.out.println("HandleDestruction p2 :)");
                CurrentCard.setFinished(true);
            }
            i++;
        }

    }

    @Test
    public void testOpensSpaceCard() throws IOException {
        game.setGameBoard(Gboard);
        GAGen gag = new GAGen();
        ArrayList<Card> cards = gag.getCardsDeck();
        Card CurrentCard = cards.get(33);

        for (Player p : game.getGameBoard().getPlayers()) {
            p.setCard(CurrentCard);
        }

        CurrentCard.setBoard(Gboard);


        System.out.println("Id Card: " + CurrentCard.getId() + " " + CurrentCard.getClass().getName());
        CurrentCard.CardEffect();


        assertEquals(p1.getPlayerState().getClass(), GiveSpeed.class);
        assertEquals(false, p1.GetHasActed());
        assertEquals(p2.getPlayerState().getClass(), Waiting.class);
        assertEquals(true, p2.GetHasActed());

        ArrayList<IntegerPair> coords= new ArrayList<>();
        coords.clear();
        coords.add(new IntegerPair(8,3));//doppio
        coords.add(new IntegerPair(6,5));//singolo andrebbe ignorato

        GiveSpeedCommand  giveSpeedCommand =new GiveSpeedCommand(coords,game.getID(),p1.GetID(),Gboard.getLevel(),"boh?","Placeholder");
        giveSpeedCommand.execute(p1);
        System.out.println("roar");
        assertEquals(p1.getPlayerState().getClass(), ConsumingEnergy.class);
        coords.clear();
        coords.add(new IntegerPair(6,9));
        ConsumeEnergyCommand consumeEnergyCommand = new ConsumeEnergyCommand(coords,game.getID(),p1.GetID(),Gboard.getLevel(),"boh?","Placeholder");
        consumeEnergyCommand.execute(p1);

        assertEquals(p2.getPlayerState().getClass(), GiveSpeed.class);
        assertEquals(false, p2.GetHasActed());
        assertEquals(p1.getPlayerState().getClass(), Waiting.class);
        assertEquals(true, p1.GetHasActed());

        giveSpeedCommand =new GiveSpeedCommand(null,game.getID(),p2.GetID(),Gboard.getLevel(),"boh?","Placeholder");
        giveSpeedCommand.execute(p2);

        assertEquals(true,CurrentCard.isFinished());
    }

    @Test
    public void testSolarSystemCard() throws IOException {
        game.setGameBoard(Gboard);
        GAGen gag = new GAGen();
        ArrayList<Card> cards = gag.getCardsDeck();
        Card CurrentCard = cards.get(27);

        for (Player p : game.getGameBoard().getPlayers()) {
            p.setCard(CurrentCard);
        }

        CurrentCard.setBoard(Gboard);


        System.out.println("Id Card: " + CurrentCard.getId() + " " + CurrentCard.getClass().getName());
        CurrentCard.CardEffect();

        assertEquals(p1.getPlayerState().getClass(), ChoosingPlanet.class);
        assertEquals(false, p1.GetHasActed());
        assertEquals(p2.getPlayerState().getClass(), Waiting.class);
        assertEquals(true, p2.GetHasActed());
        System.out.println("p1 is choosing");
        ChoosingPlanetsCommand choosingPlanetsCommand = new ChoosingPlanetsCommand(-1,game.getID(),p1.GetID(),Gboard.getLevel(),"boh?","Placeholder");
        choosingPlanetsCommand.execute(p1);
        assertEquals(p2.getPlayerState().getClass(), ChoosingPlanet.class);
        assertEquals(false, p2.GetHasActed());
        assertEquals(p1.getPlayerState().getClass(), Waiting.class);
        assertEquals(true, p1.GetHasActed());
        System.out.println("p1 has chosen");
        choosingPlanetsCommand = new ChoosingPlanetsCommand(2,game.getID(),p2.GetID(),Gboard.getLevel(),"boh?","Placeholder");
        choosingPlanetsCommand.execute(p2);

        assertEquals(p2.getPlayerState().getClass(), HandleCargo.class);
        assertEquals(false, p2.GetHasActed());
        assertEquals(p1.getPlayerState().getClass(), Waiting.class);
        assertEquals(true, p1.GetHasActed());
        assertEquals(false,CurrentCard.isFinished());
        System.out.println("p2 has chosen");
        HandleCargoCommand handleCargoCommand = new HandleCargoCommand(0,null,game.getID(),p2.GetID(),Gboard.getLevel(),"Finish","placeholder");
    handleCargoCommand.execute(p2);

    assertEquals(p1.getPlayerState().getClass(), BaseState.class);
    assertEquals(p2.getPlayerState().getClass(), BaseState.class);
        assertEquals(true,CurrentCard.isFinished());

    }


}
