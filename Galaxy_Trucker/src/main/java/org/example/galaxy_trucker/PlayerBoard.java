package org.example.galaxy_trucker;



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
    //private ArrayList<IntegerPair> hotWaterHeaters;


    private ArrayList<Tile> Buffer;


    HashMap<Connector, ArrayList<Connector>>  validConnection;


    //attributes



    public PlayerBoard(int lv) {
        this.damage = 0;
        this.shield = new int[4];

        this.exposedConnectors = 0;
        this.housingUnits = new ArrayList<>();
        this.energyTiles = new ArrayList<>();

        this.Cargo = new ArrayList<>();
        this.Buffer = new ArrayList<>();
        this.plasmaDrills = new ArrayList<>();
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
    public ArrayList<Tile> getBuffer(){
        return Buffer;
    }

    public int getExposedConnectors(){
        return exposedConnectors;
    }

   public ArrayList<IntegerPair> gethousingUnits(){
        return this.housingUnits;
    }




    public Tile[][] getPlayerBoard(){
        return this.PlayerBoard;
    }

    public int[][] getValidPlayerBoard(){
        return this.ValidPlayerBoard;
    }

    public ArrayList<IntegerPair> getEnergyTiles() {
        return energyTiles;
    }

    public int getDamage(){
        return damage;
    }

    public Tile getTile(int x, int y) {
        return this.PlayerBoard[x][y];
    }


    public ArrayList<IntegerPair> getPlasmaDrills(){
        return plasmaDrills;
    }

    public void insertTile(Tile tile, int x, int y) throws NullPointerException, ArrayIndexOutOfBoundsException {
        try {
            if (tile == null) {
                throw new NullPointerException("Tile cannot be null.");
            }

            if (x < 0 || x >= PlayerBoard.length || y < 0 || y >= PlayerBoard[0].length || ValidPlayerBoard[x][y] == -1) {
                throw new ArrayIndexOutOfBoundsException("Invalid position: (" + x + ", " + y + ")");
            }

            this.PlayerBoard[x][y] = tile;
            ValidPlayerBoard[x][y] = 1;
        } catch (NullPointerException | ArrayIndexOutOfBoundsException e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
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
        //hotWaterHeaters.remove(pos);

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

        else if (PlayerBoard[r][c].getComponent().getClass() == BatteryComp.class){
            energyTiles.add(new IntegerPair(r,c));
        }

        else if (PlayerBoard[r][c].getComponent().getClass() == plasmaDrill.class){
            plasmaDrills.add(new IntegerPair(r, c));
        }

        else if (PlayerBoard[r][c].getComponent().getClass() == modularHousingUnit.class || PlayerBoard[r][c].getComponent().getClass() == MainCockpitComp.class){
            housingUnits.add(new IntegerPair(r,c));
        }
//        else if (PlayerBoard[r][c].getComponent().getClass() == shieldGenerator.class){
//
//        }

//        else if (PlayerBoard[r][c].getComponent().getClass() == hotWaterHeater.class ){
//            hotWaterHeaters.add(new IntegerPair(r,c));
//        }


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
            i++;
            shipSection.put(i, visitedPositions);

        }

        if (ValidPlayerBoard[x][y-1] == 1 && !shipSection.get(i).contains (new IntegerPair(x,y-1))){
            visitedPositions.clear();
            findPaths(x, y-1, visitedPositions);
            i++;
            shipSection.put(i, visitedPositions);

        }

        if (ValidPlayerBoard[x+1][y] == 1 && !shipSection.get(i).contains (new IntegerPair(x+1,y))){
            visitedPositions.clear();
            findPaths(x+1, y, visitedPositions);
            i++;
            shipSection.put(i, visitedPositions);

        }

        if (ValidPlayerBoard[x][y + 1] == 1 && !shipSection.get(i).contains (new IntegerPair(x,y+1))){
            visitedPositions.clear();
            findPaths(x, y+1, visitedPositions);
            i++;
            shipSection.put(i, visitedPositions);

        }

        return shipSection;

    }


    /**
     * Method kill reduces the number of Human or Alien in a housing cell by 1 given the coordinate of this cell
     *
     * @param coordinate of type IntegerPair - the value of the coordinate.
     * @throws ArrayIndexOutOfBoundsException when the user input is not in the correct range.
     */
    public void kill(IntegerPair coordinate,int humans ,boolean purpleAlien, boolean brownAlien) throws ArrayIndexOutOfBoundsException{
        if (coordinate.getFirst() < 0 || coordinate.getFirst() >= PlayerBoard.length || coordinate.getSecond() < 0 || coordinate.getSecond() >= PlayerBoard[0].length || ValidPlayerBoard[coordinate.getFirst()][coordinate.getSecond()] == -1) {
            throw new ArrayIndexOutOfBoundsException("Invalid position: (" + coordinate.getFirst() + ", " + coordinate.getSecond() + ")");
        }
        PlayerBoard[coordinate.getFirst()][coordinate.getSecond()].getComponent().setAbility(humans, purpleAlien, brownAlien);
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
     */
    public int getEnginePower(ArrayList<IntegerPair> chosenHotWaterHeaters) {
        if (chosenHotWaterHeaters == null) {
            throw new NullPointerException("chosenHotWaterHeaters cannot be null.");
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



    //per switch
    public void pullGoods(int i, IntegerPair coordinate){
        BufferGoods.add(PlayerBoard[coordinate.getFirst()][coordinate.getSecond()].getComponent().getAbility(null).remove(i));
    }


    //per aggiungere goods nei magazzini
    public void putGoods(Goods good, IntegerPair coordinate){
        PlayerBoard[coordinate.getFirst()][coordinate.getSecond()].getComponent().setAbility(good, true); //aggiunta modifica dell'implementazione di aggiunta/rimozione goods
    }


    //per switch
    public Goods pullFromBuffer(int i){
        return BufferGoods.remove(i);
    }


    public void useEnergy(ArrayList<IntegerPair> chosenEnergyTiles){
        for (IntegerPair energy : chosenEnergyTiles){
            PlayerBoard[energy.getFirst()][energy.getSecond()].getComponent().setAbility();
        }
    }

    //per eliminare senza passare dal buffer
    public void removeGood(IntegerPair coordinate, int i){
        PlayerBoard[coordinate.getFirst()][coordinate.getSecond()].getComponent().getAbility(null).remove(i);
    }
// non lo posso gestire senza che passo modifichi housingUnit, posso solo eliminare umani
//    public void populateHousingUnit(IntegerPair coordinate , int humans ,boolean purpleAlien, boolean brownAlien){
//        PlayerBoard[coordinate.getFirst()][coordinate.getSecond()].getComponent().setAbility();
//    }


}