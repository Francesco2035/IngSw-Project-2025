package org.example.galaxy_trucker;

import javafx.util.Pair;

import javax.lang.model.type.NullType;
import java.util.ArrayList;

public class PlayerPlance {

    private Tile[][] Plance;
    private int[][] ValidPlance;
    private int damage;
    private int power;
    private ArrayList<Pair<Tile, Integer>> ExposedConnectors;
    private ArrayList<Pair<Integer, Integer>> Umans;
    private ArrayList<Pair<Integer, Integer>> Energy;
    //richiesta di Pietro per le sue carte
    private ArrayList<Pair<Integer, Integer>> RedCargo;
    private ArrayList<Pair<Integer, Integer>> YellowCargo;
    private ArrayList<Pair<Integer, Integer>> GreenCargo;
    private ArrayList<Pair<Integer, Integer>> BlueCargo;
    private ArrayList<Tile> Buffer;


    //attributes

    public PlayerPlance(int lv, Tile main_cockpit) {
        this.damage = 0;
        this.power = 0;
        this.ExposedConnectors = new ArrayList<>();
        this.Umans = new ArrayList<>();
        this.Energy = new ArrayList<>();
        this.RedCargo = new ArrayList<>();
        this.YellowCargo = new ArrayList<>();
        this.GreenCargo = new ArrayList<>();
        this.BlueCargo = new ArrayList<>();
        this.Buffer = new ArrayList<>();
        this.ValidPlance = new int[10][10];
        if (lv == 1) {
            for (int x = 0; x < 10; x++) {
                for (int y = 0; y < 10; y++) {
                    if (x < 4 || y < 3 || (x == 4 && (y == 3 || y == 4 || y == 6 || y == 8 || y == 9)) ||(x == 5 && (y == 3 || y== 9)) || (x == 8 && y == 6) || x ==9) {
                        ValidPlance[x][y] = -1;
                    }
                    else if (x == 6 && y == 6) {
                        ValidPlance[x][y] = 1;
                    }
                    else {
                        ValidPlance[x][y] = 0;
                    }

                }
            }
        }
        else {
            for (int x = 0; x < 10; x++) {
                for (int y = 0; y < 10; y++) {
                    if(y <= 3 ||x == 9 || y == 9|| x <4 || ( x == 4 && (y != 6)) || (x== 5 &&(y <5 || y>7)) || (x==8 && y == 6))  {
                        ValidPlance[x][y] = -1;
                    }
                    else if (x == 6 && y == 6) {
                        ValidPlance[x][y] = 1;
                    }
                    else {
                        ValidPlance[x][y] = 0;
                    }
                }
            }

        }
        this.Plance = new Tile[10][10];
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                if (ValidPlance[x][y] == -1) {
                    Plance[x][y] =  null;
                }
                else {
                    Plance[x][y] = main_cockpit;
                }

            }
        }
    }

    public Tile[][] getPlayerPlance(){
        return this.Plance;
   }

    public int[][] getValidPlance(){
        return this.ValidPlance;
    }
}
