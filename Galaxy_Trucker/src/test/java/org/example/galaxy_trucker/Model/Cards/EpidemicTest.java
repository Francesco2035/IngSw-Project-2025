package org.example.galaxy_trucker.Model.Cards;

import org.example.galaxy_trucker.Model.Boards.GameBoard;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.GAGen;
import org.example.galaxy_trucker.Model.Game;
import org.example.galaxy_trucker.Model.GetterHandler.HousingUnitGetter;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.TestSetupHelper;
import org.junit.jupiter.api.*;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

//manca il test su più player
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

class EpidemicTest {



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

    static PlayerBoard playerBoard1;

    static Epidemic epidemicTest=new Epidemic(2,0,TGameBoard);

    @BeforeAll
    static void setUp() {
        TGame.NewPlayer("fGr");


        Franci= TGameBoard.getPlayers().get(0);


        playerBoard1= TestSetupHelper.createInitializedBoard2();
        assertTrue(playerBoard1.checkValidity());


        Franci.setMyPlance(playerBoard1);
        System.out.println("boh111");
       TestSetupHelper.HumansSetter2(Franci.getMyPlance());

    }

    @Test
    void cardEffect() {
        //IntegerPair coordinate= new IntegerPair(5,6);


//        playerBoard1.setGetter(new HousingUnitGetter(playerBoard1, HypoteticalCoordinate, 1, false, false));
//        playerBoard1.getGetter().get();
//        playerBoard1.getGetter().get();

        epidemicTest.CardEffect();
        Franci= TGameBoard.getPlayers().get(0);

    }

    @Test
    void findPaths() {
    }
}