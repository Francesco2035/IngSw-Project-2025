package org.example.galaxy_trucker;

import org.example.galaxy_trucker.Commands.BuildingCommand;
import org.example.galaxy_trucker.Commands.FinishBuildingCommand;
import org.example.galaxy_trucker.Controller.Listeners.HandListener;
import org.example.galaxy_trucker.Controller.Listeners.PhaseListener;
import org.example.galaxy_trucker.Controller.Messages.HandEvent;
import org.example.galaxy_trucker.Controller.Messages.PhaseEvent;
import org.example.galaxy_trucker.Controller.PrepController;
import org.example.galaxy_trucker.Model.Boards.GameBoard;
import org.example.galaxy_trucker.Model.Boards.Hourglass;
import org.example.galaxy_trucker.Model.Game;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.BuildingShip;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;


class HourglassTest {


//    @Test
//    void startTimer() throws IOException {
//
//        Player p1  = new Player();
//        p1.setId("bubu");
//
//        Game g = new Game(2, "gameid");
//        g.NewPlayer(p1);
//        GameBoard gb = g.getGameBoard();
//        Hourglass h = gb.getHourglass();
//
//        p1.StartTimer();
//        while(!h.isStartable()){
////            System.out.println("hrg 1");
//        }
//
//        p1.StartTimer();
//        try {
//            p1.StartTimer();
//        }catch (RuntimeException e){
//            System.out.println(e.getMessage());
//        }
//
//        while(!h.isStartable()){
////            System.out.println("hrg 2");
//        }
//
//        try {
//            p1.StartTimer();
//        }catch (Exception e){
//            System.out.println(e.getMessage());
//        }
//
//        assertEquals(1, h.getUsages());
//
//        p1.SetReady(true);
//        p1.StartTimer();
//
//        while(h.getUsages() > 0){
////            System.out.println("hrg 3");
//        }
//
//        try {
//            p1.StartTimer();
//        }catch (Exception e){
//            System.out.println(e.getMessage());
//        }
//
//        assertEquals(0, h.getUsages());
//    }

    @Test
    public  void  newTest() throws IOException, InterruptedException {

        Game game = new Game(2, "HourglassTestController");


        Player p1 = new Player();
        p1.setId("pietro");
       Player p2 = new Player();
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
        GameBoard Gboard = game.getGameBoard();

//        Gboard.SetStartingPosition(p1);
//        Gboard.SetStartingPosition(p2);


        // CardsController c1= new CardsController(p1,game.getGameID(),false);
// CardsController c2= new CardsController(p2,game.getGameID(),false);


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
        Gboard.StartHourglass();
        BuildingCommand Hourglass = new BuildingCommand(0,0,0,-1,game.getID(),p1.GetID(),game.getLv(),"HOURGLASS","placeholder");
        FinishBuildingCommand finish = new FinishBuildingCommand(2,game.getID(),p1.GetID(),game.getLv(),"FINISHBUILDING","placeholder");
        BuildingCommand prendiDaCovered1 = new BuildingCommand(0,0,0,-1,game.getID(),p1.GetID(),game.getLv(),"PICKTILE","placeholder");
        BuildingCommand discard1 =  new BuildingCommand(0,0,0,-1,game.getID(),p1.GetID(),game.getLv(),"DISCARD","placeholder");

        prepController1.action(prendiDaCovered1,null);
        System.out.println("\n\n urgirpim\n\n");
      //  prepController1.action(Hourglass,null);
        Thread.sleep(600);

        prepController1.action(discard1,null);
        prepController1.action(Hourglass,null);
        prepController1.action(finish,null);
        System.out.println("\n\n sgrunger\n\n");
        Thread.sleep(600);
        System.out.println("\n\n sbledig\n\n");
        Thread.sleep(600);
        prepController1.action(Hourglass,null);
        System.out.println("ultimo hourglass chiamato :)");
        Thread.sleep(600);

        System.out.println("\n\n finetest\n\n");
        //prepController1.action(Hourglass,null);


    }
}