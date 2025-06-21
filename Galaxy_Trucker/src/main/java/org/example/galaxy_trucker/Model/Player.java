package org.example.galaxy_trucker.Model;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.galaxy_trucker.Controller.Listeners.CardListner;
import org.example.galaxy_trucker.Controller.Listeners.GameBoardListener;
import org.example.galaxy_trucker.Controller.Listeners.HandListener;
import org.example.galaxy_trucker.Controller.Listeners.PhaseListener;
import org.example.galaxy_trucker.Controller.Messages.FinishListener;
import org.example.galaxy_trucker.Controller.Messages.HandEvent;
import org.example.galaxy_trucker.Controller.Messages.PhaseEvent;
import org.example.galaxy_trucker.Controller.Messages.PlayerBoardEvents.TileEvent;
import org.example.galaxy_trucker.Controller.Messages.ReadyListener;
import org.example.galaxy_trucker.Controller.Messages.TileSets.CardEvent;
import org.example.galaxy_trucker.Model.Boards.GameBoard;
import org.example.galaxy_trucker.Model.Goods.Goods;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.Cards.Card;
import org.example.galaxy_trucker.Model.PlayerStates.PlayerState;
import org.example.galaxy_trucker.Model.PlayerStates.Waiting;
import org.example.galaxy_trucker.Model.Tiles.Tile;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Random;

public class Player implements Serializable {

    private HandListener handListener;
    private GameBoard CommonBoard;
    private PlayerBoard myPlayerBoard;
    private String ID;
    private boolean ready;
    private  boolean HasActed;
    private CardListner cardListner;
    private Tile CurrentTile;   //the tile that Player has in his hand
    private PlayerState PlayerState;
    private ArrayList<Goods> GoodsToHandle;
    private Card CurrentCard;
    private PhaseListener phaseListener;
    private ReadyListener readyListener;
    private FinishListener finishListener;

    public FinishListener getFinishListener() {
        return finishListener;
    }

    public void setFinishListener(FinishListener finishListener) {
        this.finishListener = finishListener;
    }

    public ReadyListener getReadyListener() {
        return readyListener;
    }

    public void setReadyListener(ReadyListener readyListener) {
        this.readyListener = readyListener;
    }

    public GameBoard getCommonBoard() {
        return CommonBoard;
    }


    public void setPhaseListener(PhaseListener phaseListener) {
        this.phaseListener = phaseListener;
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





    public Player()  {
        ready = false;
        HasActed = false;
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

        System.out.println("Listener null?");
        if (phaseListener != null) {
            System.out.println("Phase listener not null "+ state.toClientState().getClass());
            phaseListener.PhaseChanged(state.toClientState());
        }
        state.shouldAct(this);

    }

    public void setMyPlance(PlayerBoard myPlance) {
        this.myPlayerBoard = myPlance;
    }

    public void  execute() {
        this.CurrentCard.ActivateCard();
    } // possibilmente inutile

    public void StartTimer() throws RuntimeException {
        CommonBoard.callHourglass(this);
    }



    public void PickNewTile(int index)  {
        if (index == -1){
            if (CurrentTile != null) {
                throw new IllegalStateException("You can't pick a Tile, you have already one!");
            }
            CurrentTile = CommonBoard.getTilesSets().getNewTile();
            System.out.println("Id Tile: " +CurrentTile.getId());
            if (handListener != null) {
                handListener.handChanged(new HandEvent(CurrentTile.getId(), CurrentTile.getConnectors()));
            }
        }
        else if (index >= 0 ){
            if (CurrentTile != null) {
                throw new IllegalStateException("You can't pick a Tile, you have already one!");
            }
            try{
                CurrentTile = CommonBoard.getTilesSets().getNewTile(index);
                System.out.println("Id Tile: " +CurrentTile.getId());
                if (handListener != null) {
                    handListener.handChanged(new HandEvent(CurrentTile.getId(), CurrentTile.getConnectors()));
                }
            }catch(RuntimeException e){
                System.out.println(e);
                CurrentTile = null;
            }
        }
        else {
            System.out.println("mbà non va bene minore di 0");
        }
    }

    /**
     * discards the current tile and places it back in the uncovered tiles list
     */
    public void DiscardTile() throws RemoteException, JsonProcessingException {
        if (CurrentTile == null) {
            throw new IllegalStateException("You can't discard a Tile, you don't have one!");
        }
        if (CurrentTile.getChosen()){
            throw new IllegalStateException("You can't discard this Tile!");
        }

        System.out.println("\n discarding tile\n");
        CommonBoard.getTilesSets().AddUncoveredTile(CurrentTile);
        CurrentTile = null;
        System.out.println("listener hand");
        handListener.handChanged(new HandEvent(158, null));
    }

    /**
     * places the current tile in the buffer
     */
    public void PlaceInBuffer()  {
        myPlayerBoard.insertBuffer(CurrentTile);
        CurrentTile = null;
        handListener.handChanged(new HandEvent(158, null));
    }

    /**
     * takes a tile from the buffer
     * @param index of the tile to pick
     */
    public void SelectFromBuffer(int index) {
        if (CurrentTile != null) {
            throw new IllegalStateException("You can't select a Tile, you have already one!");
        }
        CurrentTile = myPlayerBoard.getTileFromBuffer(index);
        handListener.handChanged(new HandEvent(CurrentTile.getId(), CurrentTile.getConnectors()));
      }


//    /**
//     * sets the current tile on the shipboard
//     * this action is definitive: the tile cannot be moved or rotated after it is settled
//     * @param coords where the tile will be placed
//     */
//    public void PlaceTile(IntegerPair coords) {
//
//        this.myPlayerBoard.insertTile(CurrentTile, coords.getFirst(), coords.getSecond(), true);
//        CurrentTile = null;
//    }

    public void setCard(Card NewCard){
        CurrentCard = NewCard;

        if (cardListner!=null){
            cardListner.newCard(new CardEvent(NewCard.getId()));
        }

    }


    /**
     * once a player is done building his ship (or the time is up), this method sets his starting position on the common board
     */
    public void EndConstruction() throws IllegalStateException{
        if(getCommonBoard().getLevel() ==1)
            CommonBoard.SetStartingPosition(this);
        else throw new IllegalStateException("Called a lv 1 command in a lv 2 game!");
    }


    public void EndConstruction(int index) throws IllegalStateException, IllegalArgumentException{
        if(getCommonBoard().getLevel() ==2)
            try {
                CommonBoard.SetStartingPosition(this, index);
            }catch(IllegalStateException e){
                System.out.println("BRO ESPLOSA END OF CONSTRUCTION");
                throw e;
            }
        else throw new IllegalStateException("Called a lv 2 command in a lv 1 game!");
    }

    public int finishRace(boolean finished, String message){
        int result = 0;
        result =  getmyPlayerBoard().finishRace(finished);
        if (result > 0){
            finishListener.onEndGame(true, GetID(), message);
        }
        else{
            finishListener.onEndGame(false, GetID(), message);
        }
        return result;
    }


    public void SetReady(boolean ready){
        this.ready = ready;
        this.readyListener.onReady();
    }

    public void SetHasActed(boolean hasActed){
        this.HasActed = hasActed;
    }
    public void setId(String id){this.ID = id;}

    public void setBoards(GameBoard CommonBoard) {
        this.CommonBoard = CommonBoard;
        myPlayerBoard = new PlayerBoard(CommonBoard.getLevel());
    }



    public String GetID() {return this.ID;}

    public boolean GetReady() {return this.ready;}

    public boolean GetHasActed() {
        return HasActed;
    }

    public PlayerBoard getmyPlayerBoard() {return myPlayerBoard;}

    public Card getCurrentCard() {
        return CurrentCard;
    }

    public void setHandListener(HandListener handListener) {
        this.handListener = handListener;
    }

    public void removeHandListener(){
        this.handListener = null;
    }

    public void setCardListner(CardListner cardListner) {
        this.cardListner = cardListner;
    }

    public void removeCardListener(){
        this.cardListner = null;
    }


}
