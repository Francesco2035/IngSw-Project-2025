package org.example.galaxy_trucker.Model.Cards;

import org.example.galaxy_trucker.Model.Boards.GameBoard;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.Connectors.UNIVERSAL;
import org.example.galaxy_trucker.Model.GAGen;
import org.example.galaxy_trucker.Model.Game;
import org.example.galaxy_trucker.Model.Goods.GREEN;
import org.example.galaxy_trucker.Model.Goods.Goods;
import org.example.galaxy_trucker.Model.Goods.RED;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.*;
import org.example.galaxy_trucker.Model.Tiles.ModularHousingUnit;
import org.example.galaxy_trucker.Model.Tiles.Tile;
import org.example.galaxy_trucker.TestSetupHelper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class SmugglersTest {








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

    @Test
    void cardEffect() {
       ArrayList<Goods> reward=new ArrayList<>();
        reward.add(new RED());
        reward.add(new GREEN());
        reward.add(new GREEN());


         Smugglers carta= new Smugglers(2,3,GameBoard,reward,5,3);


        carta.CardEffect();
        assertEquals(new Waiting().getClass(),Pietro.getPlayerState().getClass());
        assertEquals(new Waiting().getClass(),Franci.getPlayerState().getClass());
        assertEquals(new GiveAttack().getClass(),Passo.getPlayerState().getClass());
        assertEquals(carta.getCurrentPlayer(),Passo);

        carta.checkPower(5,2);
        assertEquals(new ConsumingEnergy().getClass(),Passo.getPlayerState().getClass()); //passo ha power center in 5,4 e 6,9

        ArrayList<IntegerPair> energy= new ArrayList<>();
        energy.add(new IntegerPair(5,4));
        energy.add(new IntegerPair(5,4));


        carta.consumeEnergy(energy);
        System.out.println("sksk");
        assertEquals(new Waiting().getClass(),Passo.getPlayerState().getClass());

        System.out.println("wewe");
        assertEquals(new Waiting().getClass(),Passo.getPlayerState().getClass());  //da controllare che se ho cargo va in lose
        carta.checkPower(6, 0);
        System.out.println(Pietro.getmyPlayerBoard().getEnergy() + " energia");
        assertEquals(new Accepting().getClass(), Pietro.getPlayerState().getClass());
        carta.continueCard(false);

        assertEquals(new BaseState().getClass(), Pietro.getPlayerState().getClass());
        assertEquals(new BaseState().getClass(), Passo.getPlayerState().getClass());
        assertEquals(new BaseState().getClass(), Franci.getPlayerState().getClass());


    }

    @Test
    void loseCargo() {
    }
}