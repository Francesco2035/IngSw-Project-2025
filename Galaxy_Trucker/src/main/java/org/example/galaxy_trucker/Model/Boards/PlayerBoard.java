package org.example.galaxy_trucker.Model.Boards;


import org.example.galaxy_trucker.Exceptions.*;

import org.example.galaxy_trucker.Model.Boards.Actions.ComponentAction;
import org.example.galaxy_trucker.Model.Connectors.*;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.PlayerStates.PlayerState;
import org.example.galaxy_trucker.Model.Tiles.*;
import org.example.galaxy_trucker.Model.Goods.*;


import java.util.*;

public class PlayerBoard {

    private boolean broken;
    private int totalValue;
    private Tile[][] PlayerBoard;
    private int[][] ValidPlayerBoard;
    private int damage;
    private int exposedConnectors;
    private int[] shield;
    private int numHumans = 0;
    private int EnginePower = 0;
    private double PlasmaDrillsPower = 0;
    private int Energy = 0;
    private int lv;

    private boolean valid;

    private boolean purpleAlien;
    private boolean brownAlien;

    private ArrayList<Goods> BufferGoods;

    private ArrayList<HousingUnit> HousingUnits;
    HashMap<Integer, ArrayList<IntegerPair>> shipSection;

    private ArrayList<HotWaterHeater> HotWaterHeaters;
    private ArrayList<PlasmaDrill> PlasmaDrills;
    private ArrayList<AlienAddons> AlienAddons;
    private ArrayList<Storage> Storages;
    private ArrayList<ShieldGenerator> ShieldGenerators;
    private ArrayList<PowerCenter> PowerCenters;


    private HashMap<Integer, ArrayList<IntegerPair>> storedGoods;


    private ArrayList<Tile> Buffer;
    private ArrayList<Goods> Rewards;

    public PlayerBoard(int lv) {
        this.lv = lv;
        this.broken = false;
        this.damage = 0;
        this.shield = new int[4];
        this.Buffer = new ArrayList<>();
        this.totalValue = 0;


        this.Rewards = new ArrayList<>();


        this.valid = true;

        this.purpleAlien = false;
        this.brownAlien = false;


        this.storedGoods = new HashMap<>();

        this.exposedConnectors = 0;

        this.BufferGoods = new ArrayList<>();

        this.ValidPlayerBoard = new int[10][10];


        this.HousingUnits = new ArrayList<>();
        this.HotWaterHeaters= new ArrayList<>();
        this.PlasmaDrills= new ArrayList<>();
        this.AlienAddons= new ArrayList<>();
        this.Storages= new ArrayList<>();
        this.ShieldGenerators= new ArrayList<>();
        this.PowerCenters= new ArrayList<>();


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
                    PlayerBoard[x][y] =  new Tile(new SpaceVoid() , NONE.INSTANCE , NONE.INSTANCE,NONE.INSTANCE, NONE.INSTANCE);
                }
                else {
                    PlayerBoard[x][y] = null;
                }

            }
        }
        this.PlayerBoard[6][6] = new Tile(new MainCockpitComp(),UNIVERSAL.INSTANCE, UNIVERSAL.INSTANCE,UNIVERSAL.INSTANCE,UNIVERSAL.INSTANCE);
        PlayerBoard[6][6].getComponent().insert(this,6,6);
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

    public Tile getTileFromBuffer(int i){
        if (i > Buffer.size()) {
            throw new InvalidInput("This position in the Buffer does not exist");
        }
        if (Buffer.isEmpty()) {
            throw new InvalidInput("Buffer is empty");
        }
        return Buffer.remove(i);
    }

// setter and getters for lists
    public void setBufferGoods(ArrayList<Goods> bufferGoods) {
        BufferGoods = bufferGoods;
    }


    public void setHousingUnits(ArrayList<HousingUnit> housingUnits) {
        HousingUnits = housingUnits;
    }


    public void setHotWaterHeaters(ArrayList<HotWaterHeater> hotWaterHeaters) {
        HotWaterHeaters = hotWaterHeaters;
    }


    public void setPlasmaDrills(ArrayList<PlasmaDrill> plasmaDrills) {
        PlasmaDrills = plasmaDrills;
    }


    public void setStorages(ArrayList<Storage> storages) {
        Storages = storages;
    }


    public void setAlienAddons(ArrayList<AlienAddons> alienAddons) {
        AlienAddons = alienAddons;
    }


    public void setShieldGenerators(ArrayList<ShieldGenerator> shieldGenerators) {
        ShieldGenerators = shieldGenerators;
    }


    public void setPowerCenters(ArrayList<PowerCenter> powerCenters) {
        PowerCenters = powerCenters;
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

    public HashMap<Integer, ArrayList<IntegerPair>> getStoredGoods(){
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
            throw new InvalidInput(x, y, "Invalid input: coordinates out of bounds or invalid tile: "+ x + " "+y);
        }
        return this.PlayerBoard[x][y];
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
        tile.getComponent().insert(this,x,y);
        ValidPlayerBoard[x][y] = 1;

    }


    /**
     * Method checkConnection checks whether two connectors can be soldered.
     *
     * @param t1 of type Connector .
     * @param t2 of type Connector .
     * @return true if the connection is legal, false otherwise.
     */
    public boolean checkConnection(Connectors t1, Connectors t2 ){

        if (!t1.checkLegal(t2)){
            System.out.println("INVALID CONNECTION "+ t1.getClass() + " " + t2.getClass());
            valid = false;
        }
        return t1.checkAdjacent(t2);
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
        PlayerBoard[x][y].getComponent().remove(this);
        PlayerBoard[x][y] = new Tile(new SpaceVoid() ,NONE.INSTANCE, NONE.INSTANCE, NONE.INSTANCE);
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
        valid = true;
        ArrayList<IntegerPair> visitedPositions = new ArrayList<>();

        findPaths(r,c,visitedPositions);

        if(!valid){
            return false;
        }

        if (PathNotVisited(visitedPositions)){
            System.out.println("percorso non visitato");
            return false;
        }

        else {
            if (checkIllegal(visitedPositions)){ //secondo me si può togliere
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

        if (!valid || visited.contains(new IntegerPair(r, c))||r < 0 || c < 0 || r > 9 || c > 9 || this.ValidPlayerBoard[r][c] == -1 ) {
            return;
        }
        visited.add(new IntegerPair(r, c));
        System.out.println(r + " " + c);


        if (valid && c - 1 >=0 && ValidPlayerBoard[r][c-1] == 1 && checkConnection(getTile(r,c).getConnectors().get(0),getTile(r, c -1).getConnectors().get(2))) {

            findPaths(r, c - 1, visited);
        }



        if (valid && r - 1 >=0 && ValidPlayerBoard[r-1][c] == 1 && checkConnection(getTile(r,c).getConnectors().get(1),getTile(r-1, c ).getConnectors().get(3))){

            findPaths(r -1,c ,visited);
        }



        if (valid && c + 1 <= 9 && ValidPlayerBoard[r][c+1] == 1 && checkConnection(getTile(r,c).getConnectors().get(2),getTile(r, c + 1).getConnectors().get(0))){

            findPaths(r,c + 1 ,visited);
        }


        if (valid && r + 1 <= 9 && ValidPlayerBoard[r+1][c] == 1 && checkConnection(getTile(r,c).getConnectors().get(3),getTile(r + 1, c ).getConnectors().get(1))){

            findPaths(r +1,c ,visited);
        }

    }


    /**
     * Method destroy destroys the designated tile adding a SpaceVoid tile in its place, possibly updating the class attributes.
     *
     * @param x of type int - x coordinate.
     * @param y of type int - y coordinate.
     */
    public void destroy(int x, int y){

        PlayerBoard[x][y].getComponent().remove(this);
        damage++;
        PlayerBoard[x][y] = new Tile(new SpaceVoid(),NONE.INSTANCE, NONE.INSTANCE, NONE.INSTANCE, NONE.INSTANCE);
        ValidPlayerBoard[x][y] = 0;
        updateStoredGoods();
    }

    public void updateStoredGoods(){

        for (Integer Goods : storedGoods.keySet()){
                storedGoods.get(Goods).removeIf(pair -> ValidPlayerBoard[pair.getFirst()][pair.getSecond()] != 1);
        }

    }

    /**
     * Method chosePlayerBoard returns the selected chunk and calculates the actual damage suffered.
     *
     * @param input of type IntegerPair - the chunk selected.
     */
    public ArrayList<IntegerPair> choosePlayerBoard(IntegerPair input){
        //questo metodo non ha molto senso
        for (Integer i : this.shipSection.keySet()){
            if (this.shipSection.get(i).contains(input)){
                return this.shipSection.get(i);
            }
        }
        throw new InvalidInput("Invalid input: selected chunk doesn't exists!");
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
                        PlayerBoard[x][y].getComponent().remove(this);
                        PlayerBoard[x][y] = new Tile(new SpaceVoid(),NONE.INSTANCE, NONE.INSTANCE, NONE.INSTANCE, NONE.INSTANCE);
                        ValidPlayerBoard[x][y] = 0;
                        damage++;
                    }
                }
            }
        }
        updateAttributes(newPlayerBoard.getFirst().getFirst(),newPlayerBoard.getFirst().getSecond());
        broken = false;

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

        ArrayList<IntegerPair> visitedPositions = new ArrayList<>();
        updateBoardAttributes(x,y, visitedPositions);
        updateGloabalAttributes(visitedPositions);
        updateStoredGoods();

    }

    public void updateGloabalAttributes(ArrayList<IntegerPair> board){
        for (IntegerPair pair : board)     {
            int r = pair.getFirst();
            int c = pair.getSecond();
            PlayerBoard[r][c].controlDirections(this, r, c);
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
    public void updateBoardAttributes(int r, int c,ArrayList<IntegerPair> visited){
        if (visited.contains(new IntegerPair(r, c))||r < 0 || c < 0 || r > 9 || c > 9 || this.ValidPlayerBoard[r][c] != 1) {
            return;
        }
        System.out.println("update: "+r + " " + c);


        visited.add(new IntegerPair(r, c));
        System.out.println(r + " " + c);


        if (c - 1 >= 0 && ValidPlayerBoard[r][c-1] == 1 && checkConnection(getTile(r,c).getConnectors().get(0),getTile(r, c -1).getConnectors().get(2))) {
            updateBoardAttributes(r, c - 1, visited);
        }
        else if (getTile(r,c).getConnectors().get(0).isExposed() && (c - 1 < 0 || ValidPlayerBoard[r][c - 1] != 1)) {
            exposedConnectors++;
        }

        if (r - 1 >= 0 && ValidPlayerBoard[r-1][c] == 1 && checkConnection(getTile(r,c).getConnectors().get(1),getTile(r-1, c ).getConnectors().get(3))){
            updateBoardAttributes(r -1,c ,visited);
        }
        else if (getTile(r,c).getConnectors().get(1).isExposed() && (r - 1 < 0 || ValidPlayerBoard[r - 1][c] != 1) ) {
            exposedConnectors++;
        }

        if (c + 1 <= 9 && ValidPlayerBoard[r][c+1] == 1&& checkConnection(getTile(r,c).getConnectors().get(2),getTile(r, c + 1).getConnectors().get(0))){
            updateBoardAttributes(r,c + 1 ,visited);
        }
        else if (getTile(r,c).getConnectors().get(2).isExposed() && (c + 1 > 9 || ValidPlayerBoard[r][c + 1] != 1)) {
            exposedConnectors++;
        }

        if (r + 1 <= 9 &&ValidPlayerBoard[r+1][c] == 1 && checkConnection(getTile(r,c).getConnectors().get(3),getTile(r + 1, c ).getConnectors().get(1))){
            updateBoardAttributes(r +1,c ,visited);
        }
        else if (getTile(r,c).getConnectors().get(3).isExposed() && (r + 1 < 9 || ValidPlayerBoard[r + 1][c] != 1)) {
            exposedConnectors++;
        }

    }


    /**
     * Method handleAttack finds all possible chunks after a tile is destroyed by calling findPaths for all 4
     * direction of the destroyed tile.
     *
     * @param x of type int - x coordinate of the destroyed Tile.
     * @param y of type int - y coordinate of the destroyed Tile.
     */
    public void handleAttack(int x, int y){

        ArrayList<IntegerPair> visitedPositions = new ArrayList<>();
        int i = 0;
        this.shipSection = new HashMap<>();

        if (ValidPlayerBoard[x-1][y] == 1){

            findPaths(x-1, y, visitedPositions);

            this.shipSection.put(i, visitedPositions);
            i++;

        }

        if (ValidPlayerBoard[x][y-1] == 1 ){
            visitedPositions = new ArrayList<>();
            findPaths(x, y-1, visitedPositions);
            if (!visitedPositions.contains(new IntegerPair(x-1,y))) {

                this.shipSection.put(i, visitedPositions);
                i++;
            }

        }

        if (ValidPlayerBoard[x+1][y] == 1 ){
            visitedPositions = new ArrayList<>();
            findPaths(x+1, y, visitedPositions);
            if (!visitedPositions.contains(new IntegerPair(x-1,y)) && !visitedPositions.contains(new IntegerPair(x,y -1))) {

                this.shipSection.put(i, visitedPositions);
                i++;
            }

        }

        if (ValidPlayerBoard[x][y + 1] == 1  ){
            visitedPositions = new ArrayList<>();
            findPaths(x, y+1, visitedPositions);
            if (!visitedPositions.contains(new IntegerPair(x-1,y)) && !visitedPositions.contains(new IntegerPair(x,y -1)) && !visitedPositions.contains(new IntegerPair(x+1,y)) ) {

                this.shipSection.put(i, visitedPositions);

            }

        }
        if (shipSection.size() != 1){
            broken = true;
        }

    }




    public boolean getPurpleAlien(){
        return this.purpleAlien;
    }


    public boolean getBrownAlien(){
        return this.brownAlien;
    }


    public void setBrownAlien(boolean brownAlien) {
        this.brownAlien = brownAlien;
    }


    public void setPurpleAlien(boolean purpleAlien) {
        this.purpleAlien = purpleAlien;
    }


    public ArrayList<Goods> getBufferGoods() {
        return BufferGoods;
    }


    public ArrayList<HousingUnit> getHousingUnits() {
        return HousingUnits;
    }


    public ArrayList<HotWaterHeater> getHotWaterHeaters() {
        return HotWaterHeaters;
    }


    public ArrayList<PlasmaDrill> getPlasmaDrills() {
        return PlasmaDrills;
    }


    public ArrayList<AlienAddons> getAlienAddons() {
        return AlienAddons;
    }


    public ArrayList<Storage> getStorages() {
        return Storages;
    }


    public ArrayList<ShieldGenerator> getShieldGenerators() {
        return ShieldGenerators;
    }


    public ArrayList<PowerCenter> getPowerCenters() {
        return PowerCenters;
    }


    public int getEnginePower() {
        if (brownAlien){
            return EnginePower +2;
        }
        return EnginePower;
    }

    public void setEnginePower(int enginePower) {
        EnginePower += enginePower;
    }


    public double getPlasmaDrillsPower() {
        if(purpleAlien){
            return PlasmaDrillsPower + 2;
        }
        return PlasmaDrillsPower;
    }


    public void setPlasmaDrillsPower(double plasmaDrillsPower) {
        PlasmaDrillsPower += plasmaDrillsPower;
    }


    public int getNumHumans() {
        return numHumans;
    }


    public void setNumHumans(int numHumans) {
        this.numHumans += numHumans;
    }


    public int getEnergy() {
        return Energy;
    }


    public void setEnergy(int energy) {
        Energy += energy;
    }


    public void performAction(Component component, ComponentAction action, PlayerState state) {
            component.accept(action, state);
    }


    public PlayerBoard clone(){
        PlayerBoard clonedPlayerBoard = new PlayerBoard(lv);
        clonedPlayerBoard.broken = broken;
        clonedPlayerBoard.purpleAlien = purpleAlien;
        clonedPlayerBoard.brownAlien = brownAlien;
        clonedPlayerBoard.totalValue = totalValue;
        clonedPlayerBoard.damage = damage;
        clonedPlayerBoard.numHumans = numHumans;
        clonedPlayerBoard.exposedConnectors = exposedConnectors;
        clonedPlayerBoard.EnginePower = EnginePower;
        clonedPlayerBoard.PlasmaDrillsPower = PlasmaDrillsPower;
        clonedPlayerBoard.Energy = Energy;
        clonedPlayerBoard.lv = lv;
        clonedPlayerBoard.valid = valid;
        clonedPlayerBoard.HousingUnits = new ArrayList<>();
        clonedPlayerBoard.HotWaterHeaters= new ArrayList<>();
        clonedPlayerBoard.PlasmaDrills= new ArrayList<>();
        clonedPlayerBoard.AlienAddons= new ArrayList<>();
        clonedPlayerBoard.Storages= new ArrayList<>();
        clonedPlayerBoard.ShieldGenerators= new ArrayList<>();
        clonedPlayerBoard.PowerCenters= new ArrayList<>();
        clonedPlayerBoard.Rewards = new ArrayList<>(this.Rewards);

        clonedPlayerBoard.PlayerBoard = new Tile[PlayerBoard.length][PlayerBoard[0].length];
        for (int i = 0; i < PlayerBoard.length; i++) {
            for (int j = 0; j < PlayerBoard[i].length; j++) {
                Tile tile = PlayerBoard[i][j];
                clonedPlayerBoard.PlayerBoard[i][j] = tile != null ? tile.clone() : null;
                if (tile != null) {
                    tile.getComponent().insert(clonedPlayerBoard, i, j);
                }
            }
        }

        clonedPlayerBoard.ValidPlayerBoard = new int[ValidPlayerBoard.length][ValidPlayerBoard[0].length];
        for (int i = 0; i < ValidPlayerBoard.length; i++) {
            clonedPlayerBoard.ValidPlayerBoard[i] = Arrays.copyOf(ValidPlayerBoard[i], ValidPlayerBoard[i].length);
        }

        clonedPlayerBoard.shield = Arrays.copyOf(shield, shield.length);

        return clonedPlayerBoard;

    }


    public boolean isBroken() {
        return broken;
    }


    public int getLv() {
        return lv;
    }


    public boolean isValid() {
        return valid;
    }


    public boolean isPurpleAlien() {
        return purpleAlien;
    }


    public boolean isBrownAlien() {
        return brownAlien;
    }


    public ArrayList<Goods> getRewards(){
        return Rewards;
    }

    public Goods getFromRewards(int i){
        if (i > Rewards.size()) {
            throw new InvalidInput("This position in the Rewards does not exist");
        }
        if (Rewards.isEmpty()) {
            throw new InvalidInput("Rewards is empty");
        }
        return Rewards.remove(i);
    }

    public void AddGoodInBuffer(Goods good){
        BufferGoods.add(good);
    }

    /**
     * Method pullFromBufferGoods pull the good in position i of the BufferGoods.
     *
     * @param i of type int.
     * @return the good in order to be added to the storageCompartment.
     * @throws InvalidInput If the specified index is out of bounds or if the buffer is empty.
     */
    public Goods pullFromBufferGoods(int i) throws InvalidInput{
        if (i > BufferGoods.size()) {
            throw new InvalidInput("This position in the BufferGoods does not exist");
        }
        if (BufferGoods.isEmpty()) {
            throw new InvalidInput("BufferGoods is empty");
        }
        return BufferGoods.remove(i);
    }


    public boolean getBroken(){
        return broken;
    }


}