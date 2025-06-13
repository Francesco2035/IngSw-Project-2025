package org.example.galaxy_trucker.Model.Cards;

import org.example.galaxy_trucker.Commands.ConsumeEnergyCommand;
import org.example.galaxy_trucker.Commands.GiveAttackCommand;
import org.example.galaxy_trucker.Commands.TheftCommand;
import org.example.galaxy_trucker.Controller.CardsController;
import org.example.galaxy_trucker.Controller.Listeners.HandListener;
import org.example.galaxy_trucker.Controller.Listeners.PhaseListener;
import org.example.galaxy_trucker.Controller.Messages.HandEvent;
import org.example.galaxy_trucker.Controller.Messages.PhaseEvent;
import org.example.galaxy_trucker.Model.Boards.Actions.AddGoodAction;
import org.example.galaxy_trucker.Model.Boards.GameBoard;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.GAGen;
import org.example.galaxy_trucker.Model.Game;
import org.example.galaxy_trucker.Model.Goods.BLUE;
import org.example.galaxy_trucker.Model.Goods.Goods;
import org.example.galaxy_trucker.Model.Goods.YELLOW;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.*;
import org.example.galaxy_trucker.Model.Tiles.Tile;
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
    public void testSteal() throws IOException, InterruptedException {
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



        ArrayList<Goods> RewardList = new ArrayList<Goods>();

        RewardList.add(new BLUE());
        RewardList.add(new BLUE());
        RewardList.add(new BLUE());
        RewardList.add(new YELLOW());

        p1.setState(new HandleCargo());
        p1.getmyPlayerBoard().setRewards(RewardList);


        PlayerBoard playerBoard = p1.getmyPlayerBoard();
        PlayerState state = p1.getPlayerState();
        Tile specialStorage= playerBoard.getTile(7,8);

        System.out.println(specialStorage.getComponent().getClass() + " space is " + specialStorage.getComponent().getType());
        playerBoard.performAction(specialStorage.getComponent(),new AddGoodAction(playerBoard.getFromRewards(3),playerBoard,7,8),state);
        Tile normalStorage= playerBoard.getTile(7,9);
        playerBoard.performAction(normalStorage.getComponent(),new AddGoodAction(playerBoard.getFromRewards(0),playerBoard,7,9),state);
        playerBoard.performAction(normalStorage.getComponent(),new AddGoodAction(playerBoard.getFromRewards(0),playerBoard,7,9),state);


        System.out.println("\n");




        /// attiva smugglers
        CurrentCard.CardEffect();


        assertEquals(p1.getPlayerState().getClass(), GiveAttack.class);
//        assertEquals(p2.getPlayerState().getClass(), Waiting.class);
        ArrayList<IntegerPair> coordinates = new ArrayList<>();
        coordinates.clear();
        GiveAttackCommand fight = new GiveAttackCommand(coordinates,game.getID(),p1.GetID(),game.getLv(),"GiveAttackCommand","SK");
        fight.execute(p1);


        if (p1.getmyPlayerBoard().getStoredGoods() != null) {
            assertEquals(HandleTheft.class,p1.getPlayerState().getClass());

            TheftCommand theftCommand1 = new TheftCommand(0,new IntegerPair(7,8),game.getID(),p1.GetID(),game.getLv(),"theft","boh");
            theftCommand1.execute(p1);
            //theftCommand1.execute(p1);
            TheftCommand theftCommand2 = new TheftCommand(0,new IntegerPair(7,9),game.getID(),p1.GetID(),game.getLv(),"theft","boh");
            theftCommand2.execute(p1);
            theftCommand2.execute(p1);

        }
        else {
            assertEquals(p1.getPlayerState().getClass(), ConsumingEnergy.class);


            coordinates.add(new IntegerPair(6, 9));
            coordinates.add(new IntegerPair(6, 9));
            coordinates.add(new IntegerPair(6, 9));
            ConsumeEnergyCommand consume = new ConsumeEnergyCommand(coordinates, game.getID(), p1.GetID(), game.getLv(), "GiveAttackCommand", "SK");
            consume.execute(p1);
        }

    }


}
