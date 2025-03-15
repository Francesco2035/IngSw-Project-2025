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
    private int exposedConnectors;


    //buffer del cargo


    private ArrayList<IntegerPair> Humans;
    private ArrayList<IntegerPair> energyTiles;
    private ArrayList<IntegerPair> Cargo;
    private ArrayList<IntegerPair> plasmaDrills;

    private ArrayList<Tile> Buffer;

    private ArrayList<IntegerPair> connectedPlance; ;


    HashMap<Connector, ArrayList<Connector>>  validConnection;


    //attributes



    public PlayerPlance(int lv) {
        this.damage = 0;

        this.exposedConnectors = 0;
        this.Humans = new ArrayList<>();
        this.energyTiles = new ArrayList<>();
        this.energyTiles = new ArrayList<>();

        this.Cargo = new ArrayList<>();
        this.Buffer = new ArrayList<>();
        this.plasmaDrills = new ArrayList<>();
        this.ValidPlance = new int[10][10];
        this.validConnection = new HashMap<Connector, ArrayList<Connector>>();
        validConnection.put(Connector.UNIVERSAL, new ArrayList<>());
        validConnection.get(Connector.UNIVERSAL).addAll(List.of(Connector.UNIVERSAL, Connector.SINGLE, Connector.DOUBLE));
        validConnection.put(Connector.DOUBLE, new ArrayList<>());
        validConnection.get(Connector.DOUBLE).addAll(List.of(Connector.UNIVERSAL,  Connector.DOUBLE));
        validConnection.put(Connector.SINGLE, new ArrayList<>());
        validConnection.get(Connector.SINGLE).addAll(List.of(Connector.UNIVERSAL, Connector.SINGLE));
        validConnection.put(Connector.MOTOR, new ArrayList<>());
        validConnection.put(Connector.CANNON, new ArrayList<>());
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

    public void insertBuffer(Tile t){
        Buffer.add(t);
    }

    public ArrayList<Tile> getBuffer(){
        return Buffer;
    }

    public int getExposedConnectors(){
        return exposedConnectors;
    }

    public ArrayList<IntegerPair> getHumans(){
        return this.Humans;
    }




    public Tile[][] getPlayerPlance(){
        return this.Plance;
    }

    public int[][] getValidPlance(){
        return this.ValidPlance;
    }

    public ArrayList<IntegerPair> getEnergyTiles() {
        return energyTiles;
    }

    public Tile getTile(int x, int y) {
        return this.Plance[x][y];
    }

    public void insertTile(Tile tile, int x, int y) {
        try {
            this.Plance[x][y] = tile;
            ValidPlance[x][y] = 1;

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public boolean checkConnection(Connector t1, Connector t2 ){

        if (validConnection.get(t1).isEmpty()){
            return false;
        }
        if(validConnection.get(t1).contains(t2)){
            return true;
        }
        else {
            return false;
        }
    }


    //ritorna i path non visitati
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


    /**
     * Method checkIllegal checks if there is at least one illegal hotWaterHeater or plasmaDrill.
     *
     * @param visited of type ArrayList<IntegerPair> - player board saved as if it were a path.
     * @return boolean true if everything is legal, false otherwise.
     */
    public boolean checkIllegal( ArrayList<IntegerPair> visited){
    //System.out.println("controllo illegalità");
    int x;
    int y;

    for (IntegerPair pair : visited) {
        x = pair.getFirst();
        y = pair.getSecond();

        if (ValidPlance[x][y] == 1 && (Plance[x][y].getComponent().getClass() == plasmaDrill.class || Plance[x][y].getComponent().getClass() == hotWaterHeater.class)) {
            //System.out.println(x + " " + y);

                if (ValidPlance[x][y] == 1 && (Plance[x][y].getConnectors().get(0) == Connector.MOTOR  || Plance[x][y].getConnectors().get(1) == Connector.MOTOR) || Plance[x][y].getConnectors().get(2) == Connector.MOTOR){
                    //System.out.println("Motore illegale");
                    return false;
                }

                if(ValidPlance[x][y-1] == 1 && Plance[x][y].getConnectors().get(0) == Connector.CANNON ) {
                    //System.out.println("illegale da dx");
                    return false;
                }

                if(ValidPlance[x-1][y] == 1 && Plance[x][y].getConnectors().get(1) == Connector.CANNON ) {
                    //System.out.println("illegale dal basso");
                    return false;
                }

                if(ValidPlance[x][y+1] == 1 && Plance[x][y].getConnectors().get(2) == Connector.CANNON ) {
                    //System.out.println("illegale da sx");
                    return false;
                }

                if(ValidPlance[x+1][y] == 1 && (Plance[x][y].getConnectors().get(3) == Connector.CANNON || Plance[x][y].getConnectors().get(3) == Connector.MOTOR)) {
                    //System.out.println("illegale dall'alto");
                    return false;
                }

            }
        }
        return true;

    }

    /**
     * Method checkValidity check if the player board is valid according to the rules of the game

     */
    public boolean checkValidity(){

        int r = 6;
        int c = 6;

        ArrayList<IntegerPair> visitedPositions = new ArrayList<>();

        findPaths(r,c,visitedPositions);


        if (!PathNotVisited(visitedPositions).isEmpty()){
            return false;
        }

        else {
            if (checkIllegal(visitedPositions)){
                updateAttributes(r,c);
                return true;
            }
            else{
                return false;
            }
        }

    }

    //trova tutti i path che sono percorribili
    public void findPaths(int r, int c, ArrayList<IntegerPair> visited) {


        if (visited.contains(new IntegerPair(r, c))||r < 0 || c < 0 || r > 9 || c > 9 || this.ValidPlance[r][c] == -1) {
            return;
        }
        visited.add(new IntegerPair(r, c));
        System.out.println(r + " " + c);

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


    //elimina il tassello dell'attacco
    public void destroy(int x, int y){
        IntegerPair pos = new IntegerPair(x, y);
        Humans.remove(pos);
        energyTiles.remove(pos);
        plasmaDrills.remove(pos);
        Cargo.remove(pos);

        Plance[x][y] = new Tile(new IntegerPair(x,y), new spaceVoid(),Connector.NONE, Connector.NONE, Connector.NONE, Connector.NONE);
        ValidPlance[x][y] = 0;
    }

    //scelta del troncone
    public ArrayList<IntegerPair> choosePlance(HashMap<Integer, ArrayList<IntegerPair>> shipSection , int i){

        return shipSection.get(i);

    }


    //modifica la plance post attacco
    public void modifyPlance(ArrayList<IntegerPair> newPlance){
        for (int x = 0; x <10; x++ ){
            for(int y = 0; y <10; y++){
                if (ValidPlance[x][y] == 1){
                    if(!newPlance.contains(new IntegerPair(x,y))){
                        Plance[x][y] = new Tile(new IntegerPair(x,y),new spaceVoid(),Connector.NONE, Connector.NONE, Connector.NONE, Connector.NONE);
                        ValidPlance[x][y] = 0;
                    }
                }
            }
        }
        updateAttributes(newPlance.getFirst().getFirst(),newPlance.getFirst().getSecond());

    }


    public void updateAttributes(int x, int y){
        this.exposedConnectors = 0;
        this.Humans.clear();
        this.energyTiles.clear();
        this.Cargo.clear();
        this.plasmaDrills.clear();
        ArrayList<IntegerPair> visitedPositions = new ArrayList<>();
        updateBoardAttributes(x,y, visitedPositions);

    }

    public void updateBoardAttributes(int r, int c,ArrayList<IntegerPair> visited){
        if (visited.contains(new IntegerPair(r, c))||r < 0 || c < 0 || r > 9 || c > 9 || this.ValidPlance[r][c] == -1) { //!= 1
            return;
        }

        if (Plance[r][c].getComponent().getClass() == BatteryComp.class){
            energyTiles.add(new IntegerPair(r,c));
        }

        if (Plance[r][c].getComponent().getClass() == plasmaDrill.class){
            plasmaDrills.add(new IntegerPair(r, c));
        }

        if (Plance[r][c].getComponent().getClass() == modularHousingUnit.class || Plance[r][c].getComponent().getClass() == MainCockpitComp.class){
            Humans.add(new IntegerPair(r,c));
        }

        visited.add(new IntegerPair(r, c));

        System.out.println(r + " " + c);


        if (getTile(r, c -1) != null && checkConnection(getTile(r,c).getConnectors().get(0),getTile(r, c -1).getConnectors().get(2))) {
            findPaths(r, c - 1, visited);
        }
        else if (getTile(r,c).getConnectors().get(0) == Connector.SINGLE || getTile(r,c).getConnectors().get(0) == Connector.UNIVERSAL || getTile(r,c).getConnectors().get(0) == Connector.DOUBLE) {
            exposedConnectors++;
        }

        if (getTile(r -1, c ) != null && checkConnection(getTile(r,c).getConnectors().get(1),getTile(r-1, c ).getConnectors().get(3))){
            findPaths(r -1,c ,visited);
        }
        else if (getTile(r,c).getConnectors().get(1) == Connector.SINGLE || getTile(r,c).getConnectors().get(1) == Connector.UNIVERSAL || getTile(r,c).getConnectors().get(1) == Connector.DOUBLE) {
            exposedConnectors++;
        }

        if (getTile(r, c + 1) != null && checkConnection(getTile(r,c).getConnectors().get(2),getTile(r, c + 1).getConnectors().get(0))){
            findPaths(r,c + 1 ,visited);
        }
        else if (getTile(r,c).getConnectors().get(2) == Connector.SINGLE || getTile(r,c).getConnectors().get(2) == Connector.UNIVERSAL || getTile(r,c).getConnectors().get(2) == Connector.DOUBLE) {
            exposedConnectors++;
        }

        if (getTile(r + 1, c) != null && checkConnection(getTile(r,c).getConnectors().get(1),getTile(r + 1, c ).getConnectors().get(3))){
            findPaths(r +1,c ,visited);
        }
        else if (getTile(r,c).getConnectors().get(3) == Connector.SINGLE || getTile(r,c).getConnectors().get(3) == Connector.UNIVERSAL || getTile(r,c).getConnectors().get(3) == Connector.DOUBLE) {
            exposedConnectors++;
        }

    }


    //trova i tronconi validi post attacco
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


/**
 * Method killHuman reduces the number of humans in a housing cell by 1 given the coordinate of this cell
 *
 * @param coordinate of type IntegerPair - the value of the coordinate.
 */
    public void killHuman(IntegerPair coordinate){
        Plance[coordinate.getFirst()][coordinate.getSecond()].getComponent().setAbility(1, true, true);
    }


    //potenza istantanea cioè scorro arraylist dei cannoni , se è singolo incremento o del valore nominale o per la metèà in base alla direzione, se è doppio uso un altro metodo
    //che richiede un input dal player
    public double getPower() {
        double power = 0;
        for (IntegerPair cannon : plasmaDrills){
            if (Plance[cannon.getFirst()][cannon.getSecond()].getConnectors().get(1) == Connector.CANNON){
                if (Plance[cannon.getFirst()][cannon.getSecond()].getComponent().getAbility() == 1){
                    power += 1;
                }
                else{
                    //richiedi input con if else
                    power += 2;

                }
            }

            else{
                if (Plance[cannon.getFirst()][cannon.getSecond()].getComponent().getAbility() == 1){
                    power += 0.5;
                }
                else{
                    //richiedi input con if else
                    power += 1;

                }
            }
        }
        return power;
    }

}
