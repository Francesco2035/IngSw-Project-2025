package org.example.galaxy_trucker.Model.Cards;

import org.example.galaxy_trucker.Model.Boards.GameBoard;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.Connectors.UNIVERSAL;
import org.example.galaxy_trucker.Model.GAGen;
import org.example.galaxy_trucker.Model.Game;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.Tiles.ModularHousingUnit;
import org.example.galaxy_trucker.Model.Tiles.Tile;
import org.example.galaxy_trucker.TestSetupHelper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class WarzoneTest {




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
        Player p1 = new Player();
        p1.setId("fGr");
        Player p2 = new Player();
        p2.setId("God");
        Player p3 = new Player();
        p3.setId("Sgregno");

        TGame.NewPlayer(p1);
        TGame.NewPlayer(p2);
        TGame.NewPlayer(p3);
        Franci = GameBoard.getPlayers().get(0);
        Pietro = GameBoard.getPlayers().get(1);
        Passo = GameBoard.getPlayers().get(2);
        playerBoard1 = Pietro.getmyPlayerBoard();
        playerBoard2 = Franci.getmyPlayerBoard();
        playerBoard3 = TestSetupHelper.createInitializedBoard1();
        TestSetupHelper.HumansSetter1(playerBoard3);
        playerBoard3.checkValidity();
        Passo.setMyPlance(playerBoard3);


        ModularHousingUnit house1 = new ModularHousingUnit();
        ModularHousingUnit house2 = new ModularHousingUnit();


        IntegerPair cord = new IntegerPair(0, 0);

        Tile tile1 = new Tile(house1, UNIVERSAL.INSTANCE, UNIVERSAL.INSTANCE, UNIVERSAL.INSTANCE, UNIVERSAL.INSTANCE);
        Tile tile2 = new Tile(house2, UNIVERSAL.INSTANCE, UNIVERSAL.INSTANCE, UNIVERSAL.INSTANCE, UNIVERSAL.INSTANCE);

        playerBoard1.insertTile(tile1, 6, 7);
        playerBoard1.insertTile(tile2, 7, 7);
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
    void
    cardEffect() {
        int[] req = new int[2];
        req[0]=1;//cannoni
        req[1]=3;//movim
        req[2]=2;//umani

        int[] punishment= new int[2];
        punishment[0]=1;
        punishment[1]=2;
        punishment[2]=3;

        Warzone carta= new Warzone(1,5,GameBoard,req,punishment,1,2,3,null);
    }

    @Test
    void updateSates() {

    }
}