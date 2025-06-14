package org.example.galaxy_trucker.Model.Boards;

import org.example.galaxy_trucker.Exceptions.InvalidInput;
import org.example.galaxy_trucker.Model.Boards.Actions.*;
import org.example.galaxy_trucker.Model.GAGen;
import org.example.galaxy_trucker.Model.PlayerStates.AddCrewState;
import org.example.galaxy_trucker.Model.PlayerStates.GiveSpeed;
import org.example.galaxy_trucker.Model.PlayerStates.PlayerState;
import org.example.galaxy_trucker.Model.Tiles.HotWaterHeater;
import org.example.galaxy_trucker.TestSetupHelper;
import org.junit.jupiter.api.*;
import java.io.IOException;

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

//    @BeforeAll
//    public static void setup(){
//
//
//    }

    @Test

    @DisplayName("test validity")
    @Order(1)
    public void testValidity(){

        playerBoard = TestSetupHelper.createInitializedBoard1();
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
                () -> playerBoard.insertTile(null, -1,6, false),
                "A NullPointerException should be thrown.");

        assertThrows(
                InvalidInput.class,
                () -> playerBoard.insertTile(gag.getTilesDeck().get(10) , 2,6, false),
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

//        playerBoard.destroy(6,5);
//        assertFalse(playerBoard.checkValidity());
//        assertEquals(1, playerBoard.getDamage());
//        HashMap<Integer, ArrayList<IntegerPair>> handleAttack = playerBoard.handleAttack(6,5);
//        assertEquals(2, handleAttack.size());
//        playerBoard.modifyPlayerBoard(handleAttack.get(1));
//        assertTrue(playerBoard.checkValidity());
//        assertEquals(8, playerBoard.getDamage());
//        int[] shield = {0,1,1,0};
//        assertArrayEquals(shield, playerBoard.getShield());
//        playerBoard.destroy(6,9);

    }


    @Test
    @DisplayName("test ComponentActions")
    @Order(2)
    public void testComponentActions(){

        PlayerState state = new AddCrewState();



        try {
            playerBoard.performAction(playerBoard.getTile(6,6).getComponent(), new AddCrewAction(2,false,false, playerBoard), state);
            playerBoard.performAction(playerBoard.getTile(5, 7).getComponent(), new AddCrewAction(0, false, true, playerBoard), state);
        } catch (Exception e){
            e.printStackTrace();
        }
        assertEquals(4,playerBoard.getNumHumans());

        state = new GiveSpeed();

        GetEnginePower action = new GetEnginePower(playerBoard.getEnginePower());
        for (HotWaterHeater hw : playerBoard.getHotWaterHeaters() ) {
            playerBoard.performAction(hw,action,state);
        }
        assertEquals(5, action.getPower());


//        System.out.println("Testing GoodsActions");
//        Goods red = new RED();
//        Goods blue = new BLUE();
//        Goods yellow = new YELLOW();
//        state = PlayerStatesss.AddCargo;
//        AddGoodAction action3 = new AddGoodAction(red, playerBoard,7,8);
//        playerBoard.performAction(playerBoard.getTile(7,8).getComponent(), action3, state);
//        action3 = new AddGoodAction(blue, playerBoard,7,9);
//        playerBoard.performAction(playerBoard.getTile(7,9).getComponent(), action3, state);
//        playerBoard.performAction(playerBoard.getTile(7,9).getComponent(), action3, state);
//        assertEquals(2, playerBoard.getStoredGoods().size());
//        GetGoodAction getaction = new GetGoodAction(0,playerBoard,7,8);
//        playerBoard.performAction(playerBoard.getTile(7,8).getComponent(), getaction, PlayerStatesss.RemoveCargo);
//        assertEquals(1,playerBoard.getStoredGoods().size());
//        assertThrows(InvalidInput.class,
//                () -> playerBoard.performAction(playerBoard.getTile(7,8).getComponent(),
//                        new GetGoodAction(0,playerBoard,7,8), PlayerStatesss.RemoveCargo));
//        assertThrows( InvalidInput.class,
//                () -> playerBoard.performAction(playerBoard.getTile(7,9).getComponent(),
//                        new AddGoodAction(yellow, playerBoard,7,9), PlayerStatesss.AddCargo));
//
//
//        System.out.println("Testing EnergyActions");
//        UseEnergyAction energyAction = new UseEnergyAction(playerBoard);
//        state = PlayerStatesss.UseEnergy;
//        playerBoard.performAction(playerBoard.getTile(5,4).getComponent(), energyAction, state);
//        playerBoard.performAction(playerBoard.getTile(5,4).getComponent(), energyAction, state);
//        playerBoard.performAction(playerBoard.getTile(6,9).getComponent(), energyAction, state);
//        playerBoard.performAction(playerBoard.getTile(6,9).getComponent(), energyAction, state);
//        playerBoard.performAction(playerBoard.getTile(6,9).getComponent(), energyAction, state);
//        assertEquals(0,playerBoard.getEnergy());
//        assertThrows(InvalidInput.class,
//                ()-> playerBoard.performAction
//                        (playerBoard.getTile(6,9).getComponent(),new UseEnergyAction(playerBoard), PlayerStatesss.UseEnergy));
//        assertThrows(IllegalStateException.class,
//                ()-> playerBoard.performAction
//                        (playerBoard.getTile(6,9).getComponent(),new UseEnergyAction(playerBoard), PlayerStatesss.Accepting));


    }

    @Test
    @DisplayName("Test cloning")
    @Order(4)
    public void cloneTest(){
        PlayerBoard clone = playerBoard.clone();

        // Check: primitive & values
        assertEquals(playerBoard.isBroken(), clone.isBroken());
        assertEquals(playerBoard.getTotalValue(), clone.getTotalValue());
        assertEquals(playerBoard.getDamage(), clone.getDamage());
        assertEquals(playerBoard.getEnginePower(), clone.getEnginePower());
        assertEquals(playerBoard.getPlasmaDrillsPower(), clone.getPlasmaDrillsPower());
        assertEquals(playerBoard.getEnergy(), clone.getEnergy());
        assertEquals(playerBoard.isPurpleAlien(), clone.isPurpleAlien());
        assertEquals(playerBoard.isBrownAlien(), clone.isBrownAlien());
        assertEquals(playerBoard.isValid(), clone.isValid());
        assertEquals(playerBoard.getLv(), clone.getLv());

        // Check: shield array (contents equal, reference different)
        assertArrayEquals(playerBoard.getShield(), clone.getShield());
        assertNotSame(playerBoard.getShield(), clone.getShield());

        // Check: goods list
        assertEquals(playerBoard.getBufferGoods(), clone.getBufferGoods());
        assertNotSame(playerBoard.getBufferGoods(), clone.getBufferGoods());

        // Check: Tile matrix
        assertEquals(playerBoard.getPlayerBoard()[0][0].getComponent().getClass(), clone.getPlayerBoard()[0][0].getComponent().getClass());
        assertNotSame(playerBoard.getPlayerBoard()[0][0], clone.getPlayerBoard()[0][0]);

        // Check: ValidPlayerBoard
        assertArrayEquals(playerBoard.getValidPlayerBoard()[0], clone.getValidPlayerBoard()[0]);
        assertNotSame(playerBoard.getValidPlayerBoard(), clone.getValidPlayerBoard());

        // Mutate clone and verify playerBoard is unchanged
        clone.getShield()[0] = 99;
        assertNotEquals(playerBoard.getShield()[0], clone.getShield()[0]);

    }


}

