package org.example.galaxy_trucker.Model.Cards;

import org.example.galaxy_trucker.Controller.CardsController;
import org.example.galaxy_trucker.Model.Boards.GameBoard;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.Connectors.UNIVERSAL;
import org.example.galaxy_trucker.Model.GAGen;
import org.example.galaxy_trucker.Model.Game;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.Tiles.ModularHousingUnit;
import org.example.galaxy_trucker.Model.Tiles.Tile;
import org.example.galaxy_trucker.TestSetupHelper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class StardustTest {


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
   // static Player p2 = new Player();

    CardsController c1 = new CardsController(p1, game.getGameID(), false);
    //CardsController c2 = new CardsController(p2, game.getGameID(), false);


    @BeforeAll
    public static void init() throws IOException {
        Game game = new Game(2, "testCarteController");


        p1 = new Player();
        p1.setId("pietro");
//        p2 = new Player();
//        p2.setId("FRA");
        game.NewPlayer(p1);
       // game.NewPlayer(p2);
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
    void cardEffect() throws IOException {
        game.setGameBoard(Gboard);
        GAGen gag = new GAGen();
        ArrayList<Card> cards = gag.getCardsDeck();
        Card CurrentCard = cards.get(38);
        CurrentCard.setBoard(Gboard);
        try{
            CurrentCard.CardEffect();
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
        System.out.println("fine");


    }

}