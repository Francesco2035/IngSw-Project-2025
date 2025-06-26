package org.example.galaxy_trucker.Model.PlayerStates.DefaultActions;

import org.example.galaxy_trucker.Commands.AcceptCommand;
import org.example.galaxy_trucker.Commands.HandleCargoCommand;
import org.example.galaxy_trucker.Controller.CardsController;
import org.example.galaxy_trucker.ClientServer.Messages.ConcurrentCardListener;
import org.example.galaxy_trucker.Model.Boards.GameBoard;
import org.example.galaxy_trucker.Model.Cards.Card;
import org.example.galaxy_trucker.Model.GAGen;
import org.example.galaxy_trucker.Model.Game;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.Accepting;
import org.example.galaxy_trucker.Model.PlayerStates.BaseState;
import org.example.galaxy_trucker.Model.PlayerStates.HandleCargo;
import org.example.galaxy_trucker.Model.PlayerStates.Waiting;

import org.example.galaxy_trucker.NewTestSetupHelper;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DefaultAbandonedStation {
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

        Card CurrentCard = cards.get(10);

        CurrentCard.setConcurrentCardListener(conc);

        for (Player p : game.getGameBoard().getPlayers()) {
            p.setCard(CurrentCard);
        }

        CurrentCard.setBoard(Gboard);

        System.out.println("Id Card: " + CurrentCard.getId() + " " + CurrentCard.getClass().getName());

       //  c1.DefaultAction(null);
        CardsController c1 = new CardsController(p1,game.getGameID(),false);
        CardsController c2= new CardsController(p2,game.getGameID(),false);

        CurrentCard.CardEffect();


        ///fine setup


        assertEquals(Accepting.class, p1.getPlayerState().getClass());
        AcceptCommand acceptCommand1 =new AcceptCommand(game.getID(),p1.GetID(),game.getLv(),"decline",false,"bpoh");
        acceptCommand1.execute(p1);

        assertEquals(Accepting.class, p2.getPlayerState().getClass());
        assertEquals(Waiting.class, p1.getPlayerState().getClass());

        AcceptCommand acceptCommand2 =new AcceptCommand(game.getID(),p2.GetID(),game.getLv(),"Accept",true,"bpoh");
        acceptCommand2.execute(p2);

        assertEquals(HandleCargo.class, p2.getPlayerState().getClass());
        HandleCargoCommand put = new HandleCargoCommand(0,new IntegerPair(5,4),0,new IntegerPair(7,8),game.getID(),p2.GetID(),game.getLv(),"GetFromRewards","boh");
        put.execute(p2);

        c2.DefaultAction(null);
        assertEquals(BaseState.class, p1.getPlayerState().getClass());
    }

    /// Default action accept e handle cargo OK
}
