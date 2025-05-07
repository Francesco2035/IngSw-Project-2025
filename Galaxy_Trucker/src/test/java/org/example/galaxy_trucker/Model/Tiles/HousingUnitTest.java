package org.example.galaxy_trucker.Model.Tiles;

import org.example.galaxy_trucker.Model.Boards.Actions.AddCrewAction;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.AddCrewState;
import org.example.galaxy_trucker.TestSetupHelper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HousingUnitTest {


    @Test
    public void testHousingUnit() {
        Player player = new Player();
        player.setId("Poggi");
        PlayerBoard playerBoard = TestSetupHelper.createInitializedBoard3();
        player.setMyPlance(playerBoard);
        //printNearby(playerBoard);
        playerBoard.checkValidity();
        //printNearby(playerBoard);
        //printUnits(playerBoard);
        player.setState(new AddCrewState());
        AddCrewAction addAction = new AddCrewAction(0,true,false,playerBoard);
        playerBoard.performAction(playerBoard.getTile(7 ,8).getComponent(),addAction,player.getPlayerState());
        HousingUnit unit = (HousingUnit)playerBoard.getTile(7 ,8).getComponent();
        assertTrue(unit.isPurpleAlien() && unit.isNearPurpleAddon() && !unit.isNearBrownAddon() && !unit.isBrownAlien() && unit.getNumHumans() == 0);
        addAction = new AddCrewAction(2,false,false,playerBoard);
        unit = (HousingUnit)playerBoard.getTile(6 ,7).getComponent();
        assertTrue(!unit.isPurpleAlien() && !unit.isBrownAlien());
        playerBoard.performAction(playerBoard.getTile(6 ,7).getComponent(),addAction,player.getPlayerState());
        playerBoard.performAction(playerBoard.getTile(5 ,7).getComponent(),addAction,player.getPlayerState());
        playerBoard.performAction(playerBoard.getTile(4 ,7).getComponent(),addAction,player.getPlayerState());
        playerBoard.performAction(playerBoard.getTile(6 ,6).getComponent(),addAction,player.getPlayerState());
        playerBoard.performAction(playerBoard.getTile(5 ,6).getComponent(),addAction,player.getPlayerState());
        playerBoard.performAction(playerBoard.getTile(6 ,4).getComponent(),addAction,player.getPlayerState());
        addAction = new AddCrewAction(0,false,true,playerBoard);
        playerBoard.performAction(playerBoard.getTile(7 ,5).getComponent(),addAction,player.getPlayerState());
        //printNearby(playerBoard);
        //printUnits(playerBoard);
        PlayerBoard pb = playerBoard.clone();
        printNearby(playerBoard);
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        printNearby(pb);



        //printUnits(playerBoard);
        //printUnits(pb);




//        playerBoard.destroy(6 ,7);
//        printUnits(playerBoard);
//        playerBoard.destroy(8,5);
//        printUnits(playerBoard);
//        playerBoard.destroy(4,7);
//        printUnits(playerBoard);
//        playerBoard.destroy(5,6);
//        printUnits(playerBoard);




    }


    public void printNearby(PlayerBoard pb){
        for (HousingUnit unit : pb.getHousingUnits()) {
            System.out.println("################################ "+unit.getX() + " " + unit.getY());
            for (HousingUnit unit1: unit.getNearbyHousingUnits()){
                System.out.println(unit1.getX() + " " + unit1.getY());
            }

        }
    }


    public void printUnits(PlayerBoard playerBoard){
        System.out.println("PRINTING UNITS");
        for (HousingUnit unit: playerBoard.getHousingUnits()){
            System.out.println("Unit "+ unit.getX() + " " + unit.getY() + " " + unit.getNumHumans() +  " " + unit.isBrownAlien() + " " + unit.isPurpleAlien() );
        }
        System.out.println("PRINTING CONNECTEDUNITS");
        for (HousingUnit ConUnit: playerBoard.getConnectedHousingUnits()){
            System.out.println("ConUnit " + ConUnit.getX() + " " + ConUnit.getY());
        }
    }
}