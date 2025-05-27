package org.example.galaxy_trucker.Model.Cards;

import org.example.galaxy_trucker.Model.Boards.GameBoard;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.GAGen;
import org.example.galaxy_trucker.Model.Game;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.HandleDestruction;
import org.example.galaxy_trucker.TestSetupHelper;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;



@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MeteoritesTest {




    static Game TGame;

    static {
        try {
            TGame = new Game(2,"test");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    static GameBoard TGameBoard=TGame.getGameBoard();

    static GAGen gag=TGame.getGag();

    static IntegerPair HypoteticalCoordinate= new IntegerPair(5,5);



    static {
        try {
            gag = new GAGen();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    static Player Franci;

    static Player pie;

    static PlayerBoard playerBoard1;
    static ArrayList<Integer> attacks =new ArrayList<>();


    @BeforeAll
    static void setUp() {
        TGame.NewPlayer(new Player());
        Player p2 = new Player();
        p2.setId("pie");
        TGame.NewPlayer(p2);


        Franci= TGameBoard.getPlayers().get(0);

        Franci.setId("fGr");

        pie= TGameBoard.getPlayers().get(1);


        playerBoard1= TestSetupHelper.createInitializedBoard2();
        assertTrue(playerBoard1.checkValidity());


        Franci.setMyPlance(playerBoard1);
        System.out.println("boh111");
        TestSetupHelper.HumansSetter2(Franci.getmyPlayerBoard());


        Franci.EndConstruction();
        pie.EndConstruction();


    }

    @Test
    void cardEffect() {
                attacks.clear();
//        //0
//        attacks.add(1);
//        attacks.add(1);
//        //1
//        attacks.add(2);
//        attacks.add(0);
//        //2
//        attacks.add(0);
//        attacks.add(0);
//        //3
//        attacks.add(0);
//        attacks.add(1);
//        //4
//        attacks.add(3);
//        attacks.add(1);


        //sx
        attacks.add(0);
        attacks.add(0);
        attacks.add(0);
        attacks.add(1);
//        //sopr
//        attacks.add(1);
//        attacks.add(0);
//        attacks.add(1);
//        attacks.add(1);
//        //dx
//        attacks.add(2);
//        attacks.add(0);
//        attacks.add(2);
//        attacks.add(1);
////        //sott
//        attacks.add(3);
//        attacks.add(0);
//        attacks.add(3);
//        attacks.add(1);


        Meteorites meteoritesTest=new Meteorites(2,0,TGameBoard,attacks);
        meteoritesTest.CardEffect();
    }

    @Test
    void updateSates() {
    }

    @Test
    void defendFromMeteorites() {
        attacks.clear();

        attacks.add(1);
        attacks.add(1);
        attacks.add(1);
        attacks.add(1);
        attacks.add(3);
        attacks.add(0);

        Meteorites meteoritesTest=new Meteorites(2,0,TGameBoard,attacks);

     //   meteoritesTest.setMeteoritesOrder(2);
        System.out.println(meteoritesTest.getAttacks().get(meteoritesTest.getMeteoritesOrder()));


        Player current;
        current= TGameBoard.getPlayers().get(0);

        meteoritesTest.setCurrentPlayer(current);

        meteoritesTest.setHit(8,8);

        //assertEquals(2,current.getmyPlayerBoard().getTile(7,8).getComponent().getType());
        meteoritesTest.DefendFromLarge(null,null, null);
        assertEquals(HandleDestruction.class,current.getPlayerState().getClass());







    }
}