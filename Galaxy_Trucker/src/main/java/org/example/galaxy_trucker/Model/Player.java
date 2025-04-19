package org.example.galaxy_trucker.Model;
import org.example.galaxy_trucker.Model.Boards.GameBoard;
import org.example.galaxy_trucker.Model.Goods.Goods;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.Cards.Card;
import org.example.galaxy_trucker.Model.PlayerStates.PlayerState;
import org.example.galaxy_trucker.Model.Tiles.Tile;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class Player implements Serializable {

    private GameBoard CommonBoard;
    private PlayerBoard myPlayerBoard;
    private String ID;
    private boolean ready;
    private int credits;

    public GameBoard getCommonBoard() {
        return CommonBoard;
    }

    public Tile getCurrentTile() {
        return CurrentTile;
    }

    public void setCurrentTile(Tile currentTile) {
        if (currentTile != null) {
            throw new IllegalStateException("Your hand is full!");
        }
        CurrentTile = currentTile;
    }

    private Tile CurrentTile;   //the tile that Player has in his hand
    private PlayerState PlayerState;
    private ArrayList<Goods> GoodsToHandle;
    private Card CurrentCard;



    public Player()  {
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

    public PlayerState getPlayerState() {
        return PlayerState;
    }


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
    


    public void PickNewTile(int index){
        if (index == -1){
            if (CurrentTile != null) {
                throw new IllegalStateException("You can't pick a Tile, you have already one!");
            }
            CurrentTile = CommonBoard.getTilesSets().getNewTile();
            System.out.println("Id Tile: " +CurrentTile.getId());
        }
        else {
            if (CurrentTile != null) {
                throw new IllegalStateException("You can't pick a Tile, you have already one!");
            }

            try{
                CurrentTile = CommonBoard.getTilesSets().getNewTile(index);
                System.out.println("Id Tile: " +CurrentTile.getId());
            }catch(RuntimeException e){
                System.out.println(e);
                CurrentTile = null;
            }


        }
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


    public void SpyDeck(int index){

        ArrayList<Card> observedDeck = switch (index) {
            case 1 -> CommonBoard.getCardStack().getVisibleCards1();
            case 2 -> CommonBoard.getCardStack().getVisibleCards2();
            case 3 -> CommonBoard.getCardStack().getVisibleCards3();
            default -> throw new IllegalArgumentException("Invalid index");
        };

    }


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
//        this.setState(new FinishedBuilding());
       }


    public void SetReady(boolean ready){
        this.ready = ready;
    }
    public void setId(String id){this.ID = id;}

    public void setBoards(GameBoard CommonBoard) {
        this.CommonBoard = CommonBoard;
        myPlayerBoard = new PlayerBoard(CommonBoard.getLevel());
    }



    public String GetID() {return this.ID;}
    public int GetCredits() {return this.credits;}
    public boolean GetReady() {return this.ready;}
    public PlayerBoard getmyPlayerBoard() {return myPlayerBoard;}




    public Card getCurrentCard() {
        return CurrentCard;
    }


    //DOVREI AGGIUNGERE UN MODO PER ARRIVARE A CARD DA PLAYER DIREI :)
    //principalmente per chiamare i metodi di card dopo l'input

}
