package org.example.galaxy_trucker.Model.Boards;

import org.example.galaxy_trucker.Controller.Listeners.RewardsListener;
import org.example.galaxy_trucker.ClientServer.Messages.PBInfoEvent;
import org.example.galaxy_trucker.ClientServer.Messages.PlayerBoardEvents.RewardsEvent;
import org.example.galaxy_trucker.ClientServer.Messages.PlayerBoardEvents.TileEvent;
import org.example.galaxy_trucker.Controller.Listeners.PlayerBoardListener;
import org.example.galaxy_trucker.Exceptions.*;
import org.example.galaxy_trucker.Model.Boards.Actions.ComponentAction;
import org.example.galaxy_trucker.Model.Connectors.*;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.PlayerStates.PlayerState;
import org.example.galaxy_trucker.Model.Tiles.*;
import org.example.galaxy_trucker.Model.Goods.*;
import java.util.*;


/**
 * The PlayerBoard class represents a player's spaceship board in the Galaxy Trucker game.
 * This class manages the grid-based board where players place tiles to build their spaceship,
 * handles connections between tiles, manages various components (engines, drills, storage, etc.),
 * tracks damage and shields, and validates the board's structural integrity.
 *
 * <p>The board is represented as a 10x10 grid where tiles can be placed according to specific
 * rules and constraints. The class also handles the ship's attributes like crew, energy,
 * stored goods, and various alien bonuses.</p>
 *
 * <p>Key responsibilities include:</p>
 * <ul>
 *   <li>Tile placement and removal management</li>
 *   <li>Connection validation between adjacent tiles</li>
 *   <li>Board integrity checking using depth-first search algorithms</li>
 *   <li>Component management (engines, drills, shields, etc.)</li>
 *   <li>Damage tracking and handling</li>
 *   <li>Goods storage and retrieval</li>
 *   <li>Event notification to listeners</li>
 * </ul>
 *
 * @author Francesco Rausa
 * @version 1.0
 * @since 1.0
 */

public class PlayerBoard {

    /**
     * Listener for player board events and changes.
     * Receives notifications when the board state changes.
     */
    PlayerBoardListener listener;

    /**
     * Listener for rewards-related events.
     * Receives notifications when the rewards list is modified.
     */
    private RewardsListener rewardsListener;

    /**
     * 2D array representing the game board with placed tiles.
     * Each position can contain a Tile object or be null if empty.
     * The board dimensions are typically 10x10.
     */
    private Tile[][] PlayerBoard;

    /**
     * List of goods currently stored in the buffer area.
     * These goods are available for use but not yet placed or consumed.
     */
    private ArrayList<Goods> BufferGoods;

    /**
     * Map storing goods organized by their value/type.
     * Key: Integer representing the goods value or category
     * Value: List of coordinate pairs where these goods are located
     */
    private HashMap<Integer, ArrayList<IntegerPair>> storedGoods;

    /**
     * List of tiles currently in the player's buffer.
     * These tiles are available for placement on the board.
     */
    private ArrayList<Tile> Buffer;

    /**
     * List of rewards that the player has earned.
     * These can be goods, bonuses, or other beneficial items.
     */
    private ArrayList<Goods> Rewards;

    /**
     * Flag indicating whether the player board is in a broken state.
     * A broken board typically occurs when ship sections become disconnected.
     */
    private boolean broken;

    /**
     * 2D array tracking valid positions on the player board.
     * Values: 1 = valid/occupied position, 0 = invalid/empty position
     * Used for pathfinding and connectivity checks.
     */
    private int[][] ValidPlayerBoard;

    /**
     * 2D array marking tiles that should be removed from the player board.
     * Used to track damaged or destroyed tiles that need to be cleared.
     */
    private final int[][] toRemovePB;

    /**
     * Current level of the player board.
     * May affect available features, difficulty, or board size.
     */
    private int lv;

    /**
     * Flag indicating whether the current board configuration is valid.
     * Invalid boards may have rule violations or connectivity issues.
     */
    private boolean valid;

    /**
     * Current amount of credits/currency the player possesses.
     * Used for purchasing items, tiles, or other game elements.
     */
    private int credits;

    /**
     * List of housing units that are properly connected to the ship's systems.
     * Only connected housing units contribute to crew capacity and bonuses.
     */
    private ArrayList<HousingUnit> connectedHousingUnits;

    /**
     * Complete list of all housing units on the player board.
     * Includes both connected and disconnected units.
     */
    private ArrayList<HousingUnit> HousingUnits;

    /**
     * Map of ship sections organized by section ID.
     * Key: Integer section identifier
     * Value: List of coordinate pairs belonging to that section
     * Used for tracking ship integrity after attacks.
     */
    HashMap<Integer, ArrayList<IntegerPair>> shipSection;

    /**
     * List of all hot water heater components on the player board.
     * These components typically provide crew comfort bonuses.
     */
    private ArrayList<HotWaterHeater> HotWaterHeaters;

    /**
     * List of all plasma drill components on the player board.
     * These components are used for mining and resource extraction.
     */
    private ArrayList<PlasmaDrill> PlasmaDrills;

    /**
     * List of all alien addon components on the player board.
     * These special components provide unique alien-related bonuses.
     */
    private ArrayList<AlienAddons> AlienAddons;

    /**
     * List of all storage components on the player board.
     * These components increase cargo capacity for goods.
     */
    private ArrayList<Storage> Storages;

    /**
     * List of all shield generator components on the player board.
     * These components provide defensive capabilities against attacks.
     */
    private ArrayList<ShieldGenerator> ShieldGenerators;

    /**
     * List of all power center components on the player board.
     * These components generate energy for ship systems.
     */
    private ArrayList<PowerCenter> PowerCenters;

    /**
     * Current amount of damage sustained by the player board.
     * Higher damage values negatively impact final scoring.
     */
    private int damage;

    /**
     * Number of exposed connectors on the player board.
     * Exposed connectors are connection points not linked to adjacent tiles.
     * Higher numbers may indicate incomplete or damaged sections.
     */
    private int exposedConnectors;

    /**
     * Array representing shield strength in different areas or types.
     * Each element corresponds to a specific shield system or zone.
     */
    private int[] shield;

    /**
     * Current number of human crew members on the ship.
     * Initialized to 0, increased by housing units and crew additions.
     */
    private int numHumans = 0;

    /**
     * Current engine power rating of the ship.
     * Initialized to 0, increased by engine components.
     * Brown alien provides +2 bonus when active.
     */
    private int EnginePower = 0;

    /**
     * Current plasma drill power rating of the ship.
     * Initialized to 0.0, increased by plasma drill components.
     * Purple alien provides +2 bonus when active.
     */
    private double PlasmaDrillsPower = 0;

    /**
     * Current energy level available to the ship's systems.
     * Initialized to 0, generated by power centers and consumed by various actions.
     */
    private int Energy = 0;

    /**
     * Flag indicating whether a purple alien crew member is present.
     * Purple aliens provide bonuses to plasma drill power and crew count.
     */
    private boolean purpleAlien;

    /**
     * Flag indicating whether a brown alien crew member is present.
     * Brown aliens provide bonuses to engine power and crew count.
     */
    private boolean brownAlien;

    /**
     * Total calculated value of the player board.
     * Used for scoring and evaluation purposes.
     * Initialized to 0 and updated based on components and achievements.
     */
    private int totalValue = 0;



    /**
     * Constructs a new PlayerBoard with the specified level configuration.
     * Initializes the board grid, validity matrix, and all component lists.
     * Sets up the valid placement areas based on the level (different ship sizes).
     *
     * @param lv the level of the player board (affects the valid placement area)
     *           Level 2 has a different valid area configuration than other levels
     */
    public PlayerBoard(int lv) {
        this.lv = lv;
        this.broken = false;
        this.damage = 0;
        this.credits = 0;
        this.shield = new int[4];
        this.Buffer = new ArrayList<>();



        this.Rewards = new ArrayList<>();
        this.valid = true;

        this.purpleAlien = false;
        this.brownAlien = false;


        this.storedGoods = new HashMap<>();

        this.exposedConnectors = 0;

        this.BufferGoods = new ArrayList<>();

        this.ValidPlayerBoard = new int[10][10];
        this.toRemovePB = new int[10][10];
        this.connectedHousingUnits = new ArrayList<>();


        this.HousingUnits = new ArrayList<>();
        this.HotWaterHeaters= new ArrayList<>();
        this.PlasmaDrills= new ArrayList<>();
        this.AlienAddons= new ArrayList<>();
        this.Storages= new ArrayList<>();
        this.ShieldGenerators= new ArrayList<>();
        this.PowerCenters= new ArrayList<>();
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                toRemovePB[x][y] = 0;
            }
        }

        if (lv == 2) {
            for (int x = 0; x < 10; x++) {
                for (int y = 0; y < 10; y++) {
                    if (x < 4 || y < 3 ||
                            (x == 4 && (y == 3 || y == 4 || y == 6 || y == 8 || y == 9))
                            ||(x == 5 && (y == 3 || y== 9))
                            || (x == 8 && y == 6) || x ==9) {
                        ValidPlayerBoard[x][y] = -1;
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
    }


    /**
     * Adds the specified value from the cargo to the total value of the player board and updates the info display.
     *
     * @param i the value to add to the total value
     */
    public void setTotalValue(int i){
        this.totalValue += i;
        updateInfo();
    }


    /**
     * Retrieves the current total value of the player board.
     *
     * @return the total value accumulated on this player board
     */
    public int getTotalValue(){
        return this.totalValue;
    }


    /**
     * Inserts a tile into the buffer for later placement on the board.
     * The buffer can hold a maximum of 2 tiles.
     *
     * @param t the tile to be inserted into the buffer
     * @throws IllegalStateException if the buffer is already full (contains 2 tiles)
     * @throws InvalidInput if the tile is null (hand is empty)
     */
    public void insertBuffer(Tile t) throws IllegalStateException{
        if (Buffer.size() >= 2) {
            throw new IllegalStateException("Buffer is full");
        }
        if(t == null) throw new InvalidInput("Invalid input: your hand is empty");
        t.setChosen();
        sendUpdates(new TileEvent(t.getId(), 3, 8 + Buffer.size() , null, 0, false, false, 0, 0, t.getConnectors()));
        Buffer.add(t);
    }

    /**
     * Retrieves and removes a tile from the buffer at the specified index.
     *
     * @param i the index of the tile to retrieve from the buffer
     * @return the tile at the specified index
     * @throws InvalidInput if the index is out of bounds or the buffer is empty
     */
    public Tile getTileFromBuffer(int i) {
        if (i > Buffer.size()) {
            throw new InvalidInput("This position in the Buffer does not exist");
        }
        if (Buffer.isEmpty()) {
            throw new InvalidInput("Buffer is empty");
        }

        sendUpdates(new TileEvent(158, 3, 8 + i , null, 0, false, false, 0, 0, Buffer.get(i).getConnectors()));
        return Buffer.remove(i);
    }


    /**
     * Sets the rewards listener that will be notified when rewards change.
     *
     * @param listener the RewardsListener to be set
     */
    public void setRewardsListener(RewardsListener listener){
        this.rewardsListener = listener;
    }


    /**
     * Sets the buffer goods list.
     *
     * @param bufferGoods the ArrayList of Goods to set as buffer goods
     */
    public void setBufferGoods(ArrayList<Goods> bufferGoods) {
        BufferGoods = bufferGoods;
    }

    /**
     * Sets the housing units list.
     *
     * @param housingUnits the ArrayList of HousingUnit to set
     */
    public void setHousingUnits(ArrayList<HousingUnit> housingUnits) {
        HousingUnits = housingUnits;
    }

    /**
     * Sets the hot water heaters list.
     *
     * @param hotWaterHeaters the ArrayList of HotWaterHeater to set
     */
    public void setHotWaterHeaters(ArrayList<HotWaterHeater> hotWaterHeaters) {
        HotWaterHeaters = hotWaterHeaters;
    }

    /**
     * Sets the plasma drills list.
     *
     * @param plasmaDrills the ArrayList of PlasmaDrill to set
     */
    public void setPlasmaDrills(ArrayList<PlasmaDrill> plasmaDrills) {
        PlasmaDrills = plasmaDrills;
    }

    /**
     * Sets the storages list.
     *
     * @param storages the ArrayList of Storage to set
     */
    public void setStorages(ArrayList<Storage> storages) {
        Storages = storages;
    }

    /**
     * Sets the alien addons list.
     *
     * @param alienAddons the ArrayList of AlienAddons to set
     */
    public void setAlienAddons(ArrayList<AlienAddons> alienAddons) {
        AlienAddons = alienAddons;
    }

    /**
     * Sets the shield generators list.
     *
     * @param shieldGenerators the ArrayList of ShieldGenerator to set
     */
    public void setShieldGenerators(ArrayList<ShieldGenerator> shieldGenerators) {
        ShieldGenerators = shieldGenerators;
    }

    /**
     * Sets the power centers list.
     *
     * @param powerCenters the ArrayList of PowerCenter to set
     */
    public void setPowerCenters(ArrayList<PowerCenter> powerCenters) {
        PowerCenters = powerCenters;
    }



    /**
     * Retrieves the number of exposed connectors on the player board.
     * Exposed connectors are those that are not connected to adjacent tiles
     * and can cause damage during meteor storms.
     *
     * @return the number of exposed connectors
     */
    public int getExposedConnectors(){
        return exposedConnectors;
    }



    /**
     * Retrieves the shield status of the player board.
     * The shield is represented as an array of size 4, where each index corresponds to a direction:
     * <ul>
     *   <li>Index 0 - Left</li>
     *   <li>Index 1 - Top</li>
     *   <li>Index 2 - Right</li>
     *   <li>Index 3 - Bottom</li>
     * </ul>
     * A value of 0 indicates no protection, while a non-zero value means the side is protected.
     *
     * @return an integer array of size 4 representing the shield status in each direction
     */
    public int[] getShield(){
        return shield;
    }


    /**
     * Retrieves the player's board, which consists of a grid of tiles.
     *
     * @return a 2D array of Tile objects representing the player's board
     */
    public Tile[][] getPlayerBoard(){
        return this.PlayerBoard;
    }


    /**
     * Retrieves the validity matrix of the player board.
     * Each cell indicates whether the position is valid for placement:
     * <ul>
     *   <li>-1: Invalid position (outside ship boundaries)</li>
     *   <li>0: Valid empty position</li>
     *   <li>1: Position occupied by a tile</li>
     * </ul>
     *
     * @return a 2D array of integers representing the validity of each position
     */
    public int[][] getValidPlayerBoard(){
        return this.ValidPlayerBoard;
    }




    /**
     * Retrieves the current damage value of the player board.
     * Damage is accumulated from various sources like meteor impacts,
     * combat damage, and structural failures.
     *
     * @return the amount of damage sustained by the ship
     */
    public int getDamage(){
        return damage;
    }


    /**
     * Retrieves the stored goods organized by their value.
     * The HashMap maps goods values to lists of positions where those goods are stored.
     *
     * @return a HashMap where keys are goods values and values are lists of storage positions
     */
    public HashMap<Integer, ArrayList<IntegerPair>> getStoredGoods(){
//        System.out.println("getStoredGoods "+ storedGoods.size());
//        for (Integer i : storedGoods.keySet()){
//            //System.out.println("there are "+ storedGoods.get(i).size() + " goods of value "+i);
//            for (IntegerPair pair : storedGoods.get(i)){
//                System.out.println("Value "+i+ "| |"+ pair.getFirst() + " " + pair.getSecond());
//            }
//        }
        return storedGoods;
    }


    /**
     * Retrieves the tile located at the specified coordinates on the player board.
     *
     * @param x the x-coordinate of the tile
     * @param y the y-coordinate of the tile
     * @return the Tile object at the given coordinates
     * @throws InvalidInput if the coordinates are out of bounds or point to an invalid tile
     */
    public Tile getTile(int x, int y) throws InvalidInput {
        if (x < 0 || x >= 10 || y < 0 || y >= 10 || ValidPlayerBoard[x][y] != 1) {
            throw new InvalidInput(x, y, "Invalid input: coordinates out of bounds or invalid tile: "+ x + " "+y);
        }
        return this.PlayerBoard[x][y];
    }



    /**
     * Inserts a tile into the player board at the specified coordinates.
     * Performs validation checks if the check parameter is true, including:
     * - Null tile check
     * - Bounds checking
     * - Position availability
     * - Adjacent tile requirement (except for position 6,6)
     *
     * @param tile the tile to be placed
     * @param x the x-coordinate where the tile should be placed
     * @param y the y-coordinate where the tile should be placed
     * @param check whether to perform validation checks
     * @throws NullPointerException if the tile is null and check is true
     * @throws InvalidInput if the coordinates are invalid, out of bounds, or position requirements are not met
     */
    public void insertTile(Tile tile, int x, int y, boolean check) throws NullPointerException, InvalidInput {
        if (check){
            if (tile == null) {
                throw new NullPointerException("Tile cannot be null.");
            }

            if (x < 0 || x >= PlayerBoard.length || y < 0 || y >= PlayerBoard[0].length || ValidPlayerBoard[x][y] == -1) {
                throw new InvalidInput(x, y, "Invalid input: coordinates out of bounds or invalid tile.");
            }

            if (ValidPlayerBoard[x][y] != 0) {
                throw new InvalidInput(x,y, "Invalid input : invalid position, already occupied or space void");

            }

            if (!(x== 6 && y == 6)){
                if ((ValidPlayerBoard[x-1][y] != 1) && (ValidPlayerBoard[x][y-1] != 1) &&
                        (x >= 9 || (ValidPlayerBoard[x+1][y] != 1)) && (y >= 9 || (ValidPlayerBoard[x][y+1] != 1))) {
                    throw new InvalidInput(x,y, "Invalid input : invalid position, there aren't tiles nearby!");
                }

            }
        }

        //System.out.println(x + " " +y);

        this.PlayerBoard[x][y] = tile;
        tile.getComponent().setTile(tile);
        tile.setPlayerBoard(this);
        tile.setX(x);
        tile.setY(y);
        tile.getComponent().insert(this,x,y);
        ValidPlayerBoard[x][y] = 1;

    }


    /**
     * Checks whether two connectors can be legally connected.
     * Validates both the legal connection type and adjacency requirements.
     * Sets the board validity to false if an illegal connection is detected.
     *
     * @param t1 the first connector
     * @param t2 the second connector
     * @return true if the connection is valid and adjacent, false otherwise
     */
    public boolean checkConnection(Connectors t1, Connectors t2){

        if (!t1.checkLegal(t2)){
            System.out.println("INVALID CONNECTION "+ t1.getClass() + " " + t2.getClass());
            valid = false;
        }
        return t1.checkAdjacent(t2);
    }


    /**
     * Checks whether two connectors can be legally connected, with additional tracking
     * for tiles that should be removed due to invalid connections.
     * This version is used during board validation to mark problematic tiles in order to create
     * the default command.
     *
     * @param t1 the first connector
     * @param t2 the second connector
     * @param x the x-coordinate of the second connector's tile
     * @param y the y-coordinate of the second connector's tile
     * @param visited list of already visited positions
     * @param fromX the x-coordinate of the first connector's tile
     * @param fromY the y-coordinate of the first connector's tile
     * @return true if the connection is valid and adjacent, false otherwise
     */
    public boolean checkConnection(Connectors t1, Connectors t2, int x, int y, ArrayList<IntegerPair> visited, int fromX, int fromY){

        if (!t1.checkLegal(t2)){
            System.out.println("INVALID CONNECTION "+ t1.getClass() + " " + t2.getClass());
            valid = false;
            if (visited.contains(new IntegerPair(x, y))){
                toRemovePB[fromX][fromY] = -2;
            }
            else{
                toRemovePB[x][y] = -2;
            }
        }
        return t1.checkAdjacent(t2);
    }



    /**
     * Checks if there are any tiles that have not been reached by the pathfinding algorithm.
     * Marks unreachable tiles for removal in the toRemovePB matrix.
     *
     * @param visited list of positions that have been visited during pathfinding
     * @return true if there is at least one unreachable tile, false if all tiles are reachable
     */
    public boolean PathNotVisited(ArrayList<IntegerPair> visited){
        boolean findOne = false;
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {

                if (ValidPlayerBoard[x][y] == 1 && !visited.contains(new IntegerPair(x,y))){
                    toRemovePB[x][y] = -2;
                    findOne = true;
                }

            }

        }
        return findOne;

    }



    /**
     * Checks if there are any illegal component placements on the board.
     * Validates that components like hot water heaters and plasma drills
     * meet their specific placement requirements.
     *
     * @param visited list of positions representing the connected board area
     * @return true if all components are legally placed, false if illegal placements exist
     */
    public boolean checkIllegal( ArrayList<IntegerPair> visited){
        int x;
        int y;
        boolean legal = true;

        for (IntegerPair pair : visited) {

            x = pair.getFirst();
            y = pair.getSecond();

            if(!PlayerBoard[x][y].controlDirections(this,x,y)){
                legal = false;
                toRemovePB[x][y] = -2;
            }
        }
        return legal;

    }


    /**
     * Removes a tile from the player board at the specified coordinates.
     * The tile is replaced with a SpaceVoid tile and the position is marked as empty.
     * The main cockpit at position (6,6) cannot be removed.
     *
     * @param x the x-coordinate of the tile to remove
     * @param y the y-coordinate of the tile to remove
     * @throws InvalidInput if the coordinates are out of bounds, invalid, or point to the main cockpit
     */
    public void removeTile(int x, int y) throws InvalidInput {
        if (x < 0 || x >= 10 || y < 0 || y >= 10 || ValidPlayerBoard[x][y] == -1) {
            throw new InvalidInput(x, y, "Invalid input: coordinates out of bounds or invalid tile.");
        }
        if (x == 6 && y == 6){
            throw new InvalidInput("You can't remove the Main Cockpit");
        }
        PlayerBoard[x][y].setX(x);

        PlayerBoard[x][y].setY(y);
        System.out.println("rimuovo "+x + " "+ y);
        PlayerBoard[x][y].getComponent().remove(this);
        PlayerBoard[x][y] = new Tile(new SpaceVoid() ,NONE.INSTANCE, NONE.INSTANCE, NONE.INSTANCE, NONE.INSTANCE);
        ValidPlayerBoard[x][y] = 0;
    }



    /**
     * Validates the player board according to game rules.
     * Checks that:
     * - All tiles are reachable from the main cockpit
     * - All connections between tiles are legal
     * - Component placement constraints are satisfied
     * - No tiles remain in the buffer (adds damage if any exist)
     *
     * @return true if the player board is valid, false otherwise
     */
    public boolean checkValidity(){

        if(!Buffer.isEmpty()){
            this.damage+=Buffer.size();
            Buffer.clear();
            updateInfo();
        }
        int r = 6;
        int c = 6;
        valid = true;
        ArrayList<IntegerPair> visitedPositions = new ArrayList<>();

        findPaths(r,c,visitedPositions);

        if(!valid){
            return false;
        }

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
     * Performs a depth-first search to find all reachable tiles from the starting position.
     * Validates connections between adjacent tiles during traversal.
     * Used for board integrity checking.
     *
     * @param r the starting x-coordinate
     * @param c the starting y-coordinate
     * @param visited list to track visited positions during the search
     */
    public void findPaths(int r, int c, ArrayList<IntegerPair> visited) {

        if (!valid || visited.contains(new IntegerPair(r, c))||r < 0 || c < 0 || r > 9 || c > 9 || this.ValidPlayerBoard[r][c] == -1 ) {
            return;
        }
        visited.add(new IntegerPair(r, c));
        System.out.println(r + " " + c);


        if (valid && c - 1 >=0 && ValidPlayerBoard[r][c-1] == 1 && checkConnection(getTile(r,c).getConnectors().get(0),getTile(r, c -1).getConnectors().get(2), r, c -1, visited, r,c)) {
            findPaths(r, c - 1, visited);
        }



        if (valid && r - 1 >=0 && ValidPlayerBoard[r-1][c] == 1 && checkConnection(getTile(r,c).getConnectors().get(1),getTile(r-1, c ).getConnectors().get(3), r-1, c , visited, r,c)){
            findPaths(r -1,c ,visited);
        }



        if (valid && c + 1 <= 9 && ValidPlayerBoard[r][c+1] == 1 && checkConnection(getTile(r,c).getConnectors().get(2),getTile(r, c + 1).getConnectors().getFirst(), r, c + 1, visited, r,c)){
            findPaths(r,c + 1 ,visited);
        }


        if (valid && r + 1 <= 9 && ValidPlayerBoard[r+1][c] == 1 && checkConnection(getTile(r,c).getConnectors().get(3),getTile(r + 1, c ).getConnectors().get(1), r + 1, c, visited, r,c)){

            findPaths(r +1,c ,visited);
        }

    }


    /**
     * Destroys a tile at the specified coordinates, replacing it with a SpaceVoid.
     * Increases damage counter, updates board attributes, validates housing units,
     * and updates stored goods locations.
     *
     * @param x the x-coordinate of the tile to destroy
     * @param y the y-coordinate of the tile to destroy
     */
    public void destroy(int x, int y) {

        PlayerBoard[x][y].getComponent().remove(this);
        damage++;
        updateInfo();
        PlayerBoard[x][y] = new Tile(new SpaceVoid(),NONE.INSTANCE, NONE.INSTANCE, NONE.INSTANCE, NONE.INSTANCE);
        ValidPlayerBoard[x][y] = 0;
        for(HousingUnit Unit : HousingUnits){
            Unit.controlValidity(this, Unit.getX(), Unit.getY());
        }
        updateStoredGoods();
    }


    /**
     * Updates the stored goods locations by removing goods from destroyed tiles.
     * Removes entries for positions that are no longer valid (ValidPlayerBoard != 1).
     */
    public void updateStoredGoods(){

        for (Integer Goods : storedGoods.keySet()){
                storedGoods.get(Goods).removeIf(pair -> ValidPlayerBoard[pair.getFirst()][pair.getSecond()] != 1);
        }

    }

    /**
     * Allows the player to choose which ship section to keep after the ship breaks apart.
     * Searches through available ship sections to find the one containing the specified position.
     *
     * @param input the position (chunk) selected by the player
     * @return the list of positions representing the chosen ship section
     * @throws InvalidInput if the selected chunk doesn't exist in any ship section
     */
    public ArrayList<IntegerPair> choosePlayerBoard(IntegerPair input){
        for (Integer i : this.shipSection.keySet()){
            if (this.shipSection.get(i).contains(input)){
                ArrayList<IntegerPair> temp = this.shipSection.get(i);
                shipSection.clear();
                return temp;
            }
        }

        throw new InvalidInput("Invalid input: selected chunk doesn't exists!");
    }


    /**
     * Modifies the player board to keep only the tiles in the specified section.
     * All other tiles are removed and replaced with SpaceVoids.
     * Increases damage for each removed tile and updates all board attributes.
     *
     * @param newPlayerBoard the list of positions representing the section to keep
     */
    public void modifyPlayerBoard(ArrayList<IntegerPair> newPlayerBoard)  {
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
        updateInfo();
        updateAttributes(newPlayerBoard.getFirst().getFirst(),newPlayerBoard.getFirst().getSecond());
        broken = false;
        for(HousingUnit Unit : HousingUnits){
            Unit.controlValidity(this, Unit.getX(), Unit.getY());
        }

    }


    /**
     * Resets and updates all board attributes starting from the specified coordinates.
     * Clears exposed connectors and shield values, then recalculates them
     * by traversing the connected board area.
     *
     * @param x the starting x-coordinate for attribute calculation
     * @param y the starting y-coordinate for attribute calculation
     */
    public void updateAttributes(int x, int y){
        this.exposedConnectors = 0;
        for(int i = 0; i < 4; i ++){
            shield[i] = 0;
        }

        ArrayList<IntegerPair> visitedPositions = new ArrayList<>();
        updateBoardAttributes(x,y, visitedPositions);
        updateGlobalAttributes(visitedPositions);
        updateStoredGoods();
        updateInfo();

    }

    /**
     * Updates global attributes for all tiles on the board by controlling their directions.
     * This method iterates through each position in the provided board list and calls
     * controlDirections for each tile to update its directional attributes.
     *
     * @param board ArrayList of IntegerPair coordinates representing tiles to update
     */
    public void updateGlobalAttributes(ArrayList<IntegerPair> board){
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
     * @param visited of type  ArrayList&lt;IntegerPair&gt; - keeps track of all the tiles already visited.
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

        if (c + 1 <= 9 && ValidPlayerBoard[r][c+1] == 1&& checkConnection(getTile(r,c).getConnectors().get(2),getTile(r, c + 1).getConnectors().getFirst())){
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

        if ( x+1<10 &&ValidPlayerBoard[x+1][y] == 1 ){
            visitedPositions = new ArrayList<>();
            findPaths(x+1, y, visitedPositions);
            if (!visitedPositions.contains(new IntegerPair(x-1,y)) && !visitedPositions.contains(new IntegerPair(x,y -1))) {

                this.shipSection.put(i, visitedPositions);
                i++;
            }

        }

        if (y+1 <10 &&ValidPlayerBoard[x][y + 1] == 1  ){
            visitedPositions = new ArrayList<>();
            findPaths(x, y+1, visitedPositions);
            if (!visitedPositions.contains(new IntegerPair(x-1,y)) && !visitedPositions.contains(new IntegerPair(x,y -1)) && !visitedPositions.contains(new IntegerPair(x+1,y)) ) {

                this.shipSection.put(i, visitedPositions);

            }

        }
        if (shipSection.size() != 1 && !shipSection.isEmpty()){
            broken = true;
        }

    }



    /**
     * Gets the current status of the purple alien.
     *
     * @return true if purple alien is present, false otherwise
     */
    public boolean getPurpleAlien(){
        return this.purpleAlien;
    }

    /**
     * Gets the current status of the brown alien.
     *
     * @return true if brown alien is present, false otherwise
     */
    public boolean getBrownAlien(){
        return this.brownAlien;
    }

    /**
     * Sets the brown alien status and updates board information.
     *
     * @param brownAlien true to activate brown alien, false to deactivate
     */
    public void setBrownAlien(boolean brownAlien) {
        this.brownAlien = brownAlien;
        updateInfo();
    }

    /**
     * Sets the purple alien status and updates board information.
     *
     * @param purpleAlien true to activate purple alien, false to deactivate
     */
    public void setPurpleAlien(boolean purpleAlien) {
        this.purpleAlien = purpleAlien;
        updateInfo();
    }

    /**
     * Gets the list of goods currently in the buffer.
     *
     * @return ArrayList of Goods in the buffer
     */
    public ArrayList<Goods> getBufferGoods() {
        return BufferGoods;
    }

    /**
     * Gets the list of housing units on the player board.
     *
     * @return ArrayList of HousingUnit objects
     */
    public ArrayList<HousingUnit> getHousingUnits() {
        return HousingUnits;
    }

    /**
     * Gets the list of hot water heaters on the player board.
     *
     * @return ArrayList of HotWaterHeater objects
     */
    public ArrayList<HotWaterHeater> getHotWaterHeaters() {
        return HotWaterHeaters;
    }

    /**
     * Gets the list of plasma drills on the player board.
     *
     * @return ArrayList of PlasmaDrill objects
     */
    public ArrayList<PlasmaDrill> getPlasmaDrills() {
        return PlasmaDrills;
    }

    /**
     * Gets the list of alien addons on the player board.
     *
     * @return ArrayList of AlienAddons objects
     */
    public ArrayList<AlienAddons> getAlienAddons() {
        return AlienAddons;
    }

    /**
     * Gets the list of storage units on the player board.
     *
     * @return ArrayList of Storage objects
     */
    public ArrayList<Storage> getStorages() {
        return Storages;
    }

    /**
     * Gets the list of shield generators on the player board.
     *
     * @return ArrayList of ShieldGenerator objects
     */
    public ArrayList<ShieldGenerator> getShieldGenerators() {
        return ShieldGenerators;
    }

    /**
     * Gets the list of power centers on the player board.
     *
     * @return ArrayList of PowerCenter objects
     */
    public ArrayList<PowerCenter> getPowerCenters() {
        return PowerCenters;
    }

    /**
     * Gets the current engine power with alien bonus if applicable.
     * Returns the base engine power plus 2 if brown alien is active.
     *
     * @return current engine power value
     */
    public int getEnginePower() {
        if (EnginePower != 0){
            if (brownAlien){
                return EnginePower +2;
            }
        }
        return EnginePower;
    }
    /**
     * Increases the engine power by the specified amount.
     *
     * @param enginePower amount to add to current engine power
     */
    public void setEnginePower(int enginePower) {
        EnginePower += enginePower;
    }


    /**
     * Gets the current plasma drill power with alien bonus if applicable.
     * Returns the base plasma drill power plus 2 if purple alien is active.
     *
     * @return current plasma drill power value
     */
    public double getPlasmaDrillsPower() {
        if(PlasmaDrillsPower != 0){
            if(purpleAlien){
                return PlasmaDrillsPower + 2;
            }
        }
        return PlasmaDrillsPower;
    }

    /**
     * Increases the plasma drill power by the specified amount and updates board info.
     *
     * @param plasmaDrillsPower amount to add to current plasma drill power
     */
    public void setPlasmaDrillsPower(double plasmaDrillsPower) {
        PlasmaDrillsPower += plasmaDrillsPower;
        updateInfo();
    }

    /**
     * Gets the current number of humans on the ship.
     *
     * @return number of humans
     */
    public int getNumHumans() {

        return numHumans;
    }

    /**
     * Increases the number of humans by the specified amount and updates board info.
     *
     * @param numHumans number of humans to add
     */
    public void setNumHumans(int numHumans) {
        this.numHumans += numHumans;
        updateInfo();
    }

    /**
     * Gets the current energy level.
     *
     * @return current energy value
     */
    public int getEnergy() {
        return Energy;
    }

    /**
     * Increases the energy by the specified amount and updates board info.
     *
     * @param energy amount of energy to add
     */
    public void setEnergy(int energy) {
        Energy += energy;
        updateInfo();
    }

    /**
     * Performs a specified action on a component with the given player state.
     *
     * @param component the Component to perform action on
     * @param action the ComponentAction to execute
     * @param state the PlayerState context for the action
     */
    public void performAction(Component component, ComponentAction action, PlayerState state) {
        component.accept(action, state);
    }

    /**
     * Gets the current tile buffer.
     *
     * @return ArrayList of Tile objects in the buffer
     */
    public ArrayList<Tile> getBuffer(){
        return Buffer;
    }


    /**
     * Sets the tile buffer to a new list of tiles.
     *
     * @param newBuffer new ArrayList of Tile objects to set as buffer
     */
    public void setBuffer (ArrayList<Tile> newBuffer) {
        this.Buffer = newBuffer;
    }

    /**
     * Creates a deep copy of the current PlayerBoard instance.
     * This method clones all essential properties including tiles, alien status,
     * various component lists, and board state arrays.
     *
     * @return a new PlayerBoard instance that is a deep copy of this board
     */
    public PlayerBoard clone(){
        PlayerBoard clonedPlayerBoard = new PlayerBoard(lv);
        clonedPlayerBoard.broken = broken;
        clonedPlayerBoard.purpleAlien = purpleAlien;
        clonedPlayerBoard.brownAlien = brownAlien;
        //clonedPlayerBoard.totalValue = totalValue;
        clonedPlayerBoard.damage = damage;
        //clonedPlayerBoard.numHumans = numHumans;
        clonedPlayerBoard.exposedConnectors = exposedConnectors;
        //clonedPlayerBoard.EnginePower = EnginePower;
        //clonedPlayerBoard.PlasmaDrillsPower = PlasmaDrillsPower;
        //clonedPlayerBoard.Energy = Energy;
        clonedPlayerBoard.lv = lv;
        clonedPlayerBoard.valid = valid;
        clonedPlayerBoard.HousingUnits = new ArrayList<>();
        clonedPlayerBoard.HotWaterHeaters= new ArrayList<>();
        clonedPlayerBoard.PlasmaDrills= new ArrayList<>();
        clonedPlayerBoard.AlienAddons= new ArrayList<>();
        clonedPlayerBoard.Storages= new ArrayList<>();
        clonedPlayerBoard.ShieldGenerators= new ArrayList<>();
        clonedPlayerBoard.PowerCenters= new ArrayList<>();
        clonedPlayerBoard.connectedHousingUnits = new ArrayList<>();
        clonedPlayerBoard.Rewards = new ArrayList<>(this.Rewards);
        clonedPlayerBoard.rewardsListener = this.getRewardsListener();
        clonedPlayerBoard.setBuffer(this.Buffer);
        clonedPlayerBoard.storedGoods = new HashMap<>();

        clonedPlayerBoard.setListener(null);

        clonedPlayerBoard.PlayerBoard = new Tile[PlayerBoard.length][PlayerBoard[0].length];
        for (int i = 0; i < PlayerBoard.length; i++) {
            for (int j = 0; j < PlayerBoard[i].length; j++) {
                Tile tile = PlayerBoard[i][j];
//                clonedPlayerBoard.PlayerBoard[i][j] = tile != null ? tile.clone(clonedPlayerBoard) : null;
                if(tile != null) {
                    Tile t = tile.clone(clonedPlayerBoard);
                    //clonedPlayerBoard.insertTile(t,i,j, false);
                    clonedPlayerBoard.PlayerBoard[i][j] = t;
                }
                else{
                    Tile t = new Tile(new SpaceVoid(), NONE.INSTANCE, NONE.INSTANCE, NONE.INSTANCE, NONE.INSTANCE);
                    //clonedPlayerBoard.insertTile(t,i,j, false);
                    clonedPlayerBoard.PlayerBoard[i][j] = null;
                }


                if (tile != null) {
                    clonedPlayerBoard.PlayerBoard[i][j].setY(j);
                    clonedPlayerBoard.PlayerBoard[i][j].setX(i);
                    clonedPlayerBoard.PlayerBoard[i][j].setPlayerBoard(clonedPlayerBoard);
                    clonedPlayerBoard.PlayerBoard[i][j].getComponent().setTile(clonedPlayerBoard.PlayerBoard[i][j]);
                    clonedPlayerBoard.PlayerBoard[i][j].getComponent().insert(clonedPlayerBoard, i, j);
                }
            }
        }

        clonedPlayerBoard.ValidPlayerBoard = new int[ValidPlayerBoard.length][ValidPlayerBoard[0].length];
        for (int i = 0; i < ValidPlayerBoard.length; i++) {
            clonedPlayerBoard.ValidPlayerBoard[i] = Arrays.copyOf(ValidPlayerBoard[i], ValidPlayerBoard[i].length);
        }

        clonedPlayerBoard.shield = Arrays.copyOf(shield, shield.length);


        clonedPlayerBoard.setListener(this.getListener());

        for(HousingUnit unit : clonedPlayerBoard.getHousingUnits()){
            unit.checkNearbyUnits(clonedPlayerBoard);
        }


        return clonedPlayerBoard;

    }


    /**
     * Gets the rewards listener for this player board.
     *
     * @return the RewardsListener instance
     */
    public RewardsListener getRewardsListener() {
        return rewardsListener;
    }

    /**
     * Checks if the player board is in a broken state.
     *
     * @return true if the board is broken, false otherwise
     */
    public boolean isBroken() {
        return broken;
    }

    /**
     * Gets the current level of the player board.
     *
     * @return the level value
     */
    public int getLv() {
        return lv;
    }


    /**
     * Checks if the player board is in a valid state.
     *
     * @return true if the board is valid, false otherwise
     */
    public boolean isValid() {
        return valid;
    }


    /**
     * Checks if the purple alien is active.
     *
     * @return true if purple alien is active, false otherwise
     */
    public boolean isPurpleAlien() {
        return purpleAlien;
    }

    /**
     * Checks if the brown alien is active.
     *
     * @return true if brown alien is active, false otherwise
     */
    public boolean isBrownAlien() {
        return brownAlien;
    }

    /**
     * Sets the rewards list and notifies listeners of the change.
     *
     * @param rewards ArrayList of Goods to set as rewards
     */
    public void setRewards(ArrayList<Goods> rewards){
        this.Rewards = rewards;
        if (rewardsListener!= null && rewards != null){
            rewardsListener.rewardsChanged(new RewardsEvent(new ArrayList<>(Rewards)));
            for (Goods g : Rewards){
                System.out.println("@@@@"+ g.getClass());
            }
        }
    }

    /**
     * Gets the current rewards list.
     *
     * @return ArrayList of Goods representing current rewards
     */
    public ArrayList<Goods> getRewards(){
        return Rewards;
    }


    /**
     * Removes and returns a specific reward from the rewards list.
     *
     * @param i index of the reward to remove
     * @return the Goods object that was removed
     * @throws InvalidInput if the index is out of bounds or rewards list is empty
     */
    public Goods getFromRewards(int i){
        if (i > Rewards.size()) {
            throw new InvalidInput("This position in the Rewards does not exist");
        }
        if (Rewards.isEmpty()) {
            throw new InvalidInput("Rewards is empty");
        }
        Goods removed = Rewards.remove(i);
        if (rewardsListener!= null){
            rewardsListener.rewardsChanged(new RewardsEvent(new ArrayList<>(Rewards)));
        }
        return removed;
    }

    /**
     * Gets the broken status of the player board.
     *
     * @return true if the board is broken, false otherwise
     */
    public boolean getBroken(){
        return broken;
    }


    /**
     * Gets the list of connected housing units.
     *
     * @return ArrayList of connected HousingUnit objects
     */
    public ArrayList<HousingUnit> getConnectedHousingUnits(){
        return connectedHousingUnits;
    }


    /**
     * Sets the player board listener for receiving board change events.
     *
     * @param listener the PlayerBoardListener to set
     */
    public void setListener(PlayerBoardListener listener){
        this.listener = listener;
    }


    /**
     * Removes the current player board listener.
     */
    public void removeListener(){
        this.listener = null;
    }


    /**
     * Gets the current player board listener.
     *
     * @return the current PlayerBoardListener instance
     */
    public PlayerBoardListener getListener(){
        return listener;
    }


    /**
     * Sends tile update events to the registered listener.
     *
     * @param event the TileEvent to send to the listener
     */
    public void sendUpdates(TileEvent event){
        if(listener != null) {
            listener.playerBoardChanged(event);
        }
    }


    /**
     * Calculates the final score for the race based on stored goods, damage, and credits.
     * If the race is not finished, the score is halved (rounded up for odd numbers).
     *
     * @param finished true if the race was completed, false if not finished
     * @return the calculated final score
     */
    public int finishRace(boolean finished){
        int number;
        int result=0;

        for (Integer key: storedGoods.keySet()) {
            number = this.storedGoods.get(key).size();
            result+= key*number;
        }

        if(!finished){
            if(result%2==1){
                result = result/2;
                result+=1;
            }
            else{
                result = result/2;
            }
        }

        result -= this.damage;
        result += this.credits;
        return result;

    }

    /**
     * Updates the board information and notifies listeners of changes.
     * Calculates crew count including aliens and creates a PBInfoEvent
     * with current board statistics.
     */
    public void updateInfo(){
        if (listener != null){
            int crew = 0;
            crew += numHumans;
            if (purpleAlien){
                crew++;
            }
            if (brownAlien){
                crew++;
            }
            PBInfoEvent event = new PBInfoEvent(this.damage, this.credits, this.exposedConnectors, this.shield,crew, getEnginePower(), getPlasmaDrillsPower(), this.Energy, this.purpleAlien, this.brownAlien, this.totalValue);
            listener.PBInfoChanged(event);
        }

    }

    /**
     * Increases the credits by the specified amount and updates board information.
     *
     * @param num amount of credits to add (can be negative to subtract)
     */
    public void setCredits(int num){
        credits += num;
        updateInfo();
    }


    /**
     * Gets the array representing tiles to be removed from the player board.
     *
     * @return 2D int array representing positions of tiles to remove
     */
    public int[][] getToRemovePB() {
        return toRemovePB;
    }
}