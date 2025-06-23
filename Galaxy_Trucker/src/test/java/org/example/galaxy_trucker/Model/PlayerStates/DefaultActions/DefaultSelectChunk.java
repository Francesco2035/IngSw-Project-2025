package org.example.galaxy_trucker.Model.PlayerStates.DefaultActions;

import org.example.galaxy_trucker.Commands.HandleCargoCommand;
import org.example.galaxy_trucker.Controller.CardsController;
import org.example.galaxy_trucker.Controller.Messages.ConcurrentCardListener;
import org.example.galaxy_trucker.Model.Boards.GameBoard;
import org.example.galaxy_trucker.Model.Cards.Card;
import org.example.galaxy_trucker.Model.GAGen;
import org.example.galaxy_trucker.Model.Game;
import org.example.galaxy_trucker.Model.Goods.BLUE;
import org.example.galaxy_trucker.Model.Goods.GREEN;
import org.example.galaxy_trucker.Model.Goods.Goods;
import org.example.galaxy_trucker.Model.Goods.YELLOW;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.HandleCargo;
import org.example.galaxy_trucker.NewTestSetupHelper;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class DefaultSelectChunk {





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


    @RepeatedTest(100)
    //@Test
    public void testSelectChunk() throws Exception {
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
        p2.setMyPlance(helper.createInitializedBoard4());

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

        Card CurrentCard = cards.get(35);

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



        int i =0;
        try{
            while(!CurrentCard.isFinished() && i<15){
                System.out.println("\n\n prima che p1 agisca: \n p1: "+p1.getPlayerState().getClass()+ p1.GetHasActed()+"\n p2: "+p2.getPlayerState().getClass()+ p2.GetHasActed());

                if(!p1.GetHasActed()){

                    c1.DefaultAction(null);
                }
                System.out.println("\n\n dopo che p1 ha agito: \n p1: "+p1.getPlayerState().getClass()+ p1.GetHasActed()+"\n p2: "+p2.getPlayerState().getClass()+ p2.GetHasActed());

                if(!p2.GetHasActed() && !CurrentCard.isFinished()){
                    c2.DefaultAction(null);
                }
                i++;
            }
        }catch (Exception E){
            E.printStackTrace();
            throw new Exception(E.getMessage());
        }




    }
}
