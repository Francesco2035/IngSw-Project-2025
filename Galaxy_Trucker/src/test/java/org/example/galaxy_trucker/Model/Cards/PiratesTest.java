package org.example.galaxy_trucker.Model.Cards;

import org.example.galaxy_trucker.Model.Boards.GameBoard;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.Connectors.UNIVERSAL;
import org.example.galaxy_trucker.Model.GAGen;
import org.example.galaxy_trucker.Model.Game;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.*;
import org.example.galaxy_trucker.Model.Tiles.ModularHousingUnit;
import org.example.galaxy_trucker.Model.Tiles.Tile;
import org.example.galaxy_trucker.TestSetupHelper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class PiratesTest {


    static Game TGame;

    static {
        try {
            TGame = new Game(2,"test");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    static GameBoard GameBoard=TGame.getGameBoard();

    static GAGen gag=TGame.getGag();



    static {
        try {
            gag = new GAGen();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    static Player Franci;
    static  Player Pietro;
    static  Player Passo;

    static PlayerBoard playerBoard1;
    static PlayerBoard playerBoard2;
    static PlayerBoard playerBoard3;






    @BeforeAll
    static void setup() {
        Player p1 = new Player();
        p1.setId("fGr");
        Player p2 = new Player();
        p2.setId("God");
        Player p3 = new Player();
        p3.setId("Sgregno");

        TGame.NewPlayer(p1);
        TGame.NewPlayer(p2);
        TGame.NewPlayer(p3);
        Franci= GameBoard.getPlayers().get(0);
        Pietro= GameBoard.getPlayers().get(1);
        Passo= GameBoard.getPlayers().get(2);
        playerBoard1=Pietro.getmyPlayerBoard();
        playerBoard2= TestSetupHelper.createInitializedBoard2();
        assertTrue(playerBoard2.checkValidity());


        Franci.setMyPlance(playerBoard2);
        System.out.println("boh111");
        //TestSetupHelper.HumansSetter2(Franci.getmyPlayerBoard());


        playerBoard3= TestSetupHelper.createInitializedBoard1();
        TestSetupHelper.HumansSetter1(playerBoard3);
        playerBoard3.checkValidity();
        Passo.setMyPlance(playerBoard3);



        ModularHousingUnit house1 =new ModularHousingUnit();
        ModularHousingUnit house2 =new ModularHousingUnit();


        IntegerPair cord = new IntegerPair(0,0);

        Tile tile1 = new Tile(house1, UNIVERSAL.INSTANCE,UNIVERSAL.INSTANCE,UNIVERSAL.INSTANCE,UNIVERSAL.INSTANCE);
        Tile tile2 = new Tile(house2, UNIVERSAL.INSTANCE,UNIVERSAL.INSTANCE,UNIVERSAL.INSTANCE,UNIVERSAL.INSTANCE);

        playerBoard1.insertTile(tile1,6,7, false);
        playerBoard1.insertTile(tile2,7,7, false);
        TestSetupHelper.HumansSetter1(playerBoard1);
        playerBoard1.checkValidity();

//        playerBoard1.setSetter(new HousingUnitSetter(playerBoard1,new IntegerPair(6,7),2,false,false));
//        playerBoard1.getSetter().set();
//        playerBoard1.setSetter(new HousingUnitSetter(playerBoard1,new IntegerPair(7,7),2,false,false));
//        playerBoard1.getSetter().set();

//        TestSetupHelper.HumansSetter1(playerBoard1);
//
//        TestSetupHelper.HumansSetter1(playerBoard3);
         Passo.EndConstruction();
        Pietro.EndConstruction();
        Franci.EndConstruction();

    }


    // se fai prima victory si rompe tutto perché passo perde tempo e il primo diventa pietro ma non è un malfunzionamento della carta


   @RepeatedTest(1000)
    void cardEffect() {
        ArrayList<Integer> attacks=new ArrayList<>();
        attacks.clear();

        attacks.add(1);
        attacks.add(1);
        attacks.add(1);
        attacks.add(1);
        attacks.add(3);
        attacks.add(0);

        Pirates carta= new Pirates(2,3,GameBoard,5,4,attacks);


        carta.CardEffect();
        assertEquals(new Waiting().getClass(),Pietro.getPlayerState().getClass());
        assertEquals(new Waiting().getClass(),Franci.getPlayerState().getClass());
        assertEquals(new GiveAttack().getClass(),Passo.getPlayerState().getClass());
        assertEquals(carta.getCurrentPlayer(),Passo);
        System.out.println("successo iniziale");

        carta.checkPower(3,0);
       // assertEquals(new ConsumingEnergy().getClass(),Passo.getPlayerState().getClass()); //passo ha power center in 5,4 e 6,9

        ArrayList<IntegerPair> energy= new ArrayList<>();
//        energy.clear();
//        energy.add(new IntegerPair(5,4));
//        energy.add(new IntegerPair(5,4));
//
//
//        carta.consumeEnergy(energy);
       System.out.println("siu");





      if (carta.getCurrentPlayer()==Passo){
          if (attacks.get(carta.getShotsOrder())==1) {
              assertEquals(new HandleDestruction().getClass(), Passo.getPlayerState().getClass());
              System.out.println("large");
          }
          else{
              System.out.println("small");
              assertEquals(new DefendingFromSmall().getClass(), Passo.getPlayerState().getClass());
              carta.DefendFromSmall(null,null);
              if (carta.getCurrentPlayer()==Passo) {
                  assertEquals(new HandleDestruction().getClass(), Passo.getPlayerState().getClass());
              }
              else {
                  assertEquals(new GiveAttack().getClass(), Pietro.getPlayerState().getClass());
                  carta.checkPower(4,0);
                  carta.checkPower(4,0);
                  assertEquals(new BaseState().getClass(),Franci.getPlayerState().getClass());
              }
          }
      }
      else{
          assertEquals(new GiveAttack().getClass(), Pietro.getPlayerState().getClass());
          carta.checkPower(4,0);
          carta.checkPower(4,0);
          assertEquals(new BaseState().getClass(),Franci.getPlayerState().getClass());
      }
//       carta.checkPower(4,0);
//       carta.checkPower(4,0);
//




    }

    @Test
    void victorycase() {

        ArrayList<Integer> attacks=new ArrayList<>();
        attacks.clear();

        attacks.add(1);
        attacks.add(1);
        attacks.add(1);
        attacks.add(1);
        attacks.add(3);
        attacks.add(0);

        Pirates carta= new Pirates(2,3,GameBoard,5,4,attacks);


        carta.CardEffect();
        assertEquals(new Waiting().getClass(),Pietro.getPlayerState().getClass());
        assertEquals(new Waiting().getClass(),Franci.getPlayerState().getClass());
        assertEquals(new GiveAttack().getClass(),Passo.getPlayerState().getClass());
        assertEquals(carta.getCurrentPlayer(),Passo);

        carta.checkPower(5,0);

        assertEquals(new Accepting().getClass(),Passo.getPlayerState().getClass());

        carta.continueCard(true);
        assertEquals(new BaseState().getClass(),Franci.getPlayerState().getClass());
        assertEquals(new BaseState().getClass(),Pietro.getPlayerState().getClass());
        assertEquals(new BaseState().getClass(),Passo.getPlayerState().getClass());


        }
}