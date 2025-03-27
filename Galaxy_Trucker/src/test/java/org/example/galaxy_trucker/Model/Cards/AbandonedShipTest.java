package org.example.galaxy_trucker.Model.Cards;

import org.example.galaxy_trucker.Model.*;
import org.example.galaxy_trucker.Model.Boards.GameBoard;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.InputHandlers.AcceptKilling;
import org.example.galaxy_trucker.Model.SetterHandler.HousingUnitSetter;
import org.example.galaxy_trucker.Model.Tiles.Connector;
import org.example.galaxy_trucker.Model.Tiles.Tile;
import org.example.galaxy_trucker.Model.Tiles.modularHousingUnit;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)


class AbandonedShipTest {

    static Game TGame;

    static {
        try {
            TGame = new Game(2);
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

    static PlayerBoard playerBoard1;
    static PlayerBoard playerBoard2;




    static  AbandonedShip FakeAbandonedShip=new AbandonedShip(3,5,2,5, GameBoard);


    @BeforeAll
    static void setup() {

        TGame.NewPlayer("fGr");
        TGame.NewPlayer("God");
         Franci= GameBoard.getPlayers().get(0);
         Pietro= GameBoard.getPlayers().get(1);
        playerBoard1=Pietro.getMyPlance();
        playerBoard2=Franci.getMyPlance();

        modularHousingUnit house1 =new modularHousingUnit();
        modularHousingUnit house2 =new modularHousingUnit();

        IntegerPair cord = new IntegerPair(0,0);

        Tile tile1 = new Tile(cord,house1, Connector.UNIVERSAL,Connector.UNIVERSAL,Connector.UNIVERSAL,Connector.UNIVERSAL);
        Tile tile2 = new Tile(cord,house2, Connector.UNIVERSAL,Connector.UNIVERSAL,Connector.UNIVERSAL,Connector.UNIVERSAL);

        playerBoard1.insertTile(tile1,6,7);
        playerBoard1.insertTile(tile2,7,7);
        playerBoard1.checkValidity();

        playerBoard1.setSetter(new HousingUnitSetter(playerBoard1,new IntegerPair(6,7),2,false,false));
        playerBoard1.getSetter().set();
        playerBoard1.setSetter(new HousingUnitSetter(playerBoard1,new IntegerPair(7,7),2,false,false));
        playerBoard1.getSetter().set();

    }

    @Test
    @Order(1)
    void cardEffect() {

        FakeAbandonedShip.CardEffect();
        assertTrue(GameBoard.getPlayers().size()==2);
       Franci= GameBoard.getPlayers().get(0);
       Pietro= GameBoard.getPlayers().get(1);
        assertEquals(Franci.GetID(),"fGr");
        assertEquals(Pietro.GetID(),"God");

        System.out.println("controllo num");
        assertEquals(4,FakeAbandonedShip.getTotHumans());
        System.out.println("fine controllo");

        assertEquals(Franci.getPlayerState(),PlayerStates.Waiting);
        assertEquals(Pietro.getPlayerState(), PlayerStates.AcceptKilling);



    }


    @Test
    @Order(3)
    void activateCard() {
        Pietro=GameBoard.getPlayers().get(1);
        System.out.println(Pietro.GetID()+"state"+Pietro.getPlayerState());
        AcceptKilling handler = (AcceptKilling) Pietro.getInputHandler();
        ArrayList<IntegerPair> coords= new ArrayList<>();
        IntegerPair c1= new IntegerPair(6,7);
        IntegerPair c2= new IntegerPair(7,7);
        IntegerPair c3= new IntegerPair(7,7);
        coords.add(c1);
        coords.add(c2);
        coords.add(c3);

        handler.setInput(coords,true);

    }

    @Test
    void finishCard() {
    }

    @Test
    void continueCard() {
    }
}