package org.example.galaxy_trucker.Model.Boards;

import org.example.galaxy_trucker.Model.GAGen;
import org.example.galaxy_trucker.Model.Tiles.Tile;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;


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





    }





    @Test
    public void testValidity(){


        boolean f = playerBoard.checkValidity();
        //assertEquals(true, f);
        System.out.println(playerBoard.getClassifiedTiles().size());

    }



}

