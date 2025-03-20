package org.example.galaxy_trucker.Model.Boards;


import org.example.galaxy_trucker.Exceptions.*;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Tiles.*;
import org.example.galaxy_trucker.Model.Void;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlayerBoard {

    private Tile[][] PlayerBoard;
    private int[][] ValidPlayerBoard;
    private int damage;
    private int exposedConnectors;
    private int[] shield;


    private ArrayList<Goods> BufferGoods;

    private ArrayList<IntegerPair> housingUnits;
    private ArrayList<IntegerPair> energyTiles;
    private ArrayList<IntegerPair> Cargo;
    private ArrayList<IntegerPair> plasmaDrills;
    private ArrayList<IntegerPair> hotWaterHeaters;


    private ArrayList<Tile> Buffer;
    HashMap<Connector, ArrayList<Connector>>  validConnection;




    public PlayerBoard(int lv) {
        this.damage = 0;
        this.shield = new int[4];
        this.Buffer = new ArrayList<>();

        this.exposedConnectors = 0;
        this.housingUnits = new ArrayList<>();
        this.energyTiles = new ArrayList<>();
        this.hotWaterHeaters = new ArrayList<>();
        this.Cargo = new ArrayList<>();
        this.plasmaDrills = new ArrayList<>();

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
                if (ValidPlayerBoard[x][y] == -1) {
                    PlayerBoard[x][y] =  new Tile(new IntegerPair(x,y), new Void() ,Connector.NONE, Connector.NONE,Connector.NONE, Connector.NONE);
                }
                else {
                    PlayerBoard[x][y] = null;
                }

            }
        }
        this.PlayerBoard[6][6] = new Tile(new IntegerPair(6,6), new MainCockpitComp(0),Connector.UNIVERSAL, Connector.UNIVERSAL,Connector.UNIVERSAL, Connector.UNIVERSAL);
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


    /**
     * Method getExposedConnectors retrieves the number of exposed connectors on the player board.
     *
     * @return the number of exposed connectors.
     */
    public int getExposedConnectors(){
        return exposedConnectors;
    }



    /**
     * Method gethousingUnits retrieves a list of coordinates representing housing units on the player board.
     *
     * @return an ArrayList of IntegerPair containing the coordinates of housing units.
     */
   public ArrayList<IntegerPair> gethousingUnits(){
        return this.housingUnits;
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
     * Method getEnergyTiles retrieves a list of coordinates representing energy tiles on the player board.
     *
     * @return an ArrayList of IntegerPair containing the coordinates of energy tiles.
     */
    public ArrayList<IntegerPair> getEnergyTiles() {
        return energyTiles;
    }


    /**
     * Method getDamage retrieves the current damage value of the player board.
     *
     * @return the amount of damage.
     */
    public int getDamage(){
        return damage;
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
        if (x < 0 || x >= 10 || y < 0 || y >= 10 || ValidPlayerBoard[x][y] == -1) {
            throw new InvalidInput(x, y, "Invalid input: coordinates out of bounds or invalid tile.");
        }
        return this.PlayerBoard[x][y];
    }


    /**
     * Method getPlasmaDrills retrieves the list of coordinates where plasma drills are located.
     *
     * @return an ArrayList of IntegerPair representing the positions of plasma drills.
     */
    public ArrayList<IntegerPair> getPlasmaDrills(){
        return plasmaDrills;
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
        //System.out.println("controllo illegalità");
        int x;
        int y;

        for (IntegerPair pair : visited) {
            x = pair.getFirst();
            y = pair.getSecond();

            if (ValidPlayerBoard[x][y] == 1 && (PlayerBoard[x][y].getComponent().getClass() == plasmaDrill.class || PlayerBoard[x][y].getComponent().getClass() == hotWaterHeater.class)) {
                //System.out.println(x + " " + y);

                if (ValidPlayerBoard[x][y] == 1 && (PlayerBoard[x][y].getConnectors().get(0) == Connector.MOTOR  || PlayerBoard[x][y].getConnectors().get(1) == Connector.MOTOR) || PlayerBoard[x][y].getConnectors().get(2) == Connector.MOTOR){
                    //System.out.println("Motore illegale");
                    return false;
                }

                if(ValidPlayerBoard[x][y-1] == 1 && PlayerBoard[x][y].getConnectors().get(0) == Connector.CANNON ) {
                    //System.out.println("illegale da dx");
                    return false;
                }

                if(ValidPlayerBoard[x-1][y] == 1 && PlayerBoard[x][y].getConnectors().get(1) == Connector.CANNON ) {
                    //System.out.println("illegale dal basso");
                    return false;
                }

                if(ValidPlayerBoard[x][y+1] == 1 && PlayerBoard[x][y].getConnectors().get(2) == Connector.CANNON ) {
                    //System.out.println("illegale da sx");
                    return false;
                }

                if(ValidPlayerBoard[x+1][y] == 1 && (PlayerBoard[x][y].getConnectors().get(3) == Connector.CANNON || PlayerBoard[x][y].getConnectors().get(3) == Connector.MOTOR)) {
                    //System.out.println("illegale dall'alto");
                    return false;
                }

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
        this.PlayerBoard[x][y] = new Tile(new IntegerPair(x,y), new Void() ,Connector.NONE, Connector.NONE, Connector.NONE);
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


    /**
     * Method destroy destroys the designated tile adding a spaceVoid tile in its place, possibly updating the class attributes.
     *
     * @param x of type int - x coordinate.
     * @param y of type int - y coordinate.
     */
    public void destroy(int x, int y){
        IntegerPair pos = new IntegerPair(x, y);
        housingUnits.remove(pos);
        energyTiles.remove(pos);
        plasmaDrills.remove(pos);
        Cargo.remove(pos);
        hotWaterHeaters.remove(pos);

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

        for (int j = 0; j < shipSection.size(); j++) {
            damage += shipSection.get(j).size();
        }
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

        this.housingUnits.clear();
        this.energyTiles.clear();
        this.Cargo.clear();
        this.plasmaDrills.clear();
        ArrayList<IntegerPair> visitedPositions = new ArrayList<>();
        updateBoardAttributes(x,y, visitedPositions);

    }


    /**
     * Method updateBoardAttributes updates all the attributes of the class also calculating the number of exposed connectors
     * using the dfs algorithm.
     *
     * @param r of type int - x coordinate.
     * @param c of type int - y coordinate.
     * @param visited of type ArrayList<IntegerPair> - keeps track of all the tiles already visited.
     */
    public void updateBoardAttributes(int r, int c,ArrayList<IntegerPair> visited){
        if (visited.contains(new IntegerPair(r, c))||r < 0 || c < 0 || r > 9 || c > 9 || this.ValidPlayerBoard[r][c] == -1) { //!= 1
            return;
        }

        else if (PlayerBoard[r][c].getComponent().getClass() == powerCenter.class){
            energyTiles.add(new IntegerPair(r,c));
        }

        else if (PlayerBoard[r][c].getComponent().getClass() == plasmaDrill.class){
            plasmaDrills.add(new IntegerPair(r, c));
        }

        else if (PlayerBoard[r][c].getComponent().getClass() == modularHousingUnit.class || PlayerBoard[r][c].getComponent().getClass() == MainCockpitComp.class){
            housingUnits.add(new IntegerPair(r,c));
        }

        else if (PlayerBoard[r][c].getComponent().getClass() == shieldGenerator.class){
            for (int i = 0; i < shield.length; i++){
                shield[i] += PlayerBoard[r][c].getComponent().getAbility(1).get(i);
            }
        }

        else if (PlayerBoard[r][c].getComponent().getClass() == hotWaterHeater.class ){
            hotWaterHeaters.add(new IntegerPair(r,c));
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

            findPaths(x, y-1, visitedPositions);

            shipSection.put(i, visitedPositions);
            i++;

        }

        if (ValidPlayerBoard[x][y-1] == 1 ){
            visitedPositions = new ArrayList<>();
            findPaths(x, y-1, visitedPositions);
            if (!visitedPositions.contains(new IntegerPair(x-1,y))) {

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
     * Method kill reduces the number of Human or Alien in a housing cell by 1 given the coordinate of this cell
     *
     * @param coordinate of type IntegerPair - the value of the coordinate.
     * @throws ArrayIndexOutOfBoundsException when the user input is not in the correct range.
     * @throws HousingUnitEmptyException when the housingUnit is empty or is not possible to kill such a number of humans.
     */
    public void kill(IntegerPair coordinate,int humans ,boolean purpleAlien, boolean brownAlien) throws InvalidInput, HousingUnitEmptyException{
        if (coordinate.getFirst() < 0 || coordinate.getFirst() >= PlayerBoard.length || coordinate.getSecond() < 0 || coordinate.getSecond() >= PlayerBoard[0].length || ValidPlayerBoard[coordinate.getFirst()][coordinate.getSecond()] == -1) {
            throw new InvalidInput(coordinate.getFirst(), coordinate.getSecond(), "Invalid input: coordinates out of bounds or invalid tile." );
        }
        Component unit =  PlayerBoard[coordinate.getFirst()][coordinate.getSecond()].getComponent();
        if ((purpleAlien && !unit.isPurpleAlien()) || (brownAlien && !unit.isBrownAlien())) {
            throw new HousingUnitEmptyException("There is no alien to kill");
        }

        if (humans > unit.getAbility()){
            throw new HousingUnitEmptyException("It is not possible to kill such a number of humans");
        }
        unit.setAbility(humans, purpleAlien, brownAlien);
    }


    /**
     * Method getPower calculates the instantaneous power of the ship also based on the player's choices
     *
     * @return the Power of the ship.
     * @throws NullPointerException if chosenPlasmaDrills is null.
     */
    public double getPower(ArrayList<IntegerPair> chosenPlasmaDrills) {
        if (chosenPlasmaDrills == null) {
            throw new NullPointerException("chosenPlasmaDrills cannot be null.");
        }

        double power = 0;
        for (IntegerPair cannon : chosenPlasmaDrills){
            if (PlayerBoard[cannon.getFirst()][cannon.getSecond()].getConnectors().get(1) == Connector.CANNON){
                if (PlayerBoard[cannon.getFirst()][cannon.getSecond()].getComponent().getAbility() == 1){
                    power += 1;
                }
                else{
                    power += 2;
                }
            }
            else{
                if (PlayerBoard[cannon.getFirst()][cannon.getSecond()].getComponent().getAbility() == 1){
                    power += 0.5;
                }
                else{
                    power += 1;
                }
            }
        }
        return power;
    }


    /**
     * Method getEnginePower calculates the instantaneous EnginePower of the ship also based on the player's choices
     *
     * @param chosenHotWaterHeaters List of chosen hot water heaters for calculating engine power.
     * @return the EnginePower of the ship.
     * @throws NullPointerException if chosenHotWaterHeaters is null.
     * @throws InvalidInput if at least one engine is invalid.
     */
    public int getEnginePower(ArrayList<IntegerPair> chosenHotWaterHeaters) throws InvalidInput{
        if (chosenHotWaterHeaters == null) {
            throw new NullPointerException("chosenHotWaterHeaters cannot be null.");
        }

        if (!hotWaterHeaters.containsAll(chosenHotWaterHeaters)) {
            throw new InvalidInput("Invalid choice, at least one engine is invalid");
        }

        int power = 0;
        for (IntegerPair engine : chosenHotWaterHeaters){

            if (PlayerBoard[engine.getFirst()][engine.getSecond()].getComponent().getAbility() == 1){
                power += 1;
            }
            else{
                power += 2;
            }

        }
        return power;
    }


    /**
     * Method pullGoods removes the element at position i of a storageComponent and adds it to the BufferGoods.
     *
     * @param i of type int.
     * @param coordinate of type IntegerPair.
     * @throws InvalidInput If the coordinates are out of bounds, the tile is not a storage compartment,
     *                      or the requested position in the compartment does not exist.
     * @throws StorageCompartmentEmptyException If the storage compartment is empty and no goods can be removed.
     */
    public void pullGoods(int i, IntegerPair coordinate) throws InvalidInput, StorageCompartmentEmptyException{

        int x = coordinate.getFirst();
        int y = coordinate.getSecond();

        if (x < 0 || x >= 10 || y < 0 || y >= 10 || ValidPlayerBoard[x][y] == -1) {
            throw new InvalidInput(x, y, "Invalid input: coordinates out of bounds or invalid tile.");
        }

        if (PlayerBoard[x][y].getComponent().getClass() != storageCompartment.class && PlayerBoard[x][y].getComponent().getClass() != specialStorageCompartment.class){
            throw new InvalidInput("The following tile is not a storageCompartment");
        }

        if (PlayerBoard[x][y].getComponent().getAbility(null).isEmpty()){
            throw new StorageCompartmentEmptyException("The following StorageCompartment is empty: " + x + "," + y);
        }

        if (PlayerBoard[x][y].getComponent().getAbility(null).size() < i){
            throw new InvalidInput("This position in the StorageCompartment does not exist");
        }

        BufferGoods.add(PlayerBoard[coordinate.getFirst()][coordinate.getSecond()].getComponent().getAbility(null).remove(i));
    }


    /**
     * Method putGoods adds a good to a storageCompartment.
     *
     * @param good of type Goods.
     * @param coordinate of type IntegerPair.
     * @throws InvalidInput If the coordinates are out of bounds, the tile is not a storage compartment.
     * @throws StorageCompartmentFullException If the storage compartment is full and cannot accept more goods.
     * @throws IllegalArgumentException If the good being added is not allowed in a standard storage compartment.
     */
    public void putGoods(Goods good, IntegerPair coordinate) throws IllegalArgumentException, InvalidInput, StorageCompartmentFullException {

        int x = coordinate.getFirst();
        int y = coordinate.getSecond();

        if (x < 0 || x >= 10 || y < 0 || y >= 10 || ValidPlayerBoard[x][y] == -1) {
            throw new InvalidInput(x, y, "Invalid input: coordinates out of bounds or invalid tile.");
        }

        if (PlayerBoard[x][y].getComponent().getClass() != storageCompartment.class && PlayerBoard[x][y].getComponent().getClass() != specialStorageCompartment.class){
            throw new InvalidInput("The following tile is not a storageCompartment");
        }

        if (PlayerBoard[x][y].getComponent().getAbility(null).size() + 1 > PlayerBoard[x][y].getComponent().getAbility()){
            throw new StorageCompartmentFullException("The following StorageCompartment is full: " + x + "," + y);
        }

        PlayerBoard[x][y].getComponent().setAbility(good, true);
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


    /**
     * Method useEnergy reduces the energy of the tiles in the array by 1.
     *
     * @param chosenEnergyTiles of type ArrayList<IntegerPair> chosenEnergyTiles - the position of the EnergyTiles chosen by the player,
     *                          there may be duplicates if the user wants to consume more energy from the same tile .
     * @throws NullPointerException If the provided list is null.
     * @throws InvalidInput If at least one of the chosen energy tiles is invalid or does not exist in the player's energyTiles list.
     * @throws powerCenterEmptyException If any of the selected power centers are empty and cannot provide energy.
     */
    public void useEnergy(ArrayList<IntegerPair> chosenEnergyTiles) throws InvalidInput, powerCenterEmptyException{
        if (chosenEnergyTiles == null) {
            throw new NullPointerException("chosenEnergyTiles cannot be null.");
        }
        if (!energyTiles.containsAll(chosenEnergyTiles)) {
            throw new InvalidInput("Invalid choice, at least one energy is invalid");
        }
        for (IntegerPair energy : chosenEnergyTiles){
            if (PlayerBoard[energy.getFirst()][energy.getSecond()].getComponent().getAbility() == 0){
                throw new powerCenterEmptyException("PowerCenter is empty");
            }
            PlayerBoard[energy.getFirst()][energy.getSecond()].getComponent().setAbility();
        }
    }


    /**
     * Method removeGood removes a good from the specified StorageCompartment at the given position.
     *
     * @param coordinate of type IntegerPair.
     * @param i of type int.
     * @throws InvalidInput If the coordinates are out of bounds, the tile is invalid, or the selected tile is not a storage compartment.
     * @throws StorageCompartmentEmptyException If the storage compartment is empty and there are no goods to remove.
     */
    public void removeGood(IntegerPair coordinate, int i) throws InvalidInput, StorageCompartmentEmptyException {

        int x = coordinate.getFirst();
        int y = coordinate.getSecond();

        if (x < 0 || x >= 10 || y < 0 || y >= 10 || ValidPlayerBoard[x][y] == -1) {
            throw new InvalidInput(x, y, "Invalid input: coordinates out of bounds or invalid tile.");
        }

        if (PlayerBoard[x][y].getComponent().getClass() != storageCompartment.class && PlayerBoard[x][y].getComponent().getClass() != specialStorageCompartment.class){
            throw new InvalidInput("The following tile is not a storageCompartment");
        }

        if (PlayerBoard[x][y].getComponent().getAbility() == 0){
            throw new StorageCompartmentEmptyException("The following StorageCompartment is Empty: " + x + "," + y);
        }

        PlayerBoard[x][y].getComponent().getAbility(null).remove(i);
    }


    /**
     * Method populateHousingUnit populates a housing unit with humans or aliens.
     *
     * @param coordinate of type IntegerPair.
     * @param humans of type int.f bounds, the
     * @param purpleAlien of type boolean.
     * @param brownAlien of type boolean.
     * @throws InvalidInput If the coordinates are out otile is invalid, the tile is not a housing unit,
     *                      aliens are added to the MainCockpit, or the combination of occupants is not allowed.
     * @throws StorageCompartmentFullException If the housing unit is already full and cannot accommodate more occupants.
     */


    public void populateHousingUnit(IntegerPair coordinate , int humans ,boolean purpleAlien, boolean brownAlien) throws InvalidInput, StorageCompartmentFullException{

        int x = coordinate.getFirst();
        int y = coordinate.getSecond();

        if (x < 0 || x >= 10 || y < 0 || y >= 10 || ValidPlayerBoard[x][y] == -1) {
            throw new InvalidInput(x, y, "Invalid input: coordinates out of bounds or invalid tile.");
        }

        if (PlayerBoard[x][y].getComponent().getClass() != modularHousingUnit.class && PlayerBoard[x][y].getComponent().getClass() != MainCockpitComp.class){
            throw new InvalidInput("The following tile is not a modularHousingUnit");
        }

        if (PlayerBoard[x][y].getComponent().getClass() == MainCockpitComp.class && (purpleAlien || brownAlien)){
            throw new InvalidInput("Invalid input: aliens cannot be added to the MainCockpit");
        }

        if (PlayerBoard[x][y].getComponent().getAbility() == 2 && humans > 0){
            throw new StorageCompartmentFullException("The following StorageCompartment is Full: " + x + "," + y);
        }

        if (PlayerBoard[x][y].getComponent().getAbility() != 0 && (purpleAlien || brownAlien)){
            throw new InvalidInput("Invalid input: aliens cannot be added  if humans are already present");
        }

        if ((PlayerBoard[x][y].getComponent().isBrownAlien() || PlayerBoard[x][y].getComponent().isPurpleAlien()) && humans > 0){
            throw new InvalidInput("Invalid input: humans cannot be added if an alien is already present");
        }

        if ((purpleAlien && PlayerBoard[x][y].getComponent().isBrownAlien()) || (brownAlien && PlayerBoard[x][y].getComponent().isPurpleAlien())){
            throw new InvalidInput("Invalid input: there is already an alien of the other color present");
        }

        PlayerBoard[coordinate.getFirst()][coordinate.getSecond()].getComponent().initType(humans, purpleAlien, brownAlien);
    }

    public int sellCargo(boolean arrived){
        int totalSold=0;
        for(int i=0; i<Cargo.size(); i++){
            Tile currentTile = PlayerBoard[Cargo.get(i).getFirst()][Cargo.get(i).getSecond()];
            ArrayList<Goods> currGoods= currentTile.getComponent().getGoods();
            for(int j=0; j< currGoods.size(); j++){
                //dovrei asseganre un valore a goods senno è orrendo
                totalSold
            }
        }
    }

}