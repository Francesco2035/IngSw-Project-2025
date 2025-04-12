package org.example.galaxy_trucker.Model.Cards;

import org.example.galaxy_trucker.Model.*;
import org.example.galaxy_trucker.Model.Boards.GameBoard;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
//import org.example.galaxy_trucker.Model.InputHandlers.AcceptKilling;

import org.example.galaxy_trucker.Model.Connectors.*;
import org.example.galaxy_trucker.Model.PlayerStates.BaseState;
import org.example.galaxy_trucker.Model.PlayerStates.Waiting;
import org.example.galaxy_trucker.Model.Tiles.ModularHousingUnit;
import org.example.galaxy_trucker.Model.Tiles.Tile;
import org.example.galaxy_trucker.TestSetupHelper;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

//test is pretty through
//it's just missing a test on the positioning on the players because i don't know how to check it

class AbandonedShipTest {

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




    static  AbandonedShip FakeAbandonedShip=new AbandonedShip(3,5,2,5, GameBoard);


    @BeforeAll
    static void setup() {

        TGame.NewPlayer("fGr");
        TGame.NewPlayer("God");
        TGame.NewPlayer("Sgregno");
         Franci= GameBoard.getPlayers().get(0);
         Pietro= GameBoard.getPlayers().get(1);
         Passo= GameBoard.getPlayers().get(2);
        playerBoard1=Pietro.getmyPlayerBoard();
        playerBoard2=Franci.getmyPlayerBoard();
        playerBoard3= TestSetupHelper.createInitializedBoard1();
        TestSetupHelper.HumansSetter1(playerBoard3);
        playerBoard3.checkValidity();
        Passo.setMyPlance(playerBoard3);



        ModularHousingUnit house1 =new ModularHousingUnit();
        ModularHousingUnit house2 =new ModularHousingUnit();


        IntegerPair cord = new IntegerPair(0,0);

        Tile tile1 = new Tile(house1, UNIVERSAL.INSTANCE,UNIVERSAL.INSTANCE,UNIVERSAL.INSTANCE,UNIVERSAL.INSTANCE);
        Tile tile2 = new Tile(house2, UNIVERSAL.INSTANCE,UNIVERSAL.INSTANCE,UNIVERSAL.INSTANCE,UNIVERSAL.INSTANCE);

        playerBoard1.insertTile(tile1,6,7);
        playerBoard1.insertTile(tile2,7,7);
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
    @Order(1)
    void cardEffect() {

        FakeAbandonedShip.CardEffect();
        assertTrue(GameBoard.getPlayers().size()==3);
       Franci= GameBoard.getPlayers().get(2);
       Pietro= GameBoard.getPlayers().get(1);
        assertEquals(Franci.GetID(),"fGr");
        assertEquals(Pietro.GetID(),"God");

        assertEquals(FakeAbandonedShip.getCurrentPlayer().GetID(),"Sgregno");
        System.out.println("controllo num");
        assertEquals(8,FakeAbandonedShip.getTotHumans());
        System.out.println("fine controllo");

        //assertEquals(Franci.getPlayerState(), new Waiting());
        //assertEquals(Passo.getPlayerState(), PlayerStatesss.AcceptKilling);
       // assertEquals(Pietro.getPlayerState(), PlayerStatesss.Waiting);

    }


    @Test
    @Order(3)
    void activateCard() {
        Pietro=GameBoard.getPlayers().get(1);
        System.out.println(Passo.GetID()+"state"+Passo.getPlayerState());

                ArrayList<IntegerPair> coords= new ArrayList<>();
        IntegerPair c1= new IntegerPair(6,6);
        IntegerPair c2= new IntegerPair(6,6);
        IntegerPair c3= new IntegerPair(4,5);
        coords.add(c1);
        coords.add(c2);
        coords.add(c3);


        FakeAbandonedShip.killHumans(coords);
//        handler=(AcceptKilling) Pietro.getInputHandler();
//        handler.setInput(coords,false);
//        Pietro.execute();

        assertEquals(BaseState.class,Passo.getPlayerState().getClass());
System.out.println(Passo.GetID()+"state"+Passo.getPlayerState());


    }

    @Test
    void finishCard() {
    }

    @Test
    void continueCard() {
    }
}