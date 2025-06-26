package org.example.galaxy_trucker.Model.Boards;

import org.example.galaxy_trucker.Commands.BuildingCommand;
import org.example.galaxy_trucker.Controller.CardsController;
import org.example.galaxy_trucker.Controller.Listeners.HandListener;
import org.example.galaxy_trucker.Controller.Listeners.PhaseListener;
import org.example.galaxy_trucker.ClientServer.Messages.HandEvent;
import org.example.galaxy_trucker.ClientServer.Messages.PhaseEvent;
import org.example.galaxy_trucker.Controller.PrepController;
import org.example.galaxy_trucker.Model.Game;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.BuildingShip;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConstructionTest {


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
      //  p1.setMyPlance(TestSetupHelper.createInitializedBoard1());
        System.out.println("\n");
       // p2.setMyPlance(TestSetupHelper.createInitializedBoard2());

        assertEquals(true, p1.getmyPlayerBoard().checkValidity());
        System.out.println("sksk");
        assertEquals(true, p2.getmyPlayerBoard().checkValidity());

//        TestSetupHelper.HumansSetter1(p1.getmyPlayerBoard());
//        TestSetupHelper.HumansSetter1(p2.getmyPlayerBoard());
        Gboard = game.getGameBoard();

//        Gboard.SetStartingPosition(p1);
//        Gboard.SetStartingPosition(p2);


        // CardsController c1= new CardsController(p1,game.getGameID(),false);
// CardsController c2= new CardsController(p2,game.getGameID(),false);

    }

    @Test
    public void pickingTilesTest() {
        game.setGameBoard(Gboard);
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
            p.setState(new BuildingShip());

        }
        PrepController prepController1 = new PrepController(p1,game.getID(),null,false);
        PrepController prepController2 = new PrepController(p2,game.getID(),null,false);

        //
        BuildingCommand prendiDaCovered1 = new BuildingCommand(0,0,0,-1,game.getID(),p1.GetID(),game.getLv(),"PICKTILE","placeholder");
        BuildingCommand prendiDaCovered2 = new BuildingCommand(0,0,0,-1,game.getID(),p2.GetID(),game.getLv(),"PICKTILE","placeholder");

        BuildingCommand discard1 =  new BuildingCommand(0,0,0,-1,game.getID(),p1.GetID(),game.getLv(),"DISCARD","placeholder");
        BuildingCommand discard2 =  new BuildingCommand(0,0,0,-1,game.getID(),p2.GetID(),game.getLv(),"DISCARD","placeholder");

        BuildingCommand prendiDaUncovered2 = new BuildingCommand(0,0,0,0,game.getID(),p2.GetID(),game.getLv(),"PICKTILE","placeholder");
        BuildingCommand prendiDaUncovered1 = new BuildingCommand(0,0,0,3,game.getID(),p1.GetID(),game.getLv(),"PICKTILE","placeholder");


        prepController1.action(prendiDaCovered1,null);
        prepController1.action(discard1,null);
        prepController1.action(prendiDaCovered1,null);
        prepController1.action(discard1,null);
        prepController2.action(prendiDaUncovered2,null);
        prepController2.action(discard2,null);
        prepController2.action(prendiDaCovered2,null);
        prepController2.action(discard2,null);
        prepController2.action(prendiDaCovered2,null);
        prepController1.action(prendiDaCovered1,null);
        prepController1.action(discard1,null);
        prepController2.action(discard2,null);
        prepController1.action(prendiDaUncovered1,null);






    }
}
