package org.example.galaxy_trucker.Model.Boards;

import org.example.galaxy_trucker.Exceptions.InvalidInput;
import org.example.galaxy_trucker.Model.GAGen;
import org.example.galaxy_trucker.Model.GetterHandler.EngineGetter;
import org.example.galaxy_trucker.Model.GetterHandler.PlasmaDrillsGetter;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.SetterHandler.HousingUnitSetter;
import org.example.galaxy_trucker.Model.Tiles.Tile;
import org.example.galaxy_trucker.Model.Tiles.hotWaterHeater;
import org.example.galaxy_trucker.Model.Tiles.plasmaDrill;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PlayerBoardTest {
    static PlayerBoard playerBoard = new PlayerBoard(2);
    static GAGen gag;

    static {
        try {
            gag = new GAGen();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeAll
    public static void setup(){
        ArrayList<Tile> tiles = gag.getTilesDeck();
        Tile t1 = tiles.get(134); //"SINGLE", "DOUBLE", "SINGLE", "NONE addons, br factos
        Tile t2 = tiles.get(102); //none,cannon, single, universal , plasmadrill    ruota sx due volte factos
        Tile t3 = tiles.get(128); //doublem cannon, none, unviersal. plasmadrill  factos
        Tile t4 = tiles.get(150); //universal,k none, double, double shield none, double, double, universal ruota a sx factos

        Tile t8 = tiles.get(56);  // universal, universal, double, double. sewerpips factos
        Tile t9 = tiles.get(32); //SINGLE", "NONE", "NONE", "UNIVERSAL housingjfoaihj factos
        Tile t10 = tiles.get(33); //SINGLE", "SINGLE", "DOUBLE", "SINGLE moudsajdahoyusingunit ruota dx factos
        Tile t11 = tiles.get(73); //SINGLE", "DOUBLE", "NONE", "MOTOR" un motore factos
        Tile t12 = tiles.get(146);

        t1.getComponent().initType();
        playerBoard.insertTile(t1, 6,7);
        t2.RotateSx();
        t2.RotateSx();
        playerBoard.insertTile(t2, 7,7);
        t8.RotateDx();
        playerBoard.insertTile(t8, 7,6);
        playerBoard.insertTile(t11, 6,5);
        playerBoard.insertTile(t9, 5,7);
        playerBoard.insertTile(t3, 5,6);
        t4.getComponent().initType();
        t4.RotateSx();
        playerBoard.insertTile(t4, 5,5);
        t10.RotateDx();
        playerBoard.insertTile(t10, 4,5);
        t12.getComponent().initType();
        playerBoard.insertTile(t12, 6,8);

    }

    @Test
    @Order(1)
    @DisplayName("test validity")
    public void testValidity(){

        boolean f = playerBoard.checkValidity();
        assertTrue(f);
    }

    @Test
    @DisplayName("Testing Exception")
    @Disabled
    void getBoardException() {

        System.out.println("Testing InsertTile Exceptions");
        assertThrows(
                NullPointerException.class,
                () -> playerBoard.insertTile(null, -1,6),
                "A NullPointerException should be thrown.");


        assertThrows(
                InvalidInput.class,
                () -> playerBoard.insertTile(gag.getTilesDeck().get(10) , 2,6),
                "An InvalidInput should be thrown.");

        System.out.println("Testing getTile Exception");
        assertThrows(
                InvalidInput.class,
                () -> playerBoard.getTile(7,4),
                "An InvalidInput should be thrown.");


    }


    @Test
    @DisplayName("test destruction")
    @Order(3)
    public void testDestruction(){

        assertTrue(playerBoard.getClassifiedTiles().containsKey(hotWaterHeater.class));
        playerBoard.destroy(6,5);
        assertFalse(playerBoard.checkValidity());
        assertEquals(1, playerBoard.getDamage());
        HashMap<Integer, ArrayList<IntegerPair>> handleAttack = playerBoard.handleAttack(6,5);
        assertEquals(2, handleAttack.size());
        playerBoard.modifyPlayerBoard(handleAttack.get(1));
        assertTrue(playerBoard.checkValidity());
        assertEquals(3, playerBoard.getDamage());
        assertFalse(playerBoard.getClassifiedTiles().containsKey(hotWaterHeater.class));
        int[] shield = {0,1,1,0};
        assertArrayEquals(shield, playerBoard.getShield());



    }



    @Test
    @DisplayName("test Getters")
    @Order(2)
    public void testGetters(){
        ArrayList<IntegerPair> choise = new ArrayList<>();
        choise.add(new IntegerPair(6,5));
        playerBoard.setGetter(new EngineGetter(playerBoard,choise));
        assertEquals(1, playerBoard.getGetter().get());
        choise.clear();
        choise.add(new IntegerPair(5,6));
        choise.add(new IntegerPair(7,7));

        playerBoard.setGetter(new PlasmaDrillsGetter(playerBoard,choise));
        assertEquals(1.5, playerBoard.getGetter().get());
        playerBoard.setSetter(new HousingUnitSetter(playerBoard, new IntegerPair(5,7), 0, false, true));
        playerBoard.getSetter().set();
        assertTrue(playerBoard.getTile(5,7).getComponent().isBrownAlien());
    }


}

