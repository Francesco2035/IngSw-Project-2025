package org.example.galaxy_trucker;
import java.util.ArrayList;
import java.util.Random;

public class Player {

    private GameBoard CommonBoard;
    private PlayerBoard myPlance;
    private String ID;
    private boolean ready;
    private int credits;
    private Tile CurrentTile;   //the tile that Player has in his hand


    public Player(String id, GameBoard board) {
        CommonBoard = board;
        myPlance = new PlayerBoard(board.getLevel());
        ID = id;
        credits = 0;
        ready = false;
        CurrentTile = null;
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



    public int RollDice() {
        Random r = new Random();
        int d1 = r.nextInt(6) + 1;
        int d2 = r.nextInt(6) + 1;
        return d1+d2;
    }




    public void PickNewTile(){      //get a new random tile
        CurrentTile = CommonBoard.getTilesSets().getNewTile();
    }

    public void PickNewTile(int index){     //select a new tile from the uncovered set
        CurrentTile = CommonBoard.getTilesSets().getNewTile(index);
    }

    public void DiscardTile(){
        CommonBoard.getTilesSets().AddUncoveredTile(CurrentTile);
        CurrentTile = null;
    }

    public void PlaceInBuffer(){
        myPlance.insertBuffer(CurrentTile);
        CurrentTile = null;
    }

    public void SelectFromBuffer(int index){
        CurrentTile = myPlance.getBuffer().get(index);
        myPlance.getBuffer().remove(index);
    }


    public void PlaceTile(int x, int y){
        this.myPlance.insertTile(CurrentTile, x, y);
        CurrentTile = null;
    }

    public void RightRotate() {CurrentTile.RotateDx();}
    public void LeftRotate() {CurrentTile.RotateSx();}



    public void IncreaseCredits(int num){
        credits += num;
    }

    public void SetReady(){
        this.ready = true;
    }


    public String GetID() {return this.ID;}
    public int GetCredits() {return this.credits;}
    public boolean GetReady() {return this.ready;}
    public PlayerBoard getMyPlance() {return myPlance;}
    public ArrayList <IntegerPair> getHumans(){return this.myPlance.getHumans();}
    public ArrayList<IntegerPair> getEnergyTiles(){return this.myPlance.getEnergyTiles();}

}
