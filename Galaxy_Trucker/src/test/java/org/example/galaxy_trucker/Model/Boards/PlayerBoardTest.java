package org.example.galaxy_trucker.Model.Boards;

import org.example.galaxy_trucker.Exceptions.InvalidInput;


import org.example.galaxy_trucker.Model.Boards.Actions.AddCrewAction;
import org.example.galaxy_trucker.Model.Boards.Actions.ComponentActionVisitor;
import org.example.galaxy_trucker.Model.Boards.Actions.GetEnginePower;
import org.example.galaxy_trucker.Model.Boards.Actions.UseEnergyAction;
import org.example.galaxy_trucker.Model.GAGen;
import org.example.galaxy_trucker.Model.IntegerPair;

import org.example.galaxy_trucker.Model.PlayerStates;
import org.example.galaxy_trucker.Model.Tiles.HotWaterHeater;
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
//        assertEquals(20, playerBoard.getExposedConnectors());
//        int[] shield = {1 ,2 ,1 ,0};
//        assertArrayEquals(shield,playerBoard.getShield());
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


        playerBoard.destroy(6,5);
        assertFalse(playerBoard.checkValidity());
        assertEquals(1, playerBoard.getDamage());
        HashMap<Integer, ArrayList<IntegerPair>> handleAttack = playerBoard.handleAttack(6,5);
        assertEquals(2, handleAttack.size());
        playerBoard.modifyPlayerBoard(handleAttack.get(1));
        assertTrue(playerBoard.checkValidity());
        assertEquals(8, playerBoard.getDamage());
        int[] shield = {0,1,1,0};
        assertArrayEquals(shield, playerBoard.getShield());



    }



    @Test
    @DisplayName("test ComponentActions")
    @Order(2)
    public void testComponentActions(){

        PlayerStates state = PlayerStates.PopulateHousingUnits;

        System.out.println(playerBoard.getHousingUnits().indexOf(playerBoard.getTile(6,6).getComponent()));


        playerBoard.performAction(playerBoard.getTile(6,6).getComponent(), new AddCrewAction(2,false,false, playerBoard), state);
        playerBoard.performAction(playerBoard.getTile(5,7).getComponent(), new AddCrewAction(0,false,true, playerBoard), state);
        assertEquals(2,playerBoard.getNumHumans());




        state = PlayerStates.GiveSpeed;

        GetEnginePower action = new GetEnginePower(playerBoard.getEnginePower());
        for (HotWaterHeater hw : playerBoard.getHotWaterHeaters() ) {
            playerBoard.performAction(hw,action,state);
        }
        assertEquals(3, action.getPower());


    }


}

