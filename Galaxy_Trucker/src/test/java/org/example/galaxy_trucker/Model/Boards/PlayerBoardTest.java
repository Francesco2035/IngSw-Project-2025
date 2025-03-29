package org.example.galaxy_trucker.Model.Boards;

import org.example.galaxy_trucker.Exceptions.InvalidInput;
import org.example.galaxy_trucker.Model.GAGen;
import org.example.galaxy_trucker.Model.GetterHandler.EngineGetter;
import org.example.galaxy_trucker.Model.GetterHandler.HousingUnitGetter;
import org.example.galaxy_trucker.Model.GetterHandler.PlasmaDrillsGetter;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.SetterHandler.HousingUnitSetter;
import org.example.galaxy_trucker.Model.Tiles.Tile;
import org.example.galaxy_trucker.Model.Tiles.hotWaterHeater;
import org.example.galaxy_trucker.Model.Tiles.plasmaDrill;
import org.example.galaxy_trucker.TestSetupHelper;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PlayerBoardTest {
    static PlayerBoard playerBoard = new PlayerBoard(2);
    static PlayerBoard playerBoard2 = new PlayerBoard(2);
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

        playerBoard = TestSetupHelper.createInitializedBoard1();

    }

    @Test

    @DisplayName("test validity")
    @Order(1)
    public void testValidity(){

        boolean f = playerBoard.checkValidity();
        assertTrue(f);
        assertEquals(17, playerBoard.getExposedConnectors());
        int[] shield = {2 ,3 ,1 ,0};
        assertArrayEquals(shield,playerBoard.getShield());
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
    @Disabled
    @Order(4)
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
    @DisplayName("test HousingUnitGetters/Setters")
    @Order(2)
    public void testHousingUnitGettersSetters(){

        playerBoard.setSetter(new HousingUnitSetter(playerBoard, new IntegerPair(5,7), 0, false, true));
        playerBoard.getSetter().set();
        System.out.println("Testing HousingUnitSetter(0,false,true)");
        assertTrue(playerBoard.getTile(5,7).getComponent().isBrownAlien());
        playerBoard.setSetter(new HousingUnitSetter(playerBoard, new IntegerPair(5,7), 0, false, true));
        assertThrows(
                InvalidInput.class,
                () -> playerBoard.getSetter().set(),
                "An InvalidInput should be thrown.");
        System.out.println("Testing invalideInput Exception");
        playerBoard.setSetter(new HousingUnitSetter(playerBoard, new IntegerPair(5,7), 0, true, false));
        assertThrows(
                InvalidInput.class,
                () -> playerBoard.getSetter().set(),
                "An InvalidInput should be thrown.");
        System.out.println("Testing invalideInput Exception (adding human when is already present an alien)");
        playerBoard.setSetter(new HousingUnitSetter(playerBoard, new IntegerPair(5,7), 1, false, false));
        assertThrows(
                InvalidInput.class,
                () -> playerBoard.getSetter().set(),
                "An InvalidInput should be thrown.");
        playerBoard.setGetter(new HousingUnitGetter(playerBoard, new IntegerPair(5,7), 0, false, false));
        playerBoard.getGetter().get();
        assertFalse(playerBoard.getTile(5,7).getComponent().isBrownAlien());
        System.out.println("Testing invalideInput Exception (no near addons)");
        playerBoard.setSetter(new HousingUnitSetter(playerBoard, new IntegerPair(5,7), 0, true, false));
        assertThrows(
                InvalidInput.class,
                () -> playerBoard.getSetter().set(),
                "An InvalidInput should be thrown.");


        System.out.println("Populate HousingUnits...");
        playerBoard.setSetter(new HousingUnitSetter(playerBoard, new IntegerPair(5,7), 0, false, true));
        playerBoard.getSetter().set();
        playerBoard.setSetter(new HousingUnitSetter(playerBoard, new IntegerPair(6,6), 2, false, false));
        playerBoard.getSetter().set();
        playerBoard.setSetter(new HousingUnitSetter(playerBoard, new IntegerPair(4,5), 2, false, false));
        playerBoard.getSetter().set();
        System.out.println("Testing populateHousingUnits");
        assertTrue(!playerBoard.getTile(6,6).getComponent().isBrownAlien() &&
                !playerBoard.getTile(6,6).getComponent().isPurpleAlien() &&
                playerBoard.getTile(6,6).getComponent().getAbility() == 2);
        assertTrue(!playerBoard.getTile(4,5).getComponent().isBrownAlien() &&
                !playerBoard.getTile(4,5).getComponent().isPurpleAlien() &&
                playerBoard.getTile(4,5).getComponent().getAbility() == 2);
        assertTrue(playerBoard.getTile(5,7).getComponent().isBrownAlien() &&
                !playerBoard.getTile(5,7).getComponent().isPurpleAlien() &&
                playerBoard.getTile(5,7).getComponent().getAbility() == 0);

    }

    @Test
    @Order(3)
    public void testStorageCompartmentGettersSetters(){

    }

}

