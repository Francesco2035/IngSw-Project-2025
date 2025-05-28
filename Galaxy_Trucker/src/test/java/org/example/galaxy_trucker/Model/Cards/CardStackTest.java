package org.example.galaxy_trucker.Model.Cards;

import org.example.galaxy_trucker.Model.Boards.GameBoard;
import org.example.galaxy_trucker.Model.Game;
import org.example.galaxy_trucker.Model.Player;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CardStackTest {
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


    @BeforeAll
    public static void init() throws IOException {


    }



    @Test
    public void cardStackTest() throws InterruptedException {
        CardStacks cardStacks= game.getGameBoard().getCardStack();
        GameBoard gameBoard = game.getGameBoard();
        cardStacks.mergeDecks();
        Card card;

        assertEquals(12,game.getGameBoard().getCardStack().getFullAdventure().size());

         card = gameBoard.NewCard();

         System.out.println(card.getId());

         card = gameBoard.NewCard();
         System.out.println(card.getId());

         card = gameBoard.NewCard();
         System.out.println(card.getId());
         card = gameBoard.NewCard();
         System.out.println(card.getId());



    }
}
