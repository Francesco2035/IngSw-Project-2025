package org.example.galaxy_trucker.Model;
import org.example.galaxy_trucker.Model.Boards.GameBoard;
import org.example.galaxy_trucker.Model.Goods.Goods;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.Cards.Card;
//import org.example.galaxy_trucker.Model.InputHandlers.InputHandler;
import org.example.galaxy_trucker.Model.Tiles.Tile;
import org.example.galaxy_trucker.Model.PlayerStates.*;

import java.util.ArrayList;
import java.util.Random;

public class Player {

    private GameBoard CommonBoard;
    private PlayerBoard myPlayerBoard;
    private String ID;
    private boolean ready;
    private int credits;
    private Tile CurrentTile;   //the tile that Player has in his hand
    private PlayerState PlayerState;
    //private InputHandler InputHandler;
    private ArrayList<Goods> GoodsToHandle;
    private Card CurrentCard;



    public Player(String id, GameBoard board) {
        CommonBoard = board;
        myPlayerBoard = new PlayerBoard(board.getLevel());
        ID = id;
        credits = 0;
        ready = false;
        CurrentTile = null;
        PlayerState= null;
        GoodsToHandle = new ArrayList<>();
        CurrentCard = null;
    }




//
//    public void consumeEnergyFrom(IntegerPair coordinates) {
//        myPlayerBoard.getEnergyTiles().stream()
//                                 .filter(pair -> pair.equals(coordinates))
//                                 .findFirst()
//                                 .ifPresent(pair -> myPlayerBoard.getTile(pair.getFirst(), pair.getSecond()).getComponent().setAbility());//riduce di 1 le batterie a x, y se non sono già a zero
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
    
//    public InputHandler getInputHandler() {
//        return InputHandler;
//    }

    public PlayerState getPlayerState() {
        return PlayerState;
    }

//    public void setInputHandler(InputHandler InputHandler) {
//        this.InputHandler = InputHandler;
//    }

    public void setState(PlayerState state) {
        this.PlayerState = state;
    }

    public void setMyPlance(PlayerBoard myPlance) {
        this.myPlayerBoard = myPlance;
    }

    public void  execute() {
        this.CurrentCard.ActivateCard();
    }

    public void StartTimer() throws InterruptedException {
        CommonBoard.StartHourglass();
    }

    
    public void useEnergy(int x, int y){

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
        if (CurrentTile != null) {
            throw new IllegalStateException("You can't pick a Tile, you have already one!");
        }
        CurrentTile = CommonBoard.getTilesSets().getNewTile(index);
    }

    /**
     * discards the current tile and places it back in the uncovered tiles list
     */
    public void DiscardTile(){
        if (CurrentTile == null) {
            throw new IllegalStateException("You can't discard a Tile, you don't have one!");
        }
        CommonBoard.getTilesSets().AddUncoveredTile(CurrentTile);
        CurrentTile = null;
    }

    /**
     * places the current tile in the buffer
     */
    public void PlaceInBuffer(){
        myPlayerBoard.insertBuffer(CurrentTile);
        CurrentTile = null;
    }

    /**
     * takes a tile from the buffer
     * @param index of the tile to pick
     */
    public void SelectFromBuffer(int index){
        CurrentTile = myPlayerBoard.getTileFromBuffer(index);
      }


    /**
     * sets the current tile on the shipboard
     * this action is definitive: the tile cannot be moved or rotated after it is settled
     * @param coords where the tile will be placed
     */
    public void PlaceTile(IntegerPair coords){
        this.myPlayerBoard.insertTile(CurrentTile, coords.getFirst(), coords.getSecond());
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

    public void setCard(Card NewCard){
        CurrentCard = NewCard;
    }


    /**
     * once a player is done building his ship (or the time is up), this method sets his starting position on the common board
     */
    public void EndConstruction(){
        CommonBoard.SetStartingPosition(this.ID);
        this.setState(new FinishedBuilding());
       }


    public void SetReady(){
        this.ready = true;
    }



//    public ArrayList<IntegerPair> getPower(){
//        ArrayList<IntegerPair> Power = new ArrayList<>();
//        IntegerPair coords = new IntegerPair(6, 9);
//        Power.add(coords);
//        return Power;
//    }
//
//    public ArrayList<IntegerPair> getEnginePower(){
//        ArrayList<IntegerPair> Power = new ArrayList<>();
//        IntegerPair coords = new IntegerPair(4, 2);
//        Power.add(coords);
//        return Power;
//
//    }
//    public ArrayList<IntegerPair> getHumanstoKIll(){
//        ArrayList<IntegerPair> Locations = new ArrayList<>();
//        IntegerPair coords = new IntegerPair(4, 2);
//        Locations.add(coords);
//        return Locations;
//    }

    public String GetID() {return this.ID;}
    public int GetCredits() {return this.credits;}
    public boolean GetReady() {return this.ready;}
    public PlayerBoard getmyPlayerBoard() {return myPlayerBoard;}
    //    public ArrayList <IntegerPair> getHumans(){return this.myPlayerBoard.gethousingUnits();}
    public ArrayList<IntegerPair> getEnergyTiles(){
//        if(getmyPlayerBoard().getClassifiedTiles().containsKey(PowerCenter.class))
//            return this.getmyPlayerBoard().getClassifiedTiles().get(PowerCenter.class);
//        else return null;
        return null;
    }

//
//    // puts the good in index i into the respective coordinates
//    public void PutGoods(int index, IntegerPair coords){
//        myPlayerBoard.putGoods(GoodsToHandle.remove(index), coords);
//    }
//
//    // rimuove l'iesimo good in coordinata coords
//    public void removeGoods(IntegerPair coords,int index){
//        this.myPlayerBoard.removeGood(coords, index);
//    }

//
//    public int getGoodsIndex(){
//        return 3;
//    }
//    public IntegerPair getGoodsCoordinates(){
//        IntegerPair coords = new IntegerPair(5,5);
//        return coords;
//    }
//
//    public void switchGoods(int good1, IntegerPair coord1, int good2, IntegerPair coord2){
//        myPlayerBoard.pullGoods(good1, coord1);
//        myPlayerBoard.pullGoods(good2, coord2);
//        //se il buffer è sempre vuoto prima di questa chiamata funziona altrimenti mi serve la size
//        myPlayerBoard.putGoods(myPlayerBoard.pullFromBuffer(1),coord1);
//        myPlayerBoard.putGoods(myPlayerBoard.pullFromBuffer(0),coord2);
//    }


    public void handleCargo(ArrayList<Goods> reward){
//        this.GoodsToHandle.clear();
//
//        this.GoodsToHandle.addAll(reward);
//
//        this.setState(PlayerStates.HandlingCargo);
    }

    public void stopHandlingCargo(){
        this.setState(new Waiting());
        this.CurrentCard.finishCard();

        // controllo che
    }

    public Card getCurrentCard() {
        return CurrentCard;
    }

    //palu gay
    //DOVREI AGGIUNGERE UN MODO PER ARRIVARE A CARD DA PLAYER DIREI :)
    //principalmente per chiamare i metodi di card dopo l'input

}
