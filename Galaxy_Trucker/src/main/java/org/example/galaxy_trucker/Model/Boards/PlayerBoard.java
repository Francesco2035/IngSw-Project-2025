package org.example.galaxy_trucker.Model.Boards;


import org.example.galaxy_trucker.Exceptions.*;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Tiles.*;
import org.example.galaxy_trucker.Model.GetterHandler.*;
import org.example.galaxy_trucker.Model.SetterHandler.*;




import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerBoard {


    private int totalValue;
    private Tile[][] PlayerBoard;
    private int[][] ValidPlayerBoard;
    private int damage;
    private int exposedConnectors;
    private int[] shield;

    private boolean purpleAlien;
    private boolean brownAlien;

    private PlayerBoardGetters getter;
    private PlayerBoardSetters setter;


    private ArrayList<Goods> BufferGoods;



    private Map<Class<?>, ArrayList<IntegerPair>> classifiedTiles;
    private Map<Class<?>, ArrayList<IntegerPair>> storedGoods;


    private ArrayList<Tile> Buffer;

    public HashMap<Connector, ArrayList<Connector>> getValidConnection() {
        return validConnection;
    }

    HashMap<Connector, ArrayList<Connector>>  validConnection;



    public PlayerBoard(int lv) {
        this.damage = 0;
        this.shield = new int[4];
        this.Buffer = new ArrayList<>();
        this.totalValue = 0;


        this.purpleAlien = false;
        this.brownAlien = false;

        this.classifiedTiles = new HashMap<>();
        this.storedGoods = new HashMap<>();

        this.exposedConnectors = 0;

        this.BufferGoods = new ArrayList<>();

        this.ValidPlayerBoard = new int[10][10];
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


        if (lv == 2) {
            for (int x = 0; x < 10; x++) {
                for (int y = 0; y < 10; y++) {
                    if (x < 4 || y < 3 || (x == 4 && (y == 3 || y == 4 || y == 6 || y == 8 || y == 9)) ||(x == 5 && (y == 3 || y== 9)) || (x == 8 && y == 6) || x ==9) {
                        ValidPlayerBoard[x][y] = -1;
                    }
                    else if (x == 6 && y == 6) {
                        ValidPlayerBoard[x][y] = 1;
                    }
                    else {
                        ValidPlayerBoard[x][y] = 0;
                    }

                }
            }
        }
        else {
            for (int x = 0; x < 10; x++) {
                for (int y = 0; y < 10; y++) {
                    if(y <= 3 ||x == 9 || y == 9|| x <4 || ( x == 4 && (y != 6)) || (x== 5 &&(y <5 || y>7)) || (x==8 && y == 6))  {
                        ValidPlayerBoard[x][y] = -1;
                    }
                    else if (x == 6 && y == 6) {
                        ValidPlayerBoard[x][y] = 1;
                    }
                    else {
                        ValidPlayerBoard[x][y] = 0;
                    }
                }
            }

        }
        this.PlayerBoard = new Tile[10][10];
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                if (ValidPlayerBoard[x][y] != 1) {
                    PlayerBoard[x][y] =  new Tile(new IntegerPair(x,y), new spaceVoid() ,Connector.NONE, Connector.NONE,Connector.NONE, Connector.NONE);
                }
                else {
                    PlayerBoard[x][y] = null;
                }

            }
        }
        this.PlayerBoard[6][6] = new Tile(new IntegerPair(6,6), new MainCockpitComp(0),Connector.UNIVERSAL, Connector.UNIVERSAL,Connector.UNIVERSAL, Connector.UNIVERSAL);
    }

    public Map<Class<?>, ArrayList<IntegerPair>> getClassifiedTiles() {
        return classifiedTiles;
    }

    public void setGetter(PlayerBoardGetters getter) {
        this.getter = getter;
    }

    public void setSetter(PlayerBoardSetters setter) {
        this.setter = setter;
    }

    public void setTotalValue(int i){
        this.totalValue += i;
    }

    public int getTotalValue(){
        return this.totalValue;
    }

    /**
     * Method insertBuffer inserts the tile passed by the player into the buffer.
     *
     * @param t of type Tile .
     * @throws IllegalStateException if the buffer's size is = 2
     */
    public void insertBuffer(Tile t) throws IllegalStateException {
        if (Buffer.size() >= 2) {
            throw new IllegalStateException("Buffer is full");
        }
        Buffer.add(t);
    }


    /**
     * Method getBuffer return the buffer.
     *
     * @return the Buffer.
     */
    public ArrayList<Tile> getBuffer() throws InvalidInput{
        return Buffer;
    }

    public PlayerBoardGetters getGetter(){
        return getter;
    }

    public PlayerBoardSetters getSetter(){
        return setter;
    }



    /**
     * Method getExposedConnectors retrieves the number of exposed connectors on the player board.
     *
     * @return the number of exposed connectors.
     */
    public int getExposedConnectors(){
        return exposedConnectors;
    }



    /**
     * Method getShield retrieves the shield status of the player board.
     * The shield is represented as an array of size 4, where each index corresponds to a direction:
     * <ul>
     *   <li>Index 0 - Left</li>
     *   <li>Index 1 - Top</li>
     *   <li>Index 2 - Right</li>
     *   <li>Index 3 - Bottom</li>
     * </ul>
     * A value of 0 indicates no protection, while a non-zero value means the side is protected.
     *
     * @return an integer array of size 4 representing the shield status in each direction.
     */
    public int[] getShield(){
        return shield;
    }


    /**
     * Method getPlayerBoard retrieves the player's board, which consists of a grid of tiles.
     *
     * @return a 2D array of Tile objects representing the player's board.
     */
    public Tile[][] getPlayerBoard(){
        return this.PlayerBoard;
    }


    /**
     * Method getValidPlayerBoard retrieves the validity matrix of the player board.
     * Each cell indicates whether the position is valid (1), invalid (-1), or empty (0).
     *
     * @return a 2D array of integers representing the validity of each position.
     */
    public int[][] getValidPlayerBoard(){
        return this.ValidPlayerBoard;
    }




    /**
     * Method getDamage retrieves the current damage value of the player board.
     *
     * @return the amount of damage.
     */
    public int getDamage(){
        return damage;
    }

    public Map<Class<?>, ArrayList<IntegerPair>> getStoredGoods(){
        return storedGoods;
    }


    /**
     * Method getTile retrieves the tile located at the specified coordinates on the player board.
     *
     * @param x of type int - the x-coordinate of the tile.
     * @param y of type int - the y-coordinate of the tile.
     * @return the Tile object at the given coordinates.
     * @throws InvalidInput If the coordinates are out of bounds or point to an invalid tile.
     */
    public Tile getTile(int x, int y) throws InvalidInput {
        if (x < 0 || x >= 10 || y < 0 || y >= 10 || ValidPlayerBoard[x][y] != 1) {
            throw new InvalidInput(x, y, "Invalid input: coordinates out of bounds or invalid tile.");
        }
        return this.PlayerBoard[x][y];
    }


    public void classifyTile(Tile tile, int x, int y){

        //System.out.println(tile.getComponent().getClass());
        classifiedTiles.computeIfAbsent(tile.getComponent().getClass(), k -> new ArrayList<>()).add(new IntegerPair(x, y));

    }

    /**
     * Method insertTile inserts a tile into the player board at the specified coordinates.
     *
     * @param tile of type Tile - the tile to be placed.
     * @param x of type int - the x-coordinate where the tile should be placed.
     * @param y of type int - the y-coordinate where the tile should be placed.
     * @throws NullPointerException If the tile is null.
     * @throws InvalidInput If the coordinates are out of bounds or if the target position is invalid.
     */
    public void insertTile(Tile tile, int x, int y) throws NullPointerException, InvalidInput {

        if (tile == null) {
            throw new NullPointerException("Tile cannot be null.");
        }

        if (x < 0 || x >= PlayerBoard.length || y < 0 || y >= PlayerBoard[0].length || ValidPlayerBoard[x][y] == -1) {
            throw new InvalidInput(x, y, "Invalid input: coordinates out of bounds or invalid tile.");
        }


        this.PlayerBoard[x][y] = tile;
        ValidPlayerBoard[x][y] = 1;

    }


    /**
     * Method checkConnection checks whether two connectors can be soldered.
     *
     * @param t1 of type Connector .
     * @param t2 of type Connector .
     * @return true if the connection is legal, false otherwise.
     */
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


    /**
     * Method PathNotVisited checks if there are any tiles that have not been reached by the findPaths method.
     *
     * @param visited of type Arraylist<IntegerPair> - path calculated by the findPaths method.
     * @return returns true if there is at least one unreached tile, false otherwise.
     */
    public boolean PathNotVisited(ArrayList<IntegerPair> visited){

        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {

                if (ValidPlayerBoard[x][y] == 1 && !visited.contains(new IntegerPair(x,y))){
                    return true;
                }
            }

        }
        return false;

    }


    /**
     * Method checkIllegal checks if there is at least one illegal hotWaterHeater or plasmaDrill.
     *
     * @param visited of type ArrayList<IntegerPair> - player board saved as if it were a path.
     * @return boolean true if everything is legal, false otherwise.
     */
    public boolean checkIllegal( ArrayList<IntegerPair> visited){
        System.out.println("controllo illegalità");
        int x;
        int y;

        for (IntegerPair pair : visited) {

            x = pair.getFirst();
            y = pair.getSecond();
            System.out.println(x+ " " + y);

            if(!PlayerBoard[x][y].controlDirections(this,x,y)){
                return false;
            }
        }
        return true;

    }


    /**
     * Method removeTile removes the selected Tile.
     *
     * @param x of type int.
     * @param y of type int.
     * @throws InvalidInput if the input from the player is invalid.
     */

    public void removeTile(int x, int y) throws InvalidInput {
        if (x < 0 || x >= 10 || y < 0 || y >= 10 || ValidPlayerBoard[x][y] == -1) {
            throw new InvalidInput(x, y, "Invalid input: coordinates out of bounds or invalid tile.");
        }
        PlayerBoard[x][y] = new Tile(new IntegerPair(x,y), new spaceVoid() ,Connector.NONE, Connector.NONE, Connector.NONE);
        ValidPlayerBoard[x][y] = 0;
    }


    /**
     * Method checkValidity check if the player board is valid according to the rules of the game : there can be no unreachable pieces
     * and the constraints on the hotWaterHeaters and plasmaDrills must be respected.
     *
     * @return true if the player board is valid, false otherwise.
     */
    public boolean checkValidity(){

        int r = 6;
        int c = 6;

        ArrayList<IntegerPair> visitedPositions = new ArrayList<>();

        findPaths(r,c,visitedPositions);


        if (PathNotVisited(visitedPositions)){
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


    /**
     * Method findPaths finds all paths reachable using the dfs algorithm.
     *
     *
     * @param r of type int - x coordinate.
     * @param c of type int - y coordinate.
     * @param visited of type Arraylist<IntegerPair> - keeps track of all the tiles already visited.
     */
    public void findPaths(int r, int c, ArrayList<IntegerPair> visited) {

        if (visited.contains(new IntegerPair(r, c))||r < 0 || c < 0 || r > 9 || c > 9 || this.ValidPlayerBoard[r][c] == -1) {
            return;
        }
        visited.add(new IntegerPair(r, c));
        System.out.println(r + " " + c);

        if (ValidPlayerBoard[r][c-1] == 1 && checkConnection(getTile(r,c).getConnectors().get(0),getTile(r, c -1).getConnectors().get(2))) {
            findPaths(r, c - 1, visited);
        }

        if (ValidPlayerBoard[r-1][c] == 1 && checkConnection(getTile(r,c).getConnectors().get(1),getTile(r-1, c ).getConnectors().get(3))){
            findPaths(r -1,c ,visited);
        }

        if (ValidPlayerBoard[r][c+1] == 1 && checkConnection(getTile(r,c).getConnectors().get(2),getTile(r, c + 1).getConnectors().get(0))){
            findPaths(r,c + 1 ,visited);
        }

        if (ValidPlayerBoard[r+1][c] == 1 && checkConnection(getTile(r,c).getConnectors().get(1),getTile(r + 1, c ).getConnectors().get(3))){
            findPaths(r +1,c ,visited);
        }

    }


    /**
     * Method destroy destroys the designated tile adding a spaceVoid tile in its place, possibly updating the class attributes.
     *
     * @param x of type int - x coordinate.
     * @param y of type int - y coordinate.
     */
    public void destroy(int x, int y){

        classifiedTiles.get(PlayerBoard[x][y].getComponent().getClass()).remove(new IntegerPair(x,y));
        damage++;
        PlayerBoard[x][y] = new Tile(new IntegerPair(x,y), new spaceVoid(),Connector.NONE, Connector.NONE, Connector.NONE, Connector.NONE);
        ValidPlayerBoard[x][y] = 0;
    }



    /**
     * Method chosePlayerBoard returns the selected chunk and calculates the actual damage suffered.
     *
     * @param shipSection HashMap<Integer, ArrayList<IntegerPair>> - collection of chunks.
     * @param i of type int - the chunk selected.
     */
    public ArrayList<IntegerPair> choosePlayerBoard(HashMap<Integer, ArrayList<IntegerPair>> shipSection , int i){
        //questo metodo non ha molto senso
        return shipSection.get(i);

    }


    /**
     * Method modifyPlayerBoard modifies the player board according to the chunk chosen by the player at the time of the destruction of the ship. The method
     * updates the class attributes by calling updateAttributes.
     *
     * @param newPlayerBoard of type ArrayList<IntegerPair> - x coordinate.
     */
    public void modifyPlayerBoard(ArrayList<IntegerPair> newPlayerBoard){
        for (int x = 0; x <10; x++ ){
            for(int y = 0; y <10; y++){
                if (ValidPlayerBoard[x][y] == 1){
                    if(!newPlayerBoard.contains(new IntegerPair(x,y))){
                        PlayerBoard[x][y] = new Tile(new IntegerPair(x,y),new spaceVoid(),Connector.NONE, Connector.NONE, Connector.NONE, Connector.NONE);
                        ValidPlayerBoard[x][y] = 0;
                        damage++;
                    }
                }
            }
        }
        updateAttributes(newPlayerBoard.getFirst().getFirst(),newPlayerBoard.getFirst().getSecond());

    }


    /**
     * Method updateAttributes clears all the attributes of the player board and then updates
     * them starting from the coordinates of a tile by calling the updateBoardAttributes method.
     *
     * @param x of type int - x coordinate.
     * @param y of type int - y coordinate.
     */
    public void updateAttributes(int x, int y){
        this.exposedConnectors = 0;
        for(int i = 0; i < 4; i ++){
            shield[i] = 0;
        }

        classifiedTiles = new HashMap<>();
        ArrayList<IntegerPair> visitedPositions = new ArrayList<>();
        updateBoardAttributes(x,y, visitedPositions);

        if (classifiedTiles.containsKey(shieldGenerator.class)){
            for (int i = 0; i < classifiedTiles.get(shieldGenerator.class).size(); i++) {
                for (int j = 0; j< 4; j++){
                    shield[j] += PlayerBoard[classifiedTiles.get(shieldGenerator.class).get(i).getFirst()]
                                            [classifiedTiles.get(shieldGenerator.class).
                                            get(i).getSecond()].getComponent().getAbility(0).get(j);
                }
            }
        }
        if (classifiedTiles.containsKey(modularHousingUnit.class)){
            for (IntegerPair pair : classifiedTiles.get(modularHousingUnit.class)){
                PlayerBoard[pair.getFirst()][pair.getSecond()].controlDirections(this, pair.getFirst(), pair.getSecond());
            }
        }



    }


    /**
     * Method updateBoardAttributes updates all the attributes of the class also calculating the number of exposed connectors
     * using the dfs algorithm.
     *
     * @param r of type int - x coordinate.
     * @param c of type int - y coordinate.
     * @param visited of type ArrayList<IntegerPair> - keeps track of all the tiles already visited.
     */
    public void     updateBoardAttributes(int r, int c,ArrayList<IntegerPair> visited){
        if (visited.contains(new IntegerPair(r, c))||r < 0 || c < 0 || r > 9 || c > 9 || this.ValidPlayerBoard[r][c] != 1) {
            return;
        }
        System.out.println("update: "+r + " " + c);

        classifyTile(PlayerBoard[r][c], r,c);

        visited.add(new IntegerPair(r, c));

        System.out.println(r + " " + c);


        if (ValidPlayerBoard[r][c-1] == 1 && checkConnection(getTile(r,c).getConnectors().get(0),getTile(r, c -1).getConnectors().get(2))) {
            updateBoardAttributes(r, c - 1, visited);
        }
        else if (getTile(r,c).getConnectors().get(0) == Connector.SINGLE || getTile(r,c).getConnectors().get(0) == Connector.UNIVERSAL || getTile(r,c).getConnectors().get(0) == Connector.DOUBLE) {
            exposedConnectors++;
        }

        if (ValidPlayerBoard[r-1][c] == 1 && checkConnection(getTile(r,c).getConnectors().get(1),getTile(r-1, c ).getConnectors().get(3))){
            updateBoardAttributes(r -1,c ,visited);
        }
        else if (getTile(r,c).getConnectors().get(1) == Connector.SINGLE || getTile(r,c).getConnectors().get(1) == Connector.UNIVERSAL || getTile(r,c).getConnectors().get(1) == Connector.DOUBLE) {
            exposedConnectors++;
        }

        if (ValidPlayerBoard[r][c+1] == 1&& checkConnection(getTile(r,c).getConnectors().get(2),getTile(r, c + 1).getConnectors().get(0))){
            updateBoardAttributes(r,c + 1 ,visited);
        }
        else if (getTile(r,c).getConnectors().get(2) == Connector.SINGLE || getTile(r,c).getConnectors().get(2) == Connector.UNIVERSAL || getTile(r,c).getConnectors().get(2) == Connector.DOUBLE) {
            exposedConnectors++;
        }

        if (ValidPlayerBoard[r+1][c] == 1 && checkConnection(getTile(r,c).getConnectors().get(1),getTile(r + 1, c ).getConnectors().get(3))){
            updateBoardAttributes(r +1,c ,visited);
        }
        else if (getTile(r,c).getConnectors().get(3) == Connector.SINGLE || getTile(r,c).getConnectors().get(3) == Connector.UNIVERSAL || getTile(r,c).getConnectors().get(3) == Connector.DOUBLE) {
            exposedConnectors++;
        }

    }


    /**
     * Method handleAttack finds all possible chunks after a tile is destroyed by calling findPaths for all 4
     * direction of the destroyed tile.
     *
     * @param x of type int - x coordinate of the destroyed Tile.
     * @param y of type int - y coordinate of the destroyed Tile.
     * @return a collection of all possible chunks.
     */
    public HashMap<Integer, ArrayList<IntegerPair>> handleAttack(int x, int y){

        ArrayList<IntegerPair> visitedPositions = new ArrayList<>();
        int i = 0;
        HashMap<Integer, ArrayList<IntegerPair>> shipSection = new HashMap<>();

        if (ValidPlayerBoard[x-1][y] == 1){

            findPaths(x-1, y, visitedPositions);

            shipSection.put(i, visitedPositions);
            i++;

        }

        if (ValidPlayerBoard[x][y-1] == 1 ){
            visitedPositions = new ArrayList<>();
            findPaths(x, y-1, visitedPositions);
            if (!visitedPositions.contains(new IntegerPair(x,y-1))) {

                shipSection.put(i, visitedPositions);
                i++;
            }

        }


        if (ValidPlayerBoard[x+1][y] == 1 ){
            visitedPositions = new ArrayList<>();
            findPaths(x+1, y, visitedPositions);
            if (!visitedPositions.contains(new IntegerPair(x-1,y)) && !visitedPositions.contains(new IntegerPair(x,y -1))) {

                shipSection.put(i, visitedPositions);
                i++;
            }

        }

        if (ValidPlayerBoard[x][y + 1] == 1  ){
            visitedPositions = new ArrayList<>();
            findPaths(x, y+1, visitedPositions);
            if (!visitedPositions.contains(new IntegerPair(x-1,y)) && !visitedPositions.contains(new IntegerPair(x,y -1)) && !visitedPositions.contains(new IntegerPair(x+1,y)) ) {

                shipSection.put(i, visitedPositions);
                i++;
            }

        }

        return shipSection;

    }


    /**
     * Method pullFromBuffer pull the good in position i of the BufferGoods.
     *
     * @param i of type int.
     * @return the good in order to be added to the storageCompartment.
     * @throws InvalidInput If the specified index is out of bounds or if the buffer is empty.
     */
    public Goods pullFromBuffer(int i) throws InvalidInput{
        if (i > BufferGoods.size()) {
            throw new InvalidInput("This position in the BufferGoods does not exist");
        }
        if (BufferGoods.isEmpty()) {
            throw new InvalidInput("BufferGoods is empty");
        }
        return BufferGoods.remove(i);
    }

}