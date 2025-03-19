package org.example.galaxy_trucker.Model;
import org.example.galaxy_trucker.Model.Boards.GameBoard;
import org.example.galaxy_trucker.Model.Boards.Goods;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.Tiles.Tile;

import java.util.ArrayList;
import java.util.Random;

public class Player {

    private GameBoard CommonBoard;
    private PlayerBoard myPlance;
    private String ID;
    private boolean ready;
    private int credits;
    private Tile CurrentTile;   //the tile that Player has in his hand


    private Integer input;


    public Player(String id, GameBoard board) {
        CommonBoard = board;
        myPlance = new PlayerBoard(board.getLevel());
        ID = id;
        credits = 0;
        ready = false;
        CurrentTile = null;
        input = null;
    }



//
//    public void consumeEnergyFrom(IntegerPair coordinates) {
//        myPlance.getEnergyTiles().stream()
//                                 .filter(pair -> pair.equals(coordinates))
//                                 .findFirst()
//                                 .ifPresent(pair -> myPlance.getTile(pair.getFirst(), pair.getSecond()).getComponent().setAbility());//riduce di 1 le batterie a x, y se non sono già a zero
//    }
//





//    public void fireCannon(){}
//    public void startEngine(){}


    /**
     * rolls 2 dice
     *
     * @return the dice result (int between 2-12)
     */
    public int RollDice() {
        Random r = new Random();
        int d1 = r.nextInt(6) + 1;
        int d2 = r.nextInt(6) + 1;
        return d1+d2;
    }


    /**
     * get a new random tile from the covered tiles set
     */
    public void PickNewTile(){
        CurrentTile = CommonBoard.getTilesSets().getNewTile();
    }

    /**
     * select a new tile from the uncovered list
     *
     * @param index of the tile to pick
     */
    public void PickNewTile(int index){
        CurrentTile = CommonBoard.getTilesSets().getNewTile(index);
    }

    /**
     * discards the current tile and places it back in the uncovered tiles list
     */
    public void DiscardTile(){
        CommonBoard.getTilesSets().AddUncoveredTile(CurrentTile);
        CurrentTile = null;
    }

    /**
     * places the current tile in the buffer
     */
    public void PlaceInBuffer(){
        myPlance.insertBuffer(CurrentTile);
        CurrentTile = null;
    }

    /**
     * takes a tile from the buffer
     * @param index of the tile to pick
     */
    public void SelectFromBuffer(int index){
        CurrentTile = myPlance.getBuffer().get(index);
        myPlance.getBuffer().remove(index);
    }


    /**
     * sets the current tile on the shipboard
     * this action is definitive: the tile cannot be moved or rotated after it is settled
     * @param coords where the tile will be placed
     */
    public void PlaceTile(IntegerPair coords){
        this.myPlance.insertTile(CurrentTile, coords.getFirst(), coords.getSecond());
        CurrentTile = null;
    }

    /**
     * rotates the current tile 90° right
     */
    public void RightRotate() {CurrentTile.RotateDx();}

    /**
     * rotates the current tile 90° left
     */
    public void LeftRotate() {CurrentTile.RotateSx();}



    public void IncreaseCredits(int num){
        credits += num;
    }


    /**
     * once a player is done building his ship (or the time is up), this method sets his starting position on the common board
     */
//    public void EndConstruction(){
//        CommonBoard.SetStartingPosition(this.ID);
//    }


    public void SetReady(){
        this.ready = true;
    }


    public ArrayList<IntegerPair> getPower(){
        ArrayList<IntegerPair> Power = new ArrayList<>();
        IntegerPair coords = new IntegerPair(6, 9);
        Power.add(coords);
        return Power;
    }

    public ArrayList<IntegerPair> getEnginePower(){
        ArrayList<IntegerPair> Power = new ArrayList<>();
        IntegerPair coords = new IntegerPair(4, 2);
        Power.add(coords);
        return Power;

    }
    public ArrayList<IntegerPair> getHumanstoKIll(){
        ArrayList<IntegerPair> Locations = new ArrayList<>();
        IntegerPair coords = new IntegerPair(4, 2);
        Locations.add(coords);
        return Locations;
    }

    public String GetID() {return this.ID;}
    public int GetCredits() {return this.credits;}
    public boolean GetReady() {return this.ready;}
    public PlayerBoard getMyPlance() {return myPlance;}
    public ArrayList <IntegerPair> getHumans(){return this.myPlance.gethousingUnits();}
    public ArrayList<IntegerPair> getEnergyTiles(){return this.myPlance.getEnergyTiles();
    }


    public void setCargoAction(int action){
        if(action < 0 || action > 3)
            throw new IllegalArgumentException("Invalid action");
        else input = action;
        input.notify();
    }

    public int getCargoAction(){
        input.wait();
        if(input == null) throw new NullPointerException("No action selected");
        int temp = input;
        input = null;
        return temp;
    }

    public void setGoodIndex(int index){
        input = index;
        input.notify();
    }

    public int getGoodsIndex() throws InterruptedException {
        input.wait();
        if(input == null) throw new NullPointerException("No action selected");
        int temp = input;
        input = null;
        return temp;
    }

    public IntegerPair getGoodsCoordinates(){
        IntegerPair coords = new IntegerPair(5,5);
        return coords;
    }

    public void switchGoods(int good1, IntegerPair coord1, int good2, IntegerPair coord2){
        myPlance.pullGoods(good1, coord1);
        myPlance.pullGoods(good2, coord2);
        //se il buffer è sempre vuoto prima di questa chiamata funziona altrimenti mi serve la size
        myPlance.putGoods(myPlance.pullFromBuffer(1),coord1);
        myPlance.putGoods(myPlance.pullFromBuffer(0),coord2);
    }


    public void handleCargo(ArrayList<Goods> reward){
        ArrayList<Integer> UsedAddresses= new ArrayList<>();

        while(!reward.isEmpty()) {
            int a = getCargoAction();
            if (a == 0) { // terminate handling cargo
                return;
            } else if (a == 1) {//put goods
                int i= getGoodsIndex();
                if(!UsedAddresses.contains(i)) {
                    UsedAddresses.add(i);
                    myPlance.putGoods(reward.get(i),getGoodsCoordinates());
                    myPlance.putGoods(reward.get(i),getGoodsCoordinates());
                }
                //else sollevo eccezione?

            } else if (a == 2) {// switch positions
                //chiamo get type goods e coord
                int g1=1;
                int g2=0;
                IntegerPair c1 = new IntegerPair(5,5);
                IntegerPair c2 = new IntegerPair(8,6);
                switchGoods(g1,c1,g2,c2);
            }
        }

        //chiedergli un azione alla volta è vieteato neh?


    }

}
