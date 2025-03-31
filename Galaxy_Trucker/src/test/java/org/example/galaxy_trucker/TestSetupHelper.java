package org.example.galaxy_trucker;

import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.Connectors.DOUBLE;
import org.example.galaxy_trucker.Model.Connectors.SINGLE;
import org.example.galaxy_trucker.Model.GAGen;
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
        for (Tile t :tiles){
            System.out.println(t.getComponent().getClass().getName());
        }
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

        shield.getComponent().initType();
        shield.RotateSx();

        addonspurple.getComponent().initType();
        addonspurple.RotateSx();
        plasmaDrill.RotateDx();
        powerCenter.getComponent().initType();
        powerCenter2.getComponent().initType();

        t1.getComponent().initType();
        playerBoard.insertTile(t1, 6,7);
        t2.RotateSx();
        t2.RotateSx();
        //playerBoard.insertTile(t2, 7,7);
        t8.RotateDx();
        playerBoard.insertTile(t8, 7,6);
        playerBoard.insertTile(t11, 6,5);
        playerBoard.insertTile(t9, 5,7);
        //playerBoard.insertTile(t3, 5,6);
        t4.getComponent().initType();
        t4.RotateSx();
        playerBoard.insertTile(t4, 5,5);
        t10.RotateDx();
        playerBoard.insertTile(t10, 4,5);
        t12.getComponent().initType();
        playerBoard.insertTile(t12, 6,8);
        specialStorage.getComponent().initType();
        specialStorage.RotateDx();
        specialStorage.RotateDx();
        playerBoard.insertTile(specialStorage, 7,8);
        normalStorage.getComponent().initType();
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
    public void stampa(){
        ArrayList<Tile> tiles = gag.getTilesDeck();
        for (Tile t :tiles){
            System.out.println(t.getComponent().getClass().getName());
        }
    }

    public static PlayerBoard createInitializedBoard2(){
        return playerBoard2;
    }

    public static PlayerBoard createInitializedBoard3(){
        return playerBoard3;
    }

    public static PlayerBoard createInitializedBoard4(){
        SINGLE SS = new SINGLE();
        DOUBLE DD = new DOUBLE();
        Tile single = new Tile(new SewerPipes(),SS,SS,SS,SS);
        Tile dd = new Tile(new SewerPipes(),DD,DD,DD,DD);
        playerBoard4.insertTile(single,6,7);
        playerBoard4.insertTile(dd,7,7);
        playerBoard4.insertTile(dd,7,6);

        return playerBoard4;
    }

}
