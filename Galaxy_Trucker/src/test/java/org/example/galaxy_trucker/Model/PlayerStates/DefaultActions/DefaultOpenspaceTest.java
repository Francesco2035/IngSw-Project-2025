package org.example.galaxy_trucker.Model.PlayerStates.DefaultActions;

import org.example.galaxy_trucker.Commands.GiveSpeedCommand;
import org.example.galaxy_trucker.Controller.CardsController;
import org.example.galaxy_trucker.Messages.ConcurrentCardListener;
import org.example.galaxy_trucker.Messages.ReadyListener;
import org.example.galaxy_trucker.Model.Boards.GameBoard;
import org.example.galaxy_trucker.Model.Cards.Card;
import org.example.galaxy_trucker.Model.GAGen;
import org.example.galaxy_trucker.Model.Game;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.BaseState;
import org.example.galaxy_trucker.Model.PlayerStates.ConsumingEnergy;
import org.example.galaxy_trucker.Model.PlayerStates.GiveSpeed;
import org.example.galaxy_trucker.Model.PlayerStates.Waiting;
import org.example.galaxy_trucker.NewTestSetupHelper;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DefaultOpenspaceTest {


    static Game game;

    static {
        try {
            game = new Game(2, "pippo");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static GameBoard Gboard = game.getGameBoard();

    static Player p1;
    static Player p2;

    // static CardsController c1 = new CardsController(p1,game.getGameID(),false);
    //static CardsController c2= new CardsController(p2,game.getGameID(),false);;


    @Test
    public void DefaultAbandonedStation() throws IOException, InterruptedException {
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

        Card CurrentCard = cards.get(32);

        CurrentCard.setConcurrentCardListener(conc);

        for (Player p : game.getGameBoard().getPlayers()) {
            p.setCard(CurrentCard);
        }

        CurrentCard.setBoard(Gboard);

        System.out.println("Id Card: " + CurrentCard.getId() + " " + CurrentCard.getClass().getName());

        //  c1.DefaultAction(null);
        CardsController c1 = new CardsController(p1, game.getGameID(), false);
        CardsController c2 = new CardsController(p2, game.getGameID(), false);

        CurrentCard.CardEffect();

        assertEquals(GiveSpeed.class,p1.getPlayerState().getClass());
        assertEquals(Waiting.class,p2.getPlayerState().getClass());

        ArrayList<IntegerPair> coord1 = new ArrayList<>();
        coord1.add(new IntegerPair(6, 4));

        c1.DefaultAction(null);

        assertEquals(GiveSpeed.class,p2.getPlayerState().getClass());
        assertEquals(Waiting.class,p1.getPlayerState().getClass());


        System.out.println("skusku");
        GiveSpeedCommand speed1 = new GiveSpeedCommand(coord1,game.getID(),p2.GetID(),game.getLv(),"","");
        speed1.execute(p2);

        assertEquals(Waiting.class,p1.getPlayerState().getClass());
        assertEquals(ConsumingEnergy.class,p2.getPlayerState().getClass());
        CurrentCard.setDefaultPunishment(3);

        try {
            c2.DefaultAction(null);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        assertTrue(!p2.GetHasActed());
        c2.DefaultAction(null);

        /// Todo nella consume energy il base state non prende in considerazione il fatto che potrei avere scelto più motori / cannoni di quante energie possiedo

        assertEquals(BaseState.class,p2.getPlayerState().getClass());
        assertEquals(BaseState.class,p1.getPlayerState().getClass());

        ReadyListener readyListener = new ReadyListener() {
            @Override
            public void onReady() {
                System.out.println("READY");
            }
        };

        p2.setReadyListener(readyListener);

        assertTrue(!p2.GetReady());
        assertTrue(!p2.GetHasActed());
        c2.DefaultAction(null);
        assertTrue(p2.GetReady());
        assertTrue(p2.GetHasActed());

        try{p2.setState(new GiveSpeed());
            p2.setCard(CurrentCard);
            CurrentCard.setDefaultPunishment(2);
            c2.DefaultAction(null);

        /// fine setup

    }catch(Exception e){
        e.printStackTrace();}
    }

    /// give speed ok  e consume energy ok se non è un punishment anche ready dovrebbe essere ok se ha il listener
}