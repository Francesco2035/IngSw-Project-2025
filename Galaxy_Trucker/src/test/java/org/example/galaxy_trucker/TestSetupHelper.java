package org.example.galaxy_trucker;

import org.example.galaxy_trucker.Model.Boards.Actions.AddCrewAction;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.Connectors.DOUBLE;
import org.example.galaxy_trucker.Model.Connectors.SINGLE;
import org.example.galaxy_trucker.Model.GAGen;

import org.example.galaxy_trucker.Model.PlayerStatesss;
import org.example.galaxy_trucker.Model.Tiles.HousingUnit;
import org.example.galaxy_trucker.Model.Tiles.Tile;
import org.example.galaxy_trucker.Model.Tiles.SewerPipes;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

public class TestSetupHelper {

    static PlayerBoard playerBoard = new PlayerBoard(2);
    static PlayerBoard playerBoard2 = new PlayerBoard(2);
    static PlayerBoard playerBoard3 = new PlayerBoard(2);
    static PlayerBoard playerBoard4 = new PlayerBoard(2);
    static GAGen gag;

    static {
        try {
            gag = new GAGen();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static PlayerBoard createInitializedBoard1() {

        ArrayList<Tile> tiles = gag.getTilesDeck();
        Tile t1 = tiles.get(133); //"SINGLE", "DOUBLE", "SINGLE", "NONE addons, br factos
        Tile t2 = tiles.get(102); //none,cannon, single, universal , plasmadrill    ruota sx due volte factos
        Tile t3 = tiles.get(128); //doublem cannon, none, unviersal. plasmadrill  factos
        Tile t4 = tiles.get(149); //double,k single, double, single shield none, double, double, universal ruota a sx factos
        Tile specialStorage = tiles.get(57);
        Tile normalStorage  =tiles.get(24);
        Tile t8 = tiles.get(56);  // universal, universal, double, double. sewerpips factos
        Tile t9 = tiles.get(32); //SINGLE", "NONE", "NONE", "UNIVERSAL housingjfoaihj factos
        Tile t10 = tiles.get(33); //SINGLE", "SINGLE", "DOUBLE", "SINGLE moudsajdahoyusingunit ruota dx factos
        Tile t11 = tiles.get(73); //SINGLE", "DOUBLE", "NONE", "MOTOR" un motore factos
        Tile t12 = tiles.get(145);
        Tile powerCenter = tiles.get(12);
        Tile powerCenter2 = tiles.get(5);
        Tile plasmaDrill = tiles.get(130);
        Tile addonspurple = tiles.get(142);
        Tile modular1 = tiles.get(39);
        Tile sewerpipes = tiles.get(52);
        Tile modular2 = tiles.get(47);
        Tile modular3 = tiles.get(48);
        Tile hotWaterHeater = tiles.get(92);
        Tile shield = tiles.get(151);


        shield.RotateSx();


        addonspurple.RotateSx();
        plasmaDrill.RotateDx();




        playerBoard.insertTile(t1, 6,7);
        t2.RotateSx();
        t2.RotateSx();
        //playerBoard.insertTile(t2, 7,7);
        t8.RotateDx();
        playerBoard.insertTile(t8, 7,6);
        playerBoard.insertTile(t11, 6,5);
        playerBoard.insertTile(t9, 5,7);
        //playerBoard.insertTile(t3, 5,6);

        t4.RotateSx();
        playerBoard.insertTile(t4, 5,5);
        t10.RotateDx();
        playerBoard.insertTile(t10, 4,5);

        playerBoard.insertTile(t12, 6,8);

        specialStorage.RotateDx();
        specialStorage.RotateDx();
        playerBoard.insertTile(specialStorage, 7,8);

        playerBoard.insertTile(normalStorage, 7,9);
        playerBoard.insertTile(powerCenter, 6,9);
        playerBoard.insertTile(powerCenter2, 5,4);
        playerBoard.insertTile(plasmaDrill,8,9);
        playerBoard.insertTile(addonspurple,6,4);
        playerBoard.insertTile(modular1,7,4);
        playerBoard.insertTile(sewerpipes,7,3);
        playerBoard.insertTile(hotWaterHeater,8,3);
//        playerBoard.insertTile(modular2,8,4);
//        playerBoard.insertTile(modular3,8,5);
//        playerBoard.insertTile(shield,6,3);
        return playerBoard;

    }

    @Test
    public void print(){
        ArrayList<Tile> tiles = gag.getTilesDeck();
        for (Tile t :tiles){
            System.out.println(t.getComponent().getClass().getName());
        }
    }

    public static PlayerBoard createInitializedBoard2(){
        ArrayList<Tile> tiles = gag.getTilesDeck();
        Tile t1 = tiles.get(39); //House:   double,single,double,none
        Tile t2 = tiles.get(35); //House: singe,double,none,none
        Tile t3 = tiles.get(48);//house: uni,double,double
        Tile t4 = tiles.get(99); //Cannon: none,cannon,none,double
        Tile t5 = tiles.get(117);//Cannon: double, cannon,single,uni
        Tile t6 = tiles.get(137);//BrownAddon: uni,single,none,none
        Tile t7 = tiles.get(150);//Shield: uni,none,none,single
        Tile t8 = tiles.get(10);//battery uni,uni,none,none


        t1.RotateSx();
        t1.RotateSx();
        t2.RotateDx();
        t3.RotateDx();
        t5.RotateDx();
        t6.RotateDx();

        playerBoard2.insertTile(t1,5,6);
        playerBoard2.insertTile(t7,6,7);
        playerBoard2.insertTile(t2,7,7);
        playerBoard2.insertTile(t5,7,8);
        playerBoard2.insertTile(t6,6,5);
        playerBoard2.insertTile(t3,5,5);
        playerBoard2.insertTile(t4,4,5);
        playerBoard2.insertTile(t8,8,8);
        return playerBoard2;



    }

    public static PlayerBoard createInitializedBoard3(){
        return playerBoard3;
    }

    public static PlayerBoard createInitializedBoard4(){
        SINGLE SS = SINGLE.INSTANCE;
        DOUBLE DD = DOUBLE.INSTANCE;
        Tile single = new Tile(new SewerPipes(),SS,SS,SS,SS);
        Tile dd = new Tile(new SewerPipes(),DD,DD,DD,DD);
        playerBoard4.insertTile(single,6,7);
        playerBoard4.insertTile(dd,7,7);
        playerBoard4.insertTile(dd,7,6);

        return playerBoard4;
    }

    public static void HumansSetter1(PlayerBoard playerBoard){
//        ArrayList<HousingUnit> HousingCoords=new ArrayList<>();
//        HousingCoords.addAll(playerBoard.getHousingUnits());
//        for(HousingUnit housingUnit : HousingCoords){
//           playerBoard.performAction(housingUnit,new AddCrewAction(2,false,false, playerBoard), PlayerStatesss.PopulateHousingUnits);
//        }
    }


    public static void HumansSetter2(PlayerBoard playerBoard){ ArrayList<HousingUnit> HousingCoords=new ArrayList<>();
        HousingCoords.addAll(playerBoard.getHousingUnits());
        for(HousingUnit housingUnit : HousingCoords){
            if(housingUnit.getX()==5 && housingUnit.getY()==5){
                housingUnit.setBrownAlien(true);
            }
            else{
                housingUnit.setNumHumans(2);
            }
        }

    }


}
