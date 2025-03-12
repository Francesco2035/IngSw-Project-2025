package org.example.galaxy_trucker;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class PlayerPlance {

    private Tile[][] Plance;
    private int[][] ValidPlance;
    private int damage;
    private int power;
    private ArrayList<Pair<Tile, Integer>> ExposedConnectors;
    // posso avere dei doppioni cosi per sapere quanti sono basta guardare la lunghezza senza accedere ai tasselli
    private ArrayList<Pair<Integer, Integer>> Humans;private ArrayList<Tile> energyTiles;
    //    private ArrayList<Pair<Integer, Integer>> Energy;
    //richiesta di Pietro per le sue carte
    private ArrayList<Pair<Integer, Integer>> RedCargo;
    private ArrayList<Pair<Integer, Integer>> YellowCargo;
    private ArrayList<Pair<Integer, Integer>> GreenCargo;
    private ArrayList<Pair<Integer, Integer>> BlueCargo;
    private ArrayList<Tile> Buffer;

    private ArrayList<IntegerPair> connectedPlance; ;


    HashMap<Connector, ArrayList<Connector>>  validConnection;


    //attributes

    public PlayerPlance(int lv) {
        this.damage = 0;
        this.power = 0;
        this.ExposedConnectors = new ArrayList<>();
        this.Humans = new ArrayList<>();
        this.energyTiles = new ArrayList<>();
//        this.Energy = new ArrayList<>();
        this.RedCargo = new ArrayList<>();
        this.YellowCargo = new ArrayList<>();
        this.GreenCargo = new ArrayList<>();
        this.BlueCargo = new ArrayList<>();
        this.Buffer = new ArrayList<>();
        this.ValidPlance = new int[10][10];
        this.validConnection = new HashMap<Connector, ArrayList<Connector>>();
        validConnection.put(Connector.UNIVERSAL, new ArrayList<>());
        validConnection.get(Connector.UNIVERSAL).addAll(List.of(Connector.UNIVERSAL, Connector.SINGLE, Connector.DOUBLE));
        validConnection.put(Connector.DOUBLE, new ArrayList<>());
        validConnection.get(Connector.DOUBLE).addAll(List.of(Connector.UNIVERSAL,  Connector.DOUBLE));
        validConnection.put(Connector.SINGLE, new ArrayList<>());
        validConnection.get(Connector.SINGLE).addAll(List.of(Connector.UNIVERSAL, Connector.SINGLE));
        validConnection.put(Connector.MOTOR, new ArrayList<>());
        validConnection.put(Connector.NONE, new ArrayList<>());


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
                    Plance[x][y] =  new Tile(new IntegerPair(x,y), new Void() ,Connector.NONE, Connector.NONE,Connector.NONE, Connector.NONE);
                }
                else {
                    Plance[x][y] = null;
                }

            }
        }
        this.Plance[6][6] = new Tile(new IntegerPair(6,6), new MainCockpitComp(0),Connector.UNIVERSAL, Connector.UNIVERSAL,Connector.UNIVERSAL, Connector.UNIVERSAL);
    }

    public Tile[][] getPlayerPlance(){
        return this.Plance;
    }

    public int[][] getValidPlance(){
        return this.ValidPlance;
    }

    //    public ArrayList<Pair<Integer, Integer>> getEnergy() {
//        return Energy;
//    }
    public ArrayList<Tile> getEnergyTiles() {
        return energyTiles;
    }


    public void setEnergyTiles(ArrayList<Tile> energyTiles) {
        this.energyTiles = energyTiles;
    }


    public void addEnergyTiles(Tile energyTile) {
        this.energyTiles.add(energyTile);
    }

    public void insertTile(Tile tile, int x, int y) {
        try {
            this.Plance[x][y] = tile;
            if (tile.getComponent().getClass() == BatteryComp.class) addEnergyTiles(tile);
            ValidPlance[x][y] = 1;
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }


    public Tile getTile(int x, int y) {
        return this.Plance[x][y];
    }

    public ArrayList<Pair<Integer, Integer>> getHumans(){ return this.Humans; }

    public int getPower() {
        return power;
    }

    public boolean checkConnection(Connector t1, Connector t2 ){
        if(validConnection.get(t1).contains(t2)){
            return true;
        }
        else {
            return false;
        }
    }

    public HashMap<Integer, ArrayList<IntegerPair>> handleAttack(int x, int y){

        ArrayList<IntegerPair> visitedPositions = new ArrayList<>();
        int i = 0;
        HashMap<Integer, ArrayList<IntegerPair>> shipSection = new HashMap<>();

        if (ValidPlance[x-1][y] == 1){

            findPaths(x, y-1, visitedPositions);
            i++;
            shipSection.put(i, visitedPositions);

        }

        if (ValidPlance[x][y-1] == 1 && !shipSection.get(i).contains (new IntegerPair(x,y-1))){
            visitedPositions.clear();
            findPaths(x, y-1, visitedPositions);
            i++;
            shipSection.put(i, visitedPositions);

        }

        if (ValidPlance[x+1][y] == 1 && !shipSection.get(i).contains (new IntegerPair(x+1,y))){
            visitedPositions.clear();
            findPaths(x+1, y, visitedPositions);
            i++;
            shipSection.put(i, visitedPositions);

        }

        if (ValidPlance[x][y + 1] == 1 && !shipSection.get(i).contains (new IntegerPair(x,y+1))){
            visitedPositions.clear();
            findPaths(x, y+1, visitedPositions);
            i++;
            shipSection.put(i, visitedPositions);

        }

        return shipSection;


    }



//ritorna i path non visistati
    public ArrayList<IntegerPair> PathNotVisited(ArrayList<IntegerPair> visited){

        ArrayList<IntegerPair> result = new ArrayList<>();
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {

                if (ValidPlance[x][y] == 1 && !visited.contains(new IntegerPair(x,y))){
                    result.add(new IntegerPair(x,y));

                }

            }


        }
        return result;

    }

    public boolean checkIllegal( ArrayList<IntegerPair> visited){

        int x;
        int y;

        for (IntegerPair pair : visited) {
            x = pair.getFirst();
            y = pair.getSecond();
            if (ValidPlance[x-1][y] == 1 && (Plance[x-1][y].getConnectors().get(2) == Connector.MOTOR || Plance[x-1][y].getConnectors().get(2) == Connector.CANNON)){
                return false;
            }
            if (ValidPlance[x+1][y] == 1 && (Plance[x+1][y].getConnectors().get(0) == Connector.MOTOR || Plance[x+1][y].getConnectors().get(0) == Connector.CANNON)){
                return false;
            }
            if (ValidPlance[x][y-1] == 1 && (Plance[x][y-1].getConnectors().get(1) == Connector.MOTOR || Plance[x][y - 1].getConnectors().get(1) == Connector.CANNON)){
                return false;
            }
            if (ValidPlance[x][y + 1] == 1 && (Plance[x][y+1].getConnectors().get(3) == Connector.MOTOR || Plance[x][y + 1].getConnectors().get(3) == Connector.CANNON)){
                return false;
            }

        }
        return true;

    }


    public boolean checkValidity(){

        int r = 6;
        int c = 6;

        ArrayList<IntegerPair> visitedPositions = new ArrayList<>();

        findPaths(r,c,visitedPositions);


        if (!PathNotVisited(visitedPositions).isEmpty()){
            return false;
        }

        else {
            return checkIllegal(visitedPositions);
        }

    }

    public void findPaths(int r, int c, ArrayList<IntegerPair> visited) {

        System.out.println(r + " " + c);
        if (visited.contains(new IntegerPair(r, c))||r < 0 || c < 0 || r > 9 || c > 9 || this.ValidPlance[r][c] == -1) {
            return;
        }
        visited.add(new IntegerPair(r, c));

        if (getTile(r, c -1) != null && checkConnection(getTile(r,c).getConnectors().get(0),getTile(r, c -1).getConnectors().get(2))) {
            findPaths(r, c - 1, visited);
        }

        if (getTile(r -1, c ) != null && checkConnection(getTile(r,c).getConnectors().get(1),getTile(r-1, c ).getConnectors().get(3))){
            findPaths(r -1,c ,visited);
        }

        if (getTile(r, c + 1) != null && checkConnection(getTile(r,c).getConnectors().get(2),getTile(r, c + 1).getConnectors().get(0))){
            findPaths(r,c + 1 ,visited);
        }

        if (getTile(r + 1, c) != null && checkConnection(getTile(r,c).getConnectors().get(1),getTile(r + 1, c ).getConnectors().get(3))){
            findPaths(r +1,c ,visited);
        }

    }



}
