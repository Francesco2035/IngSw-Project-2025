package org.example.galaxy_trucker.Model.CardsCoverage;

import org.example.galaxy_trucker.Commands.AcceptCommand;
import org.example.galaxy_trucker.Commands.ConsumeEnergyCommand;
import org.example.galaxy_trucker.Commands.DebugShip;
import org.example.galaxy_trucker.Commands.GiveAttackCommand;
import org.example.galaxy_trucker.Controller.CardsController;
import org.example.galaxy_trucker.Controller.Messages.ConcurrentCardListener;
import org.example.galaxy_trucker.Model.Boards.GameBoard;
import org.example.galaxy_trucker.Model.Cards.Card;
import org.example.galaxy_trucker.Model.Connectors.UNIVERSAL;
import org.example.galaxy_trucker.Model.GAGen;
import org.example.galaxy_trucker.Model.Game;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.Accepting;
import org.example.galaxy_trucker.Model.Tiles.MainCockpitComp;
import org.example.galaxy_trucker.Model.Tiles.Tile;
import org.example.galaxy_trucker.NewTestSetupHelper;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SlaversCoverage {


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
    public void DefaultSlavers() throws IOException, InterruptedException {
        Game game = new Game(2, "testCarteController");
        NewTestSetupHelper helper = new NewTestSetupHelper();

        p1 = new Player();
        p1.setId("pietro");
        p2 = new Player();
        p2.setId("FRA");
        game.NewPlayer(p1);
        game.NewPlayer(p2);

        p1.getmyPlayerBoard().insertTile(new Tile(new MainCockpitComp(),UNIVERSAL.getInstance(), UNIVERSAL.getInstance(), UNIVERSAL.getInstance(), UNIVERSAL.getInstance()),6,6,false);

        p2.getmyPlayerBoard().insertTile(new Tile(new MainCockpitComp(),UNIVERSAL.getInstance(), UNIVERSAL.getInstance(), UNIVERSAL.getInstance(), UNIVERSAL.getInstance()),6,6,false);

        DebugShip debugShip1 = new DebugShip(game.getID(),p1.GetID(),game.getLv(),"ub","h",0);


        DebugShip debugShip2 = new DebugShip(game.getID(),p2.GetID(),game.getLv(),"ub","h",1);

       // p1.setMyPlance(helper.createInitializedBoard1());

        debugShip1.execute(p1);
        System.out.println("\n");
        //p2.setMyPlance(helper.createInitializedBoard2());
        debugShip2.execute(p2);

        assertTrue(p1.getmyPlayerBoard().checkValidity());
        System.out.println("sksk");
        assertTrue(p2.getmyPlayerBoard().checkValidity());

        helper.HumansSetter3(p1.getmyPlayerBoard());
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

        Card CurrentCard = cards.get(0);

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


        ArrayList<IntegerPair> coords1= new ArrayList<>();

        coords1.add(new IntegerPair(5,6));
        coords1.add(new IntegerPair(5,4));

        ArrayList<IntegerPair> coords2= new ArrayList<>();
        coords2.add(new IntegerPair(5,5));
        coords2.add(new IntegerPair(5,5));


        GiveAttackCommand attack = new GiveAttackCommand(coords1,game.getID(),p1.GetID(),game.getLv(),"","");
        attack.execute(p1);

        ConsumeEnergyCommand consume = new ConsumeEnergyCommand(coords2,game.getID(),p1.GetID(),game.getLv(),"","");
        consume.execute(p1);

        assertEquals(Accepting.class,p1.getPlayerState().getClass());

        AcceptCommand accept = new AcceptCommand(game.getID(),p1.GetID(),game.getLv(),"",true,"");
        accept.execute(p1);

    }
}