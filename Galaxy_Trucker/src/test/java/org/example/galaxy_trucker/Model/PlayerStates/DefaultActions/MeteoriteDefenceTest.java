package org.example.galaxy_trucker.Model.PlayerStates.DefaultActions;

import org.example.galaxy_trucker.Commands.DefendFromLargeCommand;
import org.example.galaxy_trucker.Commands.DefendFromSmallCommand;
import org.example.galaxy_trucker.Controller.CardsController;
import org.example.galaxy_trucker.ClientServer.Messages.ConcurrentCardListener;
import org.example.galaxy_trucker.Model.Boards.GameBoard;
import org.example.galaxy_trucker.Model.Cards.Card;
import org.example.galaxy_trucker.Model.Cards.Meteorites;
import org.example.galaxy_trucker.Model.GAGen;
import org.example.galaxy_trucker.Model.Game;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.DefendingFromLarge;
import org.example.galaxy_trucker.Model.PlayerStates.DefendingFromSmall;
import org.example.galaxy_trucker.NewTestSetupHelper;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class MeteoriteDefenceTest {





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


    @Test
    public void DefaultMeteorites() throws IOException, InterruptedException {
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

        Card CurrentCard = new Meteorites(2, 0, Gboard, attacks);

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
       // CurrentCard.CardEffect();


        /// fine setup

        HashMap hits = new HashMap();
        hits.put(p1.GetID(),new IntegerPair(0,0));
        hits.put(p2.GetID(),new IntegerPair(0,0));

        ((Meteorites) CurrentCard).setHits(hits);

        ((Meteorites) CurrentCard).setMeteoritesOrder(8);
        ((Meteorites) CurrentCard).setHit(8,9,p1);
        p1.setState(new DefendingFromSmall());


        System.out.println("\n betty \n");
        IntegerPair battery1 = new IntegerPair(6,9);
        IntegerPair plasma1 = new IntegerPair(8,9);
        DefendFromSmallCommand def1 = new DefendFromSmallCommand(battery1,game.getID(),p1.GetID(),game.getLv(),"boh","boh");
//        FlightController c3 = new FlightController(p1, game.getID(), new GameController(game.getID(), game, new GamesHandler(), 2, 4), false); //possibile fix
        try{
            def1.execute(p1);
        }catch (Exception e){
            e.printStackTrace();
        }


        System.out.println("\n surtr \n");
        ((Meteorites) CurrentCard).setMeteoritesOrder(10);
        ((Meteorites) CurrentCard).setHit(7,7,p1);
        p1.setState(new DefendingFromLarge());


        DefendFromLargeCommand def2 = new DefendFromLargeCommand(plasma1,battery1,game.getID(),p1.GetID(),game.getLv(),"boh","boh");
//        def2.execute(p1);

        try{
            def2.execute(p1);
        }catch (Exception e){
            e.printStackTrace();
        }

        System.out.println("\n roar \n");
        IntegerPair battery2 = new IntegerPair(8,8);
        IntegerPair plasma2 = new IntegerPair(4,5);
        ((Meteorites) CurrentCard).setMeteoritesOrder(6);
        ((Meteorites) CurrentCard).setHit(4,5,p2);
        p2.setState(new DefendingFromLarge());

        DefendFromLargeCommand def3 = new DefendFromLargeCommand(plasma2,battery2,game.getID(),p2.GetID(),game.getLv(),"boh","boh");
        def3.execute(p2);

    }
}
